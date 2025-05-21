# Use Temurin 17 JDK as base image
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built jar from local context
COPY queute-0.0.1-SNAPSHOT.jar app.jar

# Expose port 
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]

