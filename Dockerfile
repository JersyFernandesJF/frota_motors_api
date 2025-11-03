# Multi-stage build for Spring Boot application

# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml and download dependencies (cached layer)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime image
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Install wget for health checks
RUN apk add --no-cache wget

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 9090

# Health check (checking if the app is responding)
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:9090/swagger-ui.html || exit 1

# Run the application with docker profile
ENTRYPOINT ["java", "-Dspring.profiles.active=docker", "-jar", "app.jar"]

