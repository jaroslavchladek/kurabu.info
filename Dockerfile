# Use OpenJDK base image
FROM openjdk:17-jdk-slim

# Set the maintainer
MAINTAINER jaroslavchladek.com

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Set the working directory
WORKDIR /app

# Copy the Maven project files into the image
COPY pom.xml /app
COPY src /app/src

# Build the project using Maven
RUN mvn clean install -DskipTests

# Copy the built jar file to the final image
COPY target/*.jar app.jar

# Define the entry point
ENTRYPOINT ["java", "-jar", "/app.jar"]
