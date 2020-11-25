# REST
App test using: java 14 (Spring Boot), H2 (in-memory database) and RabbiMq.
Running in Ubuntu Focal 20.04 (LTS)

Running locally:
http://localhost:<port>/api/v1/delivery

# PREREQUISITES
- Java
- Docker | https://docs.docker.com/engine/install/ubuntu/ 

# Running RabbitMq in linux
docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management

# Database
H2 in-memory

|       Delivery     |
|--------------------|
|idOrder: UIID       |
|idClient: String    |
|orderAddress: String|


# Deploy To heroku
 - The application was deployed to heroku
 - RabbitMq installed into the producer app 
 - RabbitMq add on installed on heroku app ->  heroku addons:create cloudamqp:lemur -> (https://elements.heroku.com/addons/cloudamqp)
 
 - Can be tested on:
  https://rufino-rabbitmq-consumer.herokuapp.com/api/v1/delivery/

# The producer app:
  Github:https://github.com/regisrfn/SpringBootApp
  API:https://rufino-spring-boot.herokuapp.com/