# 1단계: Gradle + JDK 17 환경에서 프로젝트 빌드
FROM gradle:8.8-jdk17 AS build

WORKDIR /app

COPY . .

RUN chmod +x ./gradlew

RUN ./gradlew clean bootJar -x test


# 2단계: 실제 실행용 이미지
FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]