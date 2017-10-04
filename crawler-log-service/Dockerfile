FROM openjdk:8u131-jdk-alpine

RUN mkdir /app
WORKDIR /app
COPY ./build/libs/crawler-log-service-*.jar service.jar

ENTRYPOINT ["java", "-jar" , "service.jar"]

 

