# User Management API

This repository contains a Spring Boot Application, that implements a REST API for managing users. The API provides CRUD operations (Create, Read, Update, Delete) for users, allowing you to manage user information effectively.

## Features

- Create a new user by providing a username and email (mandatory) along with an optional firstname and lastname.
- Retrieve user information by their unique identifier.
- Update user information, including username, email, firstname, and lastname.
- Delete a user by their identifier.
- Perform batch operations to create, update, or delete multiple users simultaneously.

## Technologies Used

- Java JDK17
- Spring Boot 3.0.0
- Spring Data JPA
- RESTful API
- H2 Database

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Apache Maven 3.6.3

### Installation

1. Clone the repository:

`https://github.com/KostasKats/userCRUDAssessment.git`

2. Build the project using Maven:

```shell
mvn clean install
```

3. Run the API:

```shell
 java -jar restful-web-services-0.0.1-SNAPSHOT.jar
 ```

The service will start running on the default port `8080`.

H2 console on: `http://localhost:8080/h2-console`

JDBC URL: `jdbc:h2:mem:testdb`

## API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui.html` when the service is running. You can explore and test the API endpoints using Swagger UI.






