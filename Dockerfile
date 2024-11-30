FROM openjdk:17-jdk-slim
MAINTAINER jaroslavchladek.com
# Build the project using Maven
RUN mvn clean install -DskipTests
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]