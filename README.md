# Employee Leave Management System

A backend-based Employee Leave Management System built using Java and Spring Boot.
This project provides secure role-based leave management functionalities for Employees, Managers, and Admins using JWT Authentication and Spring Security.

---

# Features

## Authentication & Security

* JWT Authentication
* Spring Security Integration
* Role-Based Authorization
* BCrypt Password Encryption
* Stateless Authentication
* Ownership Validation

## Employee Features

* Employee Registration
* Login
* Apply Leave
* View Leave History
* Cancel Leave
* View Leave Balance

## Manager Features

* View Pending Leave Requests
* Approve Leave
* Reject Leave

## Admin Features

* Manage Employees
* Manage Leave Balances
* Full System Access

## Additional Features

* Swagger/OpenAPI Documentation
* Pagination & Sorting
* DTO Pattern
* Global Exception Handling
* Logging using SLF4J & Logback
* Validation Handling
* Layered Architecture

---

# Tech Stack

| Technology      | Used                           |
| --------------- | ------------------------------ |
| Java 17         | Backend Language               |
| Spring Boot     | Application Framework          |
| Spring Security | Authentication & Authorization |
| JWT             | Secure Token Authentication    |
| Spring Data JPA | Database Operations            |
| Hibernate       | ORM Framework                  |
| MySQL           | Database                       |
| Maven           | Dependency Management          |
| Swagger/OpenAPI | API Documentation              |
| Lombok          | Boilerplate Reduction          |
| ModelMapper     | DTO Mapping                    |
| Logback / SLF4J | Logging                        |

---

# Project Architecture

The project follows layered architecture:

Controller Layer
→ Handles API requests and responses

Service Layer
→ Contains business logic

Repository Layer
→ Handles database operations using JPA

DTO Layer
→ Used for request/response data transfer

Security Layer
→ JWT authentication and authorization

Exception Layer
→ Global exception handling

---

# Security Flow

1. User logs in using email and password
2. Spring Security authenticates credentials
3. JWT token is generated
4. Client sends JWT token in Authorization header
5. JWT filter validates token for every request
6. User role is verified
7. Access is granted based on role permissions

Authorization Header Format:

Bearer your_jwt_token

---

# Roles & Permissions

| Role     | Permissions                           |
| -------- | ------------------------------------- |
| EMPLOYEE | Apply leave, view leave, cancel leave |
| MANAGER  | Approve/reject leave requests         |
| ADMIN    | Full system access                    |

---

# Business Logic Implemented

* Prevent overlapping leave requests
* Leave balance validation
* Date validation
* Ownership validation
* Role-based API restrictions
* Leave status tracking

---

# API Documentation

Swagger UI available at:

http://localhost:8080/swagger-ui/index.html

#

---

# Database Entities

## Employee

* id
* name
* email
* password
* department
* joiningDate
* role

## LeaveRequest

* id
* leaveType
* startDate
* endDate
* reason
* status

## LeaveBalance

* id
* leaveType
* remainingLeaves

---

# Future Improvements

* Email Notifications
* Unit Testing
* Docker Support
* AWS Deployment
* Redis Caching
* CI/CD Pipeline
* Frontend Integration
* Refresh Tokens
* Microservices Architecture

---

# How To Run Project

1. Clone Repository

git clone https://github.com/Shivam-khatavkar/leave-management-system.git

2. Configure MySQL Database

Update application.properties with your MySQL credentials.

3. Run Project

mvn spring-boot

4. Open Swagger

http://localhost:8080/swagger-ui/index.html

---

# Author

Shivam Khatavkar

GitHub:
https://github.com/Shivam-khatavkar
