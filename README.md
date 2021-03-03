# Product Service #

This project is for the Config service in Austin Capital Bank and uses a microservice strategy, as opposed to a monolith-first strategy. 
It is built on top of [Spring Boot](https://projects.spring.io/spring-boot/) and [Spring Cloud](http://projects.spring.io/spring-cloud/) libraries using [Netflix OSS](https://netflix.github.io/). 


## Build

Using Maven, you can build the application:

```
$ mvn clean install

```
Maven will automatically download all dependencies. This may take a few moments.

```

## Run

You can run this service standalone, simply starting it using Maven.

```
$ mvn spring-boot:run 

```

## Tools

Since this is a Maven based project, you can choice any preferred IDE to support it. If you prefer Eclipse, there is a [Spring Tool Suite](https://spring.io/tools) available which all the plugins already installed on it. 


## Setup for Developers

### Dependencies
* [Java SDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) 
* [Maven 3](https://maven.apache.org/download.cgi)
* [Docker](https://docs.docker.com/engine/installation/) and Compose binaries
* Eclipse IDE (preferred [Spring Tool Suite](https://spring.io/tools), all plugins already included)

### Setup the environment
* Install the Eclipse IDE 
* Clone and import the repository (go to File -> Import -> Maven -> Check out Maven Projects from SCM)
* After this process completed you can check at your workspace the projects imported and created
* You can run each service individually (click at service project -> Run As -> Spring Boot App)
