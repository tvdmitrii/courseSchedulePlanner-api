# Course Schedule Planner

This project was originally developed as an individual academic project and designed and implemented end-to-end by me. It showcases hands-on experience with Java-based web application development, including REST API design, unit testing, database modeling, and Hibernate object–relational mapping.

## Structure
This repository contains the backend REST API and database access logic. [Course Schedule Planner WebApp](https://github.com/tvdmitrii/courseSchedulePlanner-webApp) contains the WebApp that consumes the REST API. [Course Schedule Planner API Interface](https://github.com/tvdmitrii/courseSchedulePlanner-interface) contains the DTO classes and resource interfaces for REST API.

## Problem Statement

When it comes to scheduling, I believe that an image is worth a thousand words. My goal is to create a web application that would greatly simplify course scheduling. The user simply chooses the courses they would like to enroll into, the application loads the information about all the available class meeting times for these courses, and generates a visual representations for all the possible schedules. Instead of piecing together a dozen of "TuTh 10:30am-12:00pm" and "MW 2:00pm-4:00pm" the user just goes through the schedules one by one and chooses the one they like.

## Project Technologies/Techniques 

* Platform
  * JDK 21 
  * Jakarta EE 10
  * Jakarta Servlet 6.0
  * JSTL 3.0
* REST API
  * Jersey 3.1
* ORM Framework
  * Hibernate Version 6.4
* Database
  * MySQL 8.0
* Dependency Management
  * Maven
* Security/Authentication
  * AWS Cognito
* CSS 
  * Bootstrap
* Logging
  * Log4J2
* Hosting
  * AWS Elastic Beanstalk
* Unit Testing
  * JUnit 5 tests to cover all testable logic
* IDE: IntelliJ IDEA

## Design

- [Database design](DesignDocuments/databaseDesign.png).
- [User stories](DesignDocuments/userStories.md) outlining the project capabilities.
- [Application Screen Mockups](DesignDocuments/screens.md).

## Demo

A short demo and high-level walkthrough of the project is available here: [YouTube](https://youtu.be/JCVENmpXvnI).

## Development Progression
- [Project plan](projectPlan.md) outlining a rough project execution plan.
- [Development log](developmentLog.md).
