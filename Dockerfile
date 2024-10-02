FROM openjdk:17-jdk-slim-buster

COPY ./build/libs/platform-0.0.1-SNAPSHOT.jar app.jar
COPY ./src/main/resources/.env .env

EXPOSE 8080

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
