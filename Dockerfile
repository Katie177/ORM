FROM --platform=linux/amd64 maven:3.9.6-eclipse-temurin-17-alpine AS builder

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Final image
FROM --platform=linux/amd64 eclipse-temurin:17-jre-alpine

# Create app directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/education-platform-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]