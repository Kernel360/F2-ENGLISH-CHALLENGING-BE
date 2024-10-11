FROM openjdk:17-jdk-slim-buster

ARG PROFILES

RUN sudo apt-get -y update
RUN sudo apt -y install wget
RUN sudo apt -y install unzip
RUN sudo apt -y install curl
RUN sudo apt-get -y install xvfb

# google chrome 설치
RUN sudo wget https://chrome-versions.com/google-chrome-stable-114.0.5735.106-1.deb
#RUN wget https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb
RUN sudo apt-get -y update
RUN sudo apt -y install ./google-chrome-stable-114.0.5735.106-1.deb
#RUN apt -y install ./google-chrome-stable_current_amd64.deb

# chromedriver 설치
RUN sudo wget -O /tmp/chromedriver.zip https://chromedriver.storage.googleapis.com/` curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE_114`/chromedriver_linux64.zip
RUN sudo unzip /tmp/chromedriver.zip chromedriver -d .

# Xvfb 설정
RUN Xvfb :99 -ac &
ENV DISPLAY=:99

COPY ./build/libs/platform-0.0.1-SNAPSHOT.jar biengual/app.jar
COPY ./src/main/resources/.env biengual/.env

EXPOSE 8080

WORKDIR /biengual
ENTRYPOINT ["java", "-Dspring.profiles.active=${PROFILES}", "-jar", "app.jar"]