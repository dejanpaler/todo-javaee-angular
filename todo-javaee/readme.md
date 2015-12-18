Todo Application based on Java EE and AngularJS
===============================================
Todo application is a web based todo list manager. It helps users to create new todos, 
view entered todos, update specific todo and delete selected todo. 

Server side is based on Java EE platform. Client side laverages AngularJS framework. 
It exposes business logic through the REST API which are consumed by stateless client using JavaScript framework - Angularjs.     

Use case for the application is inspired by [todomvc](http://todomvc.com/).

## Goal
Main goal of the project setup is to show how to be productive while having fun with Java EE platform and AngularJS framework.

## Development
Project is maven based. Import it to the preferred IDE. 

### Testing
Running integration test:
`mvn failsafe:integration-test`

Create unit test reports: 
`mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true org.jacoco:jacoco-maven-plugin:report`

Create integration test report:
`mvn clean org.jacoco:jacoco-maven-plugin:prepare-agent-integration install failsafe:integration-test -Dmaven.test.failure.ignore=true org.jacoco:jacoco-maven-plugin:report-integration`

Mutation testing:
`mvn org.pitest:pitest-maven:mutationCoverage`

Load testing:
File `create.read.todo-load.test.jmx` in maven test resource folder contains [jmeter](http://jmeter.apache.org) test.  

### Analysing source code
#### SonarQube
Create reports for unit tests and integration tests:
```
    mvn -T 1C clean
    
    mvn -T 1C org.jacoco:jacoco-maven-plugin:prepare-agent install -Dmaven.test.failure.ignore=true org.jacoco:jacoco-maven-plugin:report
    
    mvn -T 1C org.jacoco:jacoco-maven-plugin:prepare-agent-integration failsafe:integration-test install -Dmaven.test.failure.ignore=true org.jacoco:jacoco-maven-plugin:report-integration
```

Analyse the project:
```
mvn -T 1C sonar:sonar -Dsonar.host.url=http://localhost:9000
```

### Continuous deployment
The continuous deployment is enabled with [Docker](https://www.docker.com) and [Gradle](http://gradle.org). 

Running continuous deployment: 
`gradle server -t`
