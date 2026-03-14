# -----------------------------
# Builder stage
# -----------------------------
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copy Gradle wrapper & settings
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src ./src

# Build the Spring Boot jar
RUN ./gradlew bootJar --no-daemon

# -----------------------------
# Runtime stage
# -----------------------------
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Copy the jar from builder
COPY --from=builder /app/build/libs/*.jar app.jar

# Create directory for SQLite database persistence
RUN mkdir -p /app/data

# Expose application port
EXPOSE 8080

# Use JVM flags and environment variable for Spring profile
# SPRING_PROFILES_ACTIVE will be injected from docker-compose.yml
# ENTRYPOINT ["sh", "-c", "java --enable-native-access=ALL-UNNAMED --sun-misc-unsafe-memory-access=allow -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE -jar app.jar"]
ENTRYPOINT ["java", "-Xms128m", "-Xmx256m", "--enable-native-access=ALL-UNNAMED", "--sun-misc-unsafe-memory-access=allow", "-jar", "app.jar"]
