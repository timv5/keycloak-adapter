# keycloak-adapter

Springboot application which uses Keycloak for authentication & authorization.
It can be easily used wit your custom frontend client. 

## Technologies used
- java
- springboot
- postgres
- keycloak

## Description

Project contains simple rest api backend application with registration on Keycloak. 
It has admin privileges on Keycloak defined in application.properties. 
There is only one POST endpoint which accepts customer details as a body and saves it in
postgres database. Then it also saves it on Keycloak. If there is any problem on Keycloak
side, rollback is triggered.

## How to run a project?
1. in root directory run: `docker-compose -f docker-compose.yml up -d`
It will run 2 containers. First a postgres database and second one Keycloak. Credentials
are located in database.env & keycloak.env file. Once keycloak is running import a new realm
(in a root project folder there is a config file realm-config.json) - new realm with sample users
will be create.
2. create db schema - execute ddl.sql in the root project
3. run springboot application.

## Keycloak
Keycloak is accessible on: http://localhost:8180
