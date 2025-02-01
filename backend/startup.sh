#!/bin/bash
#nohup java -jar /path/to/app/hello-world.jar > /path/to/log.txt 2>&1 &
# https://studygyaan.com/spring-boot/deploy-spring-boot-app-on-vm-using-nginx-https-domain#google_vignette
nohup ./mvnw spring-boot:run > log.txt 2>&1 &
echo $! > ./pid.file