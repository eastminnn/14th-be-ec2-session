# 1단계: Gradle과 JDK 17이 있는 이미지에서 Spring Boot 프로젝트를 빌드한다.
FROM gradle:8.8-jdk17 AS build

# 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일 전체를 컨테이너 내부로 복사
COPY . .

# Gradle Wrapper 실행 권한 부여
RUN chmod +x ./gradlew

# 테스트를 제외하고 Spring Boot jar 파일 빌드
RUN ./gradlew clean bootJar -x test


# 2단계: 실제 실행에 필요한 JDK 17 환경만 사용한다.
FROM openjdk:17-jdk-slim

# 컨테이너 내부 작업 디렉토리 설정
WORKDIR /app

# 1단계에서 만들어진 jar 파일을 app.jar 이름으로 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Spring Boot 기본 실행 포트
EXPOSE 8080

# 컨테이너가 실행될 때 Spring Boot 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]