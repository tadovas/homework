## Rest service/client demo project
### You will need:
* Installed JDK *1.8+* (obviously)
* Docker and docker-compose
### Run _./runMe.sh_ to get system up and running
#### A few words: 

- One crawler log service and 5 crawlers will be started. Each crawler will attempt to create a crawler log, 
fill it with celebrities, and close it.

- Service supports up to 100 elements in storage - after that it will become unhealthy (status: DOWN). Crawlers will respect that :) (will stop creating logs and just sleep in loop). 

- It will take (100 max elemnts divided by 5 crawlers) x 2 seconds sleep = 40 seconds to fill up repository and make service unhealthy.

- A full repo dump can be accessible through crawler-log-search link. Visit [http://localhost:8080](http://localhost:8080/) for list of available APIs
