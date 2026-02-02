# Antigravity Mobile Bridge

Google Drive 파일 동기화를 활용하여 모바일에서 데스크탑 Antigravity를 제어하는 시스템입니다.

## 📋 개요

- **문제:** Chrome 원격 데스크톱으로 모바일에서 PC 조작 시 작은 화면에서 프롬프트 입력이 불편함
- **해결:** 모바일에서 `input.md` 편집 → 자동 동기화 → PC에서 Antigravity 실행 → `output.md`로 결과 확인

## 🚀 빠른 시작

### 1. 사전 준비

- **Google Drive 설치** (또는 OneDrive)
  - Windows: [Google Drive for Desktop](https://www.google.com/drive/download/)
  - 설치 후 동기화 폴더 경로 확인 (예: `C:\Users\PC\Google Drive`)

- **Python 3.8 이상**
  ```bash
  python --version
  ```

### 2. 설치

```bash
# 1. 의존성 설치
pip install -r requirements.txt

# 2. config.yaml 수정
# sync_folder 경로를 본인의 Google Drive 경로로 변경
notepad config.yaml
```

**config.yaml 예시:**
```yaml
sync_folder: "C:\\Users\\YourName\\Google Drive\\AntigravityBridge"
input_file: "input.md"
output_file: "output.md"
history_folder: "history"
enable_history: true
debounce_seconds: 2
```

### 3. 실행

```bash
python bridge_watcher.py
```

실행하면 자동으로 Google Drive에 `AntigravityBridge` 폴더와 `input.md`, `output.md` 파일이 생성됩니다.

## 📱 모바일 사용법

### iOS

1. **Google Drive 앱** 열기
2. `AntigravityBridge` 폴더로 이동
3. `input.md` 파일 열기
4. 질문 작성 후 저장
5. 5~10초 후 `output.md` 열어서 답변 확인

### Android

1. **Google Drive 앱** 열기
2. `AntigravityBridge` 폴더로 이동
3. `input.md` 파일 열기 (또는 텍스트 에디터 연결)
4. 질문 작성 후 저장
5. 5~10초 후 `output.md` 열어서 답변 확인

## ⚙️ Antigravity 연동

현재 스크립트는 **더미 응답**을 반환합니다. 실제 Antigravity와 연동하려면 `bridge_watcher.py`의 `call_antigravity()` 메서드를 수정해야 합니다.

### 연동 방법 (선택)

#### 방법 1: 파일 기반 통신
Antigravity가 특정 폴더의 파일을 읽고 쓰는 방식이라면:
```python
def call_antigravity(self, prompt: str) -> str:
    # 1. Antigravity 입력 파일에 프롬프트 작성
    with open('antigravity_input.txt', 'w') as f:
        f.write(prompt)
    
    # 2. Antigravity가 처리할 때까지 대기
    while not os.path.exists('antigravity_output.txt'):
        time.sleep(0.5)
    
    # 3. 결과 읽기
    with open('antigravity_output.txt', 'r') as f:
        return f.read()
```

#### 방법 2: CLI 실행
Antigravity CLI가 있다면:
```python
import subprocess

def call_antigravity(self, prompt: str) -> str:
    result = subprocess.run(
        ['antigravity', '--prompt', prompt],
        capture_output=True,
        text=True
    )
    return result.stdout
```

#### 방법 3: HTTP API
Antigravity가 HTTP 서버를 제공한다면:
```python
import requests

def call_antigravity(self, prompt: str) -> str:
    response = requests.post(
        'http://localhost:5000/api/prompt',
        json={'prompt': prompt}
    )
    return response.json()['response']
```

## 🔧 백그라운드 실행

### Windows 작업 스케줄러

1. **작업 스케줄러** 열기 (`taskschd.msc`)
2. **작업 만들기** 클릭
3. **일반** 탭:
   - 이름: `Antigravity Bridge`
   - "사용자가 로그온할 때만 실행" 선택
4. **트리거** 탭:
   - 새로 만들기 → "로그온할 때"
5. **동작** 탭:
   - 새로 만들기 → "프로그램 시작"
   - 프로그램: `python`
   - 인수: `C:\Users\PC\Downloads\blog\mobile-bridge\bridge_watcher.py`
   - 시작 위치: `C:\Users\PC\Downloads\blog\mobile-bridge`
6. 확인

이제 PC를 켤 때마다 자동으로 스크립트가 실행됩니다.

### 수동 백그라운드 실행

```bash
# PowerShell에서
Start-Process python -ArgumentList "bridge_watcher.py" -WindowStyle Hidden
```

## 📂 파일 구조

```
Google Drive/AntigravityBridge/
├── input.md           # 모바일에서 편집하는 질문 파일
├── output.md          # Antigravity 응답 결과
└── history/           # 과거 대화 기록 (enable_history: true일 때)
    ├── 2026-01-29_15-30-45.md
    └── 2026-01-29_16-20-12.md
```

## 🐛 트러블슈팅

### 파일 변경이 감지되지 않아요
- Google Drive 동기화 상태 확인
- `config.yaml`의 `sync_folder` 경로가 정확한지 확인
- `bridge.log` 파일 확인

### 동기화가 너무 느려요
- Google Drive 대신 OneDrive 사용 (일반적으로 더 빠름)
- `debounce_seconds`를 줄이기 (단, 너무 작으면 중복 처리 가능)

### 모바일에서 파일을 편집할 수 없어요
- Google Drive 앱에서 "오프라인 사용 가능" 설정
- 또는 Markdown 에디터 앱 사용 (iOS: Drafts, Android: Markor)

## 📝 로그

모든 활동은 `bridge.log` 파일에 기록됩니다.

```bash
# 실시간 로그 확인 (PowerShell)
Get-Content bridge.log -Wait -Tail 20
```

## 🔒 보안

- 민감한 정보는 `input.md`에 작성하지 마세요 (Google Drive에 동기화됨)
- 히스토리 기능이 활성화되어 있으면 모든 대화가 저장됩니다

## 📄 라이선스

MIT License
