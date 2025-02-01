##################################################
# STAGE 1: Build your app (using Maven)
##################################################
FROM maven:3.9.2-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy the Maven configuration (pom.xml) and source code
COPY pom.xml ./
COPY src ./src

# If you have resources like .env in your root, copy them if needed
# but typically you don't need .env for the build itself:
# COPY .env ./

# Run Maven inside Docker to build the project
RUN mvn clean package -DskipTests


##################################################
# STAGE 2: Create the final runtime image
##################################################
FROM openjdk:17-jdk-slim

WORKDIR /app

# If you need .env in the final container, copy it here
ARG ENV_FILE_PATH
COPY ${ENV_FILE_PATH} /app/.env

# Now, copy the built JAR from the 'builder' stage
COPY --from=builder /app/target/*.jar app.jar

# Expose your app's port (optional)
EXPOSE 8082

# Specify the entrypoint to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]