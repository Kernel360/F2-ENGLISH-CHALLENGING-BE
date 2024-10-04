FROM openjdk:17-jdk-slim-buster

ARG PROFILES

COPY ./build/libs/platform-0.0.1-SNAPSHOT.jar app.jar
COPY ./src/main/resources/.env .env

EXPOSE 8080

WORKDIR /biengual
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]