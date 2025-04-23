# Step 1: Use a Gradle image to build the JAR
FROM gradle:7.6-jdk17 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Gradle wrapper and build files
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# Run the Gradle build to generate the .jar
RUN gradle clean build

# Step 2: Create the runtime image with the JAR
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy the generated JAR from the builder step
COPY --from=builder /app/build/libs/soprafs24.jar app.jar

# Expose port 8080 — required by App Engine
EXPOSE 8080

# Start the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
