-- 애플리케이션 시작 시 'post' 테이블이 없으면 생성합니다.
-- (MySQL 문법 기준)
CREATE TABLE IF NOT EXISTS post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    author VARCHAR(255)
);