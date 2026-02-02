import os
import time
import yaml
import logging
import subprocess
import tempfile
from datetime import datetime
from pathlib import Path
from watchdog.observers import Observer
from watchdog.events import FileSystemEventHandler

# 로깅 설정
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('bridge.log'),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)


class InputFileHandler(FileSystemEventHandler):
    """input.md 파일 변경을 감지하고 처리하는 핸들러"""
    
    def __init__(self, config):
        self.config = config
        self.sync_folder = Path(config['sync_folder'])
        self.input_file = self.sync_folder / config['input_file']
        self.output_file = self.sync_folder / config['output_file']
        self.processing_flag = self.sync_folder / '.processing'
        self.last_modified = 0
        self.debounce_seconds = config.get('debounce_seconds', 2)
        
        # 히스토리 폴더 생성
        if config.get('enable_history', False):
            self.history_folder = self.sync_folder / config['history_folder']
            self.history_folder.mkdir(exist_ok=True)
        else:
            self.history_folder = None
    
    def on_modified(self, event):
        """파일 수정 이벤트 처리"""
        if event.src_path != str(self.input_file):
            return
        
        # Debounce: 너무 빠른 연속 저장 방지
        current_time = time.time()
        if current_time - self.last_modified < self.debounce_seconds:
            logger.debug(f"Debouncing: {current_time - self.last_modified:.2f}s since last modification")
            return
        
        self.last_modified = current_time
        self.process_input()
    
    def process_input(self):
        """input.md를 읽고 Antigravity에 전송 후 output.md에 작성"""
        
        # 이미 처리 중이면 스킵
        if self.processing_flag.exists():
            logger.warning("Already processing another request, skipping...")
            return
        
        try:
            # 처리 중 플래그 생성
            self.processing_flag.touch()
            logger.info("Processing input.md...")
            
            # input.md 읽기
            if not self.input_file.exists():
                logger.error("input.md not found!")
                return
            
            with open(self.input_file, 'r', encoding='utf-8') as f:
                prompt = f.read().strip()
            
            if not prompt:
                logger.warning("input.md is empty, skipping...")
                return
            
            logger.info(f"Prompt: {prompt[:100]}...")  # 처음 100자만 로그
            
            # Antigravity 호출 (현재는 더미 응답)
            response = self.call_antigravity(prompt)
            
            # output.md에 작성
            with open(self.output_file, 'w', encoding='utf-8') as f:
                f.write(response)
            
            logger.info("Response written to output.md")
            
            # 히스토리 저장
            if self.history_folder:
                self.save_history(prompt, response)
            
        except Exception as e:
            logger.error(f"Error processing input: {e}", exc_info=True)
            # 에러를 output.md에 작성
            with open(self.output_file, 'w', encoding='utf-8') as f:
                f.write(f"❌ Error: {str(e)}\n\nPlease check bridge.log for details.")
        
        finally:
            # 처리 중 플래그 제거
            if self.processing_flag.exists():
                self.processing_flag.unlink()
    
    def call_antigravity(self, prompt: str) -> str:
        """
        Antigravity에 프롬프트를 전송하고 응답을 받음
        
        Antigravity CLI를 subprocess로 실행하여 실제 응답을 받음
        """
        import subprocess
        import tempfile
        
        logger.info("Calling Antigravity CLI...")
        
        # CLI 경로 가져오기
        cli_path = self.config.get('antigravity_cli_path', 'antigravity')
        logger.debug(f"Using CLI: {cli_path}")
        
        try:
            # 임시 파일에 프롬프트 작성
            with tempfile.NamedTemporaryFile(mode='w', suffix='.md', delete=False, encoding='utf-8') as tmp:
                tmp.write(prompt)
                tmp_path = tmp.name
            
            logger.debug(f"Created temp file: {tmp_path}")
            
            # Antigravity CLI 실행
            result = subprocess.run(
                [cli_path, tmp_path],
                capture_output=True,
                text=True,
                timeout=120,  # 2분 타임아웃
                encoding='utf-8'
            )
            
            # 임시 파일 삭제
            try:
                os.unlink(tmp_path)
            except:
                pass
            
            if result.returncode != 0:
                error_msg = f"Antigravity CLI failed with code {result.returncode}"
                logger.error(error_msg)
                logger.error(f"stderr: {result.stderr}")
                return f"❌ Error: {error_msg}\n\nStderr:\n{result.stderr}"
            
            # 응답 반환
            response = result.stdout.strip()
            
            if not response:
                logger.warning("Antigravity returned empty response")
                return "⚠️ Antigravity returned an empty response. Please check if the CLI is working correctly."
            
            logger.info(f"Received response ({len(response)} chars)")
            return response
            
        except subprocess.TimeoutExpired:
            logger.error("Antigravity CLI timed out (120s)")
            return "❌ Error: Antigravity CLI timed out after 120 seconds."
        
        except FileNotFoundError:
            logger.error(f"Antigravity CLI not found: {cli_path}")
            return f"❌ Error: Antigravity CLI not found at '{cli_path}'. Please check config.yaml."
        
        except Exception as e:
            logger.error(f"Unexpected error calling Antigravity: {e}", exc_info=True)
            return f"❌ Unexpected error: {str(e)}\n\nPlease check bridge.log for details."
    
    def save_history(self, prompt: str, response: str):
        """대화 기록을 히스토리 폴더에 저장"""
        timestamp = datetime.now().strftime('%Y-%m-%d_%H-%M-%S')
        history_file = self.history_folder / f"{timestamp}.md"
        
        with open(history_file, 'w', encoding='utf-8') as f:
            f.write(f"# Conversation - {timestamp}\n\n")
            f.write(f"## Prompt\n\n{prompt}\n\n")
            f.write(f"## Response\n\n{response}\n")
        
        logger.info(f"History saved: {history_file.name}")


def load_config(config_path='config.yaml'):
    """설정 파일 로드"""
    if not os.path.exists(config_path):
        logger.error(f"Config file not found: {config_path}")
        raise FileNotFoundError(f"Please create {config_path}")
    
    with open(config_path, 'r', encoding='utf-8') as f:
        config = yaml.safe_load(f)
    
    return config


def main():
    """메인 실행 함수"""
    logger.info("=" * 60)
    logger.info("Antigravity Mobile Bridge - File Watcher")
    logger.info("=" * 60)
    
    # 설정 로드
    try:
        config = load_config()
        logger.info(f"Config loaded: {config['sync_folder']}")
    except Exception as e:
        logger.error(f"Failed to load config: {e}")
        return
    
    # 동기화 폴더 확인
    sync_folder = Path(config['sync_folder'])
    if not sync_folder.exists():
        logger.error(f"Sync folder not found: {sync_folder}")
        logger.info("Creating sync folder...")
        sync_folder.mkdir(parents=True, exist_ok=True)
    
    # input.md, output.md 초기화
    input_file = sync_folder / config['input_file']
    output_file = sync_folder / config['output_file']
    
    if not input_file.exists():
        input_file.write_text("# Write your prompt here\n\n", encoding='utf-8')
        logger.info(f"Created {input_file}")
    
    if not output_file.exists():
        output_file.write_text("# Waiting for input...\n", encoding='utf-8')
        logger.info(f"Created {output_file}")
    
    # 파일 감시 시작
    event_handler = InputFileHandler(config)
    observer = Observer()
    observer.schedule(event_handler, str(sync_folder), recursive=False)
    observer.start()
    
    logger.info(f"👀 Watching: {input_file}")
    logger.info("Press Ctrl+C to stop...")
    
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        logger.info("Stopping watcher...")
        observer.stop()
    
    observer.join()
    logger.info("Watcher stopped.")


if __name__ == '__main__':
    main()
