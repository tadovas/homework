FROM openjdk:8u131-jdk-alpine

RUN mkdir /app
WORKDIR /app
COPY ./build/distributions/crawler-log-client.zip client.zip
RUN unzip client.zip


ENTRYPOINT ["crawler-log-client/bin/crawler-log-client"]

 

