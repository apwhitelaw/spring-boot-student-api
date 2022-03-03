# Spring Boot Student API
Basic student web api using:
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* JUnit 5
* Mockito

This project is a complete api with tests to verify functionality.

## Student API
The api is accessed using the `api/v1.0/student` endpoint. 
From there, you have the following options.
* GET `/` - returns all students
* GET `/{id}` - returns one student of `id`, if exists
* POST `/` - register new student (using request body)
* PUT `/{id}` - update student of `id` (using request param)
* DELETE `/{id}` - delete student of `id`