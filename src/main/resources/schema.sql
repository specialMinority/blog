-- 애플리케이션 시작 시 'post' 테이블이 없으면 생성합니다.
-- (MySQL 문법 기준)
CREATE TABLE IF NOT EXISTS post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    author VARCHAR(255)
);

-- 2. [추가] 댓글 테이블
CREATE TABLE IF NOT EXISTS comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,          -- 외래 키 (어떤 글의 댓글인지)
    content VARCHAR(500) NOT NULL,    -- 댓글 내용
    author VARCHAR(255) NOT NULL,     -- 작성자

    -- 외래 키 제약조건 (선택사항이지만 데이터 무결성을 위해 권장)
    FOREIGN KEY (post_id) REFERENCES post(id) ON DELETE CASCADE
);