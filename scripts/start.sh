#!/bin/bash

ROOT_PATH="/home/ec2-user/F2-ENGLISH-CHALLENGING-BE"
JAR="$ROOT_PATH/build/libs/platform-0.0.1-SNAPSHOT.jar"

APP_LOG="$ROOT_PATH/platform-0.0.1-SNAPSHOT.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] $JAR 복사" >> $START_LOG
cp $ROOT_PATH/build/libs/platform-0.0.1-SNAPSHOT.jar $JAR

echo "[$NOW] > $JAR 실행" >> $START_LOG
nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > 서비스 PID: $SERVICE_PID" >> $START_LOG