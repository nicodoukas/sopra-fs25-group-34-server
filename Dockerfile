# Use Eclipse Temurin JDK 17 as the base image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the built Spring Boot JAR into the container
COPY /app/build/libs/soprafs24.jar app.jar

# Expose port 8080 — required by App Engine
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
