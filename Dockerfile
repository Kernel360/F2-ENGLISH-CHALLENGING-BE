FROM openjdk:17-jdk-slim-buster

ARG PROFILES

RUN apt-get -y update
RUN apt -y install wget
RUN apt -y install unzip
RUN apt -y install curl

# google chrome 설치
RUN wget https://chrome-versions.com/google-chrome-stable-114.0.5735.106-1.deb
#RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN apt-get -y update
RUN apt -y install ./google-chrome-stable-114.0.5735.106-1.deb
#RUN apt -y install ./google-chrome-stable_current_amd64.deb

# chromedriver 설치
RUN wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/` curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE_114`/chromedriver_linux64.zip
RUN unzip /tmp/chromedriver.zip chromedriver -d .

COPY ./build/libs/platform-0.0.1-SNAPSHOT.jar biengual/app.jar
COPY ./src/main/resources/.env biengual/.env

EXPOSE 8080

WORKDIR /biengual
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]