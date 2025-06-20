# Use Java 21 with Maven (for building inside Docker)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copy all project files and build
COPY . .
RUN mvn clean package -DskipTests

# Use a smaller runtime image
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copy the jar from the builder image
COPY --from=build /app/target/SuccessScoreCalculationHydration-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]