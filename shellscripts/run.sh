#!/bin/sh

if [ -d ./target ]; then
    echo "/target is ready. run jar"
    sudo nohup java -jar target/demo-1.0.0-DEPLOY.jar >> nohup.out 2>&1 &
else
    echo "error: /target is not ready"
fi