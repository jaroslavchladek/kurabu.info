FROM openjdk:17-jdk-slim
MAINTAINER jaroslavchladek.com
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]