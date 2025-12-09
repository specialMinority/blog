-- 1. 기존 게시글 테이블 (이미 있음)
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