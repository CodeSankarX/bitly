# syntax=docker/dockerfile:1.6

# Build stage: compile Spring Boot application and create bootable jar
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /workspace

# Leverage Gradle wrapper included in the repo
COPY gradlew gradlew
COPY gradle gradle
RUN chmod +x gradlew

# Copy build configuration first to enable better layer caching
COPY build.gradle settings.gradle ./

# Copy sources
COPY src src

# Pre-fetch dependencies (best-effort to warm cache) and build without tests
RUN ./gradlew --no-daemon clean bootJar -x test

# Runtime stage: lightweight JRE image
FROM eclipse-temurin:17-jre-jammy

ENV TZ=Etc/UTC
WORKDIR /app

# Copy the fat jar from the build stage
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

# Run as non-root for better security
RUN useradd -u 1001 -m appuser
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
