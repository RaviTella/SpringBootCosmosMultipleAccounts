# Overview
This sample application showcases connecting to multiple Cosmos DB accounts from a single spring boot application. This application is built with Spring Boot 2.3.4  and "Spring Data Cosmos" client library(3.0.0).

# Instructions

## First:
 * Maven
 * Create three Cosmos DB SQL API Account. 
 * Clone the repository

## Then:
* Update application.properties file with the credentials of the three Cosmos DB account you have crated and database names of your choice 
* Run the application with mvn spring-boot:run 
* you will see a new database and a collection created in each of the accounts. Each collection will have a document each. 
* Logs will show the three documents read from the three collections


