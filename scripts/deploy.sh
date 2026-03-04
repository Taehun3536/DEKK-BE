#!/bin/bash

# 1. 변수 설정
REPOSITORY="/home/ubuntu/dekk-was"
APP_NAME="DEKK" # 빌드 파일명(DEKK-0.0.1.jar)과 일치하도록 대문자로 설정

echo "> 현재 구동 중인 애플리케이션 pid 확인"
# -i 옵션으로 대소문자 구분 없이 찾고, -f로 전체 경로에서 프로세스를 식별합니다.
CURRENT_PID=$(pgrep -fil $APP_NAME | grep java | awk '{print $1}')

echo "> 현재 구동 중인 애플리케이션 pid: $CURRENT_PID"

# 2. 기존 프로세스 종료
if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션 배포 시작"

# 3. 새 JAR 파일 찾기
# *SNAPSHOT.jar를 *.jar로 수정하여 실제 빌드 파일인 DEKK-0.0.1.jar를 찾도록 합니다.
# grep -v plain을 통해 실행 불가능한 plain jar 파일은 제외합니다.
JAR_NAME=$(ls -tr $REPOSITORY/build/libs/*.jar | grep -v plain | tail -n 1)

echo "> JAR Name: $JAR_NAME"

# 4. 실행 권한 부여 및 실행
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> 애플리케이션 실행 (프로필: prod)"
# nohup과 &를 사용하여 세션이 끊겨도 백그라운드에서 실행되도록 합니다.
nohup java -jar -Duser.timezone=Asia/Seoul -Dspring.profiles.active=prod $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &

echo "> 배포 완료! 로그는 $REPOSITORY/nohup.out 에서 확인할 수 있습니다."
