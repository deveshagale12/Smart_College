# Stage 1: Build
FROM maven:3.8.5-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render dynamic port binding
# This allows the PORT env var from Render to override the default 8080
EXPOSE 8080

# Use a shell-form ENTRYPOINT to allow environment variable expansion
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT:-8080}"]