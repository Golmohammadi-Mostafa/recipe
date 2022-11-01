This project has been developed to add, update, remove and get recipes.

### Requirements To Run Application Without Docker

* JDK 11
* PostgresSQL
* Maven
* you can see APIs: [Swagger](http://localhost:8080/swagger-ui.html#)
* In the root directory you can find postman collection

##

You can use the following operations to search and find recipes in the search filter:

* EQUAL / LiKE (for String) :
* NOT_EQUAL/ NOT_LIKE !:
* GREATER_THAN >
* GREATER_THAN_ EQUAL >:
* LESS_THAN <
* LESS_THAN_EQUAL <:
* key: the field name, for example, vegetarian, instructions, etc.
* operation: the operation, for example, equality, less than, etc.
* value: the field value, for example, oven, etc.
* search filter recipes for example (instructions:oven,vegetarian:false)
* search filter ingredients for example (name:onion)

##

**Note: Once you have successfully logged in and obtained the token, you should introduce it with the prefix "Bearer .**

### Dependencies And Tools Used To Build Application

* Git
* JDK 11
* Spring Boot
* Spring security
* data-jpa
* starter-web
* Maven
* Lombok
* MapStruct
* Swagger
* liquibase

**For detailed information refer to pom.xml**

### How to dockerized SpringBoot App & PostgresSQL

* There is a **Dockerfile** in the root directory `Dockerfile and docker-compose.yml`, this file is used to dockerized
  the SpringBoot App.

* The last and most important file is **docker-compose.yml**, which is available in the `recipe` directory, this file
  contains the configuration which will start the **SpringBoot App** and **PostgresSQL** and make them connected.

### Start the Application with the help of Docker

Go to the `recipe` directory and execute the following commands in the terminal

    1- mvn clea package  2- docker-compose -f docker-compose.yml up --build

And we are done, the **SpringBoot App** will be starting on port **8080** and **PostgresSQL** is on **5434**

Now you can open the swagger to access the APIs:
[Swagger](http://localhost:8080/swagger-ui.html#)


<p align="center">
  <b>Thank You </b>
</p>
