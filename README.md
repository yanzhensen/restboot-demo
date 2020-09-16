# restboot-demo

##package jar
dev 

skip test => clean => package

##run jar
restboot 8888.bat
````
@echo off
title restboot 8888
java -jar -Xms200m -Xmx500m restboot-0.0.1-SNAPSHOT.jar --server.port=8888 --spring.profiles.active=dev
````
