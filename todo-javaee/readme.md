Todo Application based on Java EE and AngularJS
===============================================


Todo application is a web based todo list manager. It helps users to create new todos, 
view entered todos, update specific todo and delete selected todo. 

Server side is based on Java EE platform. Client side laverages AngularJS framework. 
It exposes business logic through the REST API which are consumed by stateless client using JavaScript framework - Angularjs.     

Use case for the application is inspired by [todomvc](http://todomvc.com/).

## Goal
Main goal of the project setup is to show how to be productive while having fun with Java EE platform and AngularJS framework.

## Prerequisites

## Use Cases

## How to Work with the Source

## Nice to have

### Geenral
- multi user support

### Java
- more unit tests
- add comments
- integration tests
- example for web sockets

### JavaScript
- more unit tests
- functional tests
- add comments
- refactor out JavaScript source to separate project
- enable dependency management using tools such as bower
- create build script with tools such as grunt

## Development

Project is maven based. Import it to the preferred IDE. 

### MySql setup

Crete todo database with todo user and grant users permissions. Database credentials are used in class 
```si.todoapp.todo.TodoItemDataSource```.

    CREATE DATABASE todo CHARACTER SET utf8 COLLATE utf8_general_ci;

    CREATE USER 'todo' IDENTIFIED BY 'todo';
    GRANT ALL ON todo.* TO 'todo'@'%' IDENTIFIED BY 'todo';
    GRANT ALL ON todo.* TO 'todo'@'localhost' IDENTIFIED BY 'todo';
    FLUSH PRIVILEGES;

### Tests

* run maven integration test

    mvn failsafe:integration-test

* create unit test reports 

    mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true org.jacoco:jacoco-maven-plugin:report

* create integration test report

    mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent-integration install failsafe:integration-test -Dmaven.test.failure.ignore=true org.jacoco:jacoco-maven-plugin:report-integration

* mutation testing

    mvn clean install org.pitest:pitest-maven:mutationCoverage

* load testing

   File create.read.todo-load.test.jmx in maven test resource folder contains [jmeter](http://jmeter.apache.org) test.  

* sonar

   TODO
