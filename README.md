# Car Rental Spring REST API

## Overview
### This repository contains prototype of a simple Car Rental REST API using Spring Framework. 
This API has a set of endpoints that allows to manage simple Car Rental, register and login new users
and make multiple reservation for cars that are available for rental.

## Key Features
### General:
   - CRUD operation for each entity using JpaRepository interface
   - Controller Advice for handing exceptions
   - Test and Prod profiles
   - H2 and MySQL databases
   - Junit Tests for Controller, Service and Repository layers
   - Open API 3.0 (Swagger) for endpoints testing

### Security Configuration:
   - Spring Security Basic HTTP type
   - Database Authentication

### Data Layer Configuration:
   - schema.sql file for managing and defining tables
   - data.sql file for data population
   - Custom Query methods
   - DTO patterns for selected entities
   - Table per class Jpa Inheritance strategy for Client and Employee entities

### Profiles:
   - Test profile
      * disables Spring Security for tests
      * uses H2 in memory database
      * uses Hibernate to define schemas for db tables
      * populates database by executing scripts from data.sql file using JdbcTemplate
      * has additional properties for easier testing and debugging

   - Prod profile
       * enables Spring Security
       * uses MySQL database
       * all schemas are manually defined in schema.sql file
       * data populated from data.sql file
     
## Application Logic Description
There are three levels of Car Rental management: Admin, Manager, Employee.

Client can make multiple reservations for multiple cars.

In order to successfully make reservation for a car there has to be no time collisions, car needs to have status set to AVAILABLE and be assigned to selected rent branch. 
 
## Technologies Used
<div align="center">
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192107854-765620d7-f909-4953-a6da-36e1ef69eea6.png" alt="HTTP" title="HTTP"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192107858-fe19f043-c502-4009-8c47-476fc89718ad.png" alt="REST" title="REST"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108374-8da61ba1-99ec-41d7-80b8-fb2f7c0a4948.png" alt="GitHub" title="GitHub"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192108890-200809d1-439c-4e23-90d3-b090cf9a4eea.png" alt="IntelliJ" title="IntelliJ"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/192109061-e138ca71-337c-4019-8d42-4792fdaa7128.png" alt="Postman" title="Postman"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/186711335-a3729606-5a78-4496-9a36-06efcc74f800.png" alt="Swagger" title="Swagger"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201156-9a724800-adec-11eb-9a9d-3cd0f67da4bc.png" alt="Java" title="Java"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117201470-f6d56780-adec-11eb-8f7c-e70e376cfd07.png" alt="Spring" title="Spring"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183891303-41f257f8-6b3d-487c-aa56-c497b880d0fb.png" alt="Spring Boot" title="Spring Boot"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207242-07d5a700-adf4-11eb-975e-be04e62b984b.png" alt="Maven" title="Maven"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117207493-49665200-adf4-11eb-808e-a9c0fcc2a0a0.png" alt="Hibernate" title="Hibernate"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/117533873-484d4480-afef-11eb-9fad-67c8605e3592.png" alt="JUnit" title="JUnit"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183892181-ad32b69e-3603-418c-b8e7-99e976c2a784.png" alt="mocikto" title="mocikto"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/190229463-87fa862f-ccf0-48da-8023-940d287df610.png" alt="Lombok" title="Lombok"/></code>
	<code><img width="50" src="https://user-images.githubusercontent.com/25181517/183896128-ec99105a-ec1a-4d85-b08b-1aa1620b2046.png" alt="MySQL" title="MySQL"/></code>
</div>

## Contribution
### Contributions are welcome! Feel free to open issues, submit pull requests, or suggest improvements.
