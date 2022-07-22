#!/bin/sh

pid_search=$(netstat -nap | grep 8080 | awk '{print $7}')
pid_value=${#pid_search}

if [ $pid_value == 0 ]; then
    echo "Process already terminated."
else
    echo "Start terminate process"
    pid_val=${pid_search%%'/'*}
    kill -9 $pid_val
    if [ $? == 0 ]; then
        echo 'Terminate process success'
    else
        echo 'Terminate process fail'
    fi
fi

if [ -d ./target ]; then
    echo "/target is ready. Start jar"
    sudo nohup java -jar target/demo-1.0.0-DEPLOY.jar >> nohup.out 2>&1 &
else
    echo "error: /target is not ready"
fi