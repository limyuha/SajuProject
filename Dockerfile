FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# 빌드 단계 (Maven으로 빌드)
COPY pom.xml .
COPY src ./src
RUN ./mvnw -B -DskipTests clean package

# 실행 단계
COPY target/*.jar app.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
