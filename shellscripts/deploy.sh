#!/bin/sh

maven = "/usr/local/apache-maven-3.8.6/bin"
resource_source = "/home/ec2-user/Matilda_Backend/src/main/resources"

if [ -d ./src/main/resources ]; then
    echo "application.properties is ready"
else
    echo "application.properties is not ready"
    mkdir src/main/resources
    cp ${resource_source}/application.properties ./src/main/resources
fi

${maven}/mvn package