# 🐳 Spring Boot Blog Project

Spring Boot와 Docker를 활용하여 개발된 블로그 애플리케이션입니다. AWS EC2 인스턴스에 Docker Compose를 사용하여 배포되었습니다.

## 🚀 데모 (Demo)

AWS EC2 환경에서 실제 구동 중인 서비스 화면입니다.

![Service Demo](docs/img/demo.png)

## 🛠 기술 스택 (Tech Stack)

- **Backend**: Java 17, Spring Boot 3.3.4
- **Database**: MySQL 8.0 (AWS RDS)
- **Frontend**: Thymeleaf
- **Infrastructure**: AWS EC2, Docker, Docker Compose, Nginx

## 🏗 배포 아키텍처

- **Web Server**: Nginx (Reverse Proxy)
- **App Server**: Spring Boot Container
- **Database**: AWS RDS (MySQL)
- **Network**: Docker Bridge Network

## 🏁 시작하기 (Getting Started)

### 로컬 실행
```bash
# 1. Clone the repository
git clone https://github.com/specialMinority/blog.git

# 2. Configure environment variables (.env)
# (See .env.example if available)

# 3. Run with Docker Compose
docker-compose up -d --build
```
