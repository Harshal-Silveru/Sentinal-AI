# AI-DevSecOps Microservices

A Spring Boot based microservices application for managing users, projects, security scans, AI-based security analysis, recommendations, and security reports.

## Architecture

The application consists of four independent microservices:

| Service                             | Port | Database   | Responsibility                    |
| ----------------------------------- | ---: | ---------- | --------------------------------- |
| User Service                        | 8081 | user_db    | User management                   |
| Project & Security Scanning Service | 8082 | project_db | Project and scan management       |
| AI Analysis Service                 | 8083 | ai_db      | Risk analysis and recommendations |
| Reporting Service                   | 8084 | report_db  | Security report management        |

## Technology Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Maven
* REST APIs
* Swagger / OpenAPI
* IntelliJ IDEA
* Postman

## Microservice Communication

The services communicate using REST APIs.

```text
User Service :8081
       ↑
       │ REST
       │
Project Service :8082
       ↑
       │ REST
       │
AI Analysis Service :8083
       ↑
       │ REST
       │
Reporting Service :8084
```

### Communication Flow

1. Project Service verifies users through User Service.
2. AI Analysis Service verifies scans through Project Service.
3. Reporting Service retrieves analysis information through AI Analysis Service.

Each service maintains its own PostgreSQL database.

## Main Features

### User Service

* Create user
* Retrieve users
* Retrieve user by ID
* Update user
* Delete user

### Project & Security Scanning Service

* Create project
* Retrieve projects
* Retrieve project by ID
* Retrieve projects by user
* Delete project
* Create security scan
* Retrieve scans
* Retrieve scan by ID
* Retrieve scans by project

### AI Analysis Service

* Create security analysis
* Retrieve analyses
* Retrieve analysis by ID
* Retrieve analyses by scan
* Delete analysis
* Create security recommendations
* Retrieve recommendations
* Retrieve recommendation by analysis
* Delete recommendation
* Calculate risk score
* Generate security explanations

### Reporting Service

* Generate security reports
* Retrieve reports
* Retrieve report by ID
* Retrieve reports by analysis
* Delete reports
* Generate report summaries

## Databases

Database-per-service architecture is used:

```text
User Service       → user_db
Project Service    → project_db
AI Analysis        → ai_db
Reporting Service  → report_db
```

Services do not directly access another service's database.

## Running the Application

Start PostgreSQL and create the following databases:

```text
user_db
project_db
ai_db
report_db
```

Configure the PostgreSQL username and password in each service's `application.properties`.

Then start the services:

```text
User Service       → http://localhost:8081
Project Service    → http://localhost:8082
AI Analysis        → http://localhost:8083
Reporting Service  → http://localhost:8084
```

## Swagger Documentation

Swagger UI is available at:

```text
http://localhost:8081/swagger-ui/index.html
http://localhost:8082/swagger-ui/index.html
http://localhost:8083/swagger-ui/index.html
http://localhost:8084/swagger-ui/index.html
```

## Example End-to-End Flow

```text
1. Create User
       ↓
2. Create Project
       ↓
3. Create Security Scan
       ↓
4. Create AI Analysis
       ↓
5. Generate Recommendation
       ↓
6. Generate Security Report
```

## Testing

REST APIs can be tested using Postman or Swagger UI.

The complete flow demonstrates REST-based communication between independent microservices and database-per-service architecture.

## Project Status

Phase 2 microservice implementation completed with four Spring Boot services, PostgreSQL persistence, REST communication, and Swagger API documentation.
