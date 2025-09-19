# 1단계: 빌드 (Maven 포함된 이미지 사용)
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 의존성 먼저 다운로드 (빌드 캐시 최적화)
COPY pom.xml .
RUN mvn dependency:go-offline

# 소스 복사 후 빌드
COPY src ./src
RUN mvn clean package -DskipTests

# 2단계: 실행 (가볍게 JDK만)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 빌드 결과 JAR 복사
COPY --from=build /app/target/*.jar app.jar

# Render에서 자동으로 PORT 환경변수 내려주므로 반영
ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
