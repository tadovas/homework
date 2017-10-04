#!/bin/bash

./gradlew build && docker-compose up --scale crawler-log-client=5
