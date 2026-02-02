# ==========================================
# 1단계: 빌드 스테이지 (Build Stage)
# ==========================================
# Gradle과 JDK 17이 포함된 이미지를 사용
FROM gradle:8.5-jdk17 AS builder

# 작업 디렉토리 설정 (컨테이너 내부의 /app 폴더에서 작업)
WORKDIR /app

# Gradle 설정 파일 먼저 복사 (의존성 캐싱 최적화)
# 이렇게 하면 소스 코드가 변경되어도 의존성은 다시 다운로드하지 않음
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드 (이 레이어는 캐시됨)
RUN gradle dependencies --no-daemon

# 소스 코드 복사
COPY src ./src

# 애플리케이션 빌드 (테스트는 스킵하여 빌드 시간 단축)
RUN gradle bootJar --no-daemon -x test

# ==========================================
# 2단계: 실행 스테이지 (Runtime Stage)
# ==========================================
# JRE만 포함된 경량 이미지 사용 (Alpine Linux 기반)
FROM eclipse-temurin:17-jre-alpine

# 보안: root 사용자 대신 일반 사용자 생성
# root로 실행하면 컨테이너가 해킹당했을 때 위험
RUN addgroup -S spring && adduser -S spring -G spring

# 작업 디렉토리 설정
WORKDIR /app

# 빌드 스테이지에서 생성된 JAR 파일만 복사
# 소스 코드나 빌드 도구는 포함되지 않아 이미지 크기가 작아짐
COPY --from=builder /app/build/libs/*.jar app.jar

# 파일 소유권을 spring 사용자로 변경
RUN chown spring:spring app.jar

# 일반 사용자로 전환
USER spring

# 컨테이너가 사용할 포트 명시 (문서화 목적)
EXPOSE 8080

# 애플리케이션 실행
# JVM 옵션을 추가할 수 있음 (예: -Xmx512m)
ENTRYPOINT ["java", "-jar", "app.jar"]