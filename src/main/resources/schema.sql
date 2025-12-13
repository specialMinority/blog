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

-- 3. [추가]회원 테이블 (이름을 member로 설정)
CREATE TABLE IF NOT EXISTS member (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- (참고) 나중에 Post와 Comment 테이블에도 member_id 컬럼을 추가해야 관계가 맺어짐