# Online Bookstore API

## Overview

The **Online Bookstore API** is a Spring Boot–based web application that provides endpoints for user registration, login, book catalog browsing, cart management, and order checkout. It is developed using **Java 17**, relies on **Gradle** as the build system, and uses **OpenAPI 3.1** with **Swagger UI** for interactive API documentation. The project follows a **Test-Driven Development (TDD)** approach to ensure high code quality, maintainability, and reliability.

## Project Goals

- **Provide REST APIs** for core bookstore operations (catalog, cart, orders, authentication).
- **Expose the API via Swagger** for easy testing and exploration.
- **Ensure reliability** through TDD and comprehensive integration tests.

## Features

- **User Registration & Login**  
  Secure endpoints for creating accounts and authenticating users with JWT.

- **Book Catalog**  
  Browse available books with details such as title, author, and price.

- **Shopping Cart**  
  Add, update, and remove items from a user’s cart with stock validation.

- **Order Checkout**  
  Place orders, validate stock, create order items, and clear the cart.

- **OpenAPI Documentation**  
  Interactive API docs available via Swagger UI and a static `openapi.yml`.

- **Global Error Handling**  
  Standardized error responses using DTOs and exception handlers.

## Technologies Used

- **Java 17**
- **Spring Boot 4.1.1**
- **Gradle 8.14**
- **Spring Security (JWT authentication)**
- **springdoc-openapi-starter-webmvc-ui** for Swagger/OpenAPI docs
- **H2 Database** for development and testing

## Running the Application

**Prerequisites**
- Java 17+
- Gradle 8.14+
- IDE (IntelliJ IDEA / Eclipse) or command line

**Clone the repository:**

```bash
git clone https://github.com/<your-username>/online-bookstore.git
cd online-bookstore
```

- **Build the project with Gradle:**
  ./gradlew clean build
- **Run the Application:**
  ./gradlew bootRun
  
The application runs on port 8080 by default. 

You can access it at:

- API root: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html#/
- OpenAPI spec: http://localhost:8080/v3/api-docs
- Actuator health: http://localhost:8080/actuator/health

## Testing
Run all unit tests:
```bash
./gradlew clean test
```

## Authorization Details

The Online Bookstore API uses JWT-based authentication to secure endpoints.

- **Public Endpoints:**

           /api/auth/register → Register a new user
           /api/auth/login → Authenticate and receive a JWT token

- **Secured Endpoints**  
       Require a valid JWT token in the Authorization header:
       Authorization: Bearer <your-jwt-token>
 
       Examples:
                   /api/books/** → Book catalog operations
                   /api/carts/** → Cart management
                   /api/orders/** → Order checkout and tracking

- **Swagger UI Integration**  
Swagger UI provides an Authorize button where you can paste your JWT token. 
Once authorized, all secured endpoints can be tested directly from the UI.

- **Token Expiry**  
JWT tokens have an expiration time. 
If expired, you must log in again to obtain a new token.

## API Endpoints

| Endpoint             | Method | Description                  |
|----------------------|--------|------------------------------|
| /api/auth/register   | POST   | Register a new user          |
| /api/auth/login      | POST   | Authenticate and get JWT     |
| /api/books           | GET    | Browse available books       |
| /api/carts           | GET    | View user cart               |
| /api/carts           | POST   | Add item to cart             |
| /api/orders          | POST   | Place an order               |

## API Response Codes

The Online Bookstore API follows standard HTTP status codes for responses:

- **200 OK**  
  Request was successful.  
  Example: Book list retrieved, cart contents returned.

- **201 Created**  
  Resource successfully created.  
  Example: User registered, order placed.

- **400 Bad Request**  
  The request was invalid or missing required parameters.  
  Example: Invalid book ID, malformed JSON body.

- **401 Unauthorized**  
  Authentication failed or JWT token missing/invalid.  
  Example: Accessing secured endpoints without a valid token.

- **403 Forbidden**  
  User does not have permission to access the resource.  
  Example: Future enhancement with role-based access control.

- **404 Not Found**  
  Requested resource could not be found.  
  Example: Book ID not present in catalog.

- **409 Conflict**  
  Request could not be completed due to a conflict.  
  Example: Attempting to place an order when stock is insufficient.

- **500 Internal Server Error**  
  Unexpected error occurred on the server.  
  Example: Database failure, unhandled exception.

## Future Enhancements for Online Bookstore

- **Database Upgrade**  
  Replace H2 with MySQL for persistence and scalability.

- **Role-Based Access Control**  
  Extend Spring Security to support roles like admin and customer.

- **Order Tracking**  
  Implement order status updates (`SHIPPED`, `DELIVERED`, `CANCELLED`) with notifications.

- **CI/CD & OpenAPI Publishing**  
  Automate builds, tests, and publish OpenAPI docs via pipelines.

- **Microservices Architecture**  
  Split into independent services (auth, catalog, cart, orders) for scalability.

- **React Frontend Interface**  
  Develop a React-based UI to consume the Online Bookstore API.

- **Integration Testing with Karate**  
  Add end-to-end integration tests using Karate to validate API flows.

- **Performance Testing with Gatling (Scala)**  
  Simulate API performance under load.

- **Security Scanning (Wiz / CVE Monitoring)**  
  Integrate vulnerability scanning tools (e.g., Wiz,Veracode etc) to detect security issues proactively.


## Contributing

Contributions are welcome!
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/xyz`)
3. Commit changes (`git commit -m "feat: add xyz"`)
4. Push to branch and open a Pull Request

## Contact

Maintainer: Nivedha Prakash  
Email: <nive16071997@gmail.com>  
