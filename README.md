# 🏥 MedCare Backend

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.0-green?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-Auth-purple?style=for-the-badge&logo=jsonwebtokens)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

**A robust healthcare management backend system built with Java and Spring Boot**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Getting Started](#-getting-started) • [API Documentation](#-api-documentation) • [Contributing](#-contributing)

</div>

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [API Documentation](#-api-documentation)
- [Project Structure](#-project-structure)
- [Security](#-security)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

---

## 🌟 Overview

**MedCare Backend** is a comprehensive healthcare management system designed to streamline medical operations. It provides secure, scalable, and efficient APIs for managing patients, appointments, and medical staff with enterprise-grade security using JWT authentication and role-based access control.

---

## ✨ Features

### Core Functionality

- 🧑‍⚕️ **Patient Management** – Complete CRUD operations for patient records  
- 📅 **Appointment Scheduling** – Efficient appointment booking and management  
- 👨‍⚕️ **Staff Management** – Comprehensive medical staff administration  
- 🔐 **Security** – JWT-based authentication with role-based access control  
- 🏗️ **RESTful API** – Clean, intuitive API design for seamless frontend integration  
- 📊 **Database Integration** – Robust data persistence with MySQL  
- 🔄 **Data Validation** – Comprehensive input validation and error handling  

### Additional Features

- 📝 Audit logging for all critical operations  
- 🚦 Rate limiting for API protection  
- 📧 Email notifications (configurable)  
- 🔍 Advanced search and filtering capabilities  

---

## 🛠️ Tech Stack

### Backend

- **Language:** Java 17  
- **Framework:** Spring Boot 3.x  
- **Security:** Spring Security + JWT  
- **Database:** MySQL 8.0  
- **ORM:** JPA / Hibernate  
- **Build Tool:** Maven 3.6+  

### Development Tools

- **API Testing:** Postman / Swagger  
- **IDE:** IntelliJ IDEA / Eclipse  
- **Version Control:** Git  

---

## 📋 Prerequisites

Ensure the following are installed:

- ☕ Java Development Kit (JDK) 17+  
- 📦 Apache Maven 3.6+  
- 🗄️ MySQL 8.0+  
- 🔧 Git  
- 📝 IDE (IntelliJ IDEA recommended)  

---

## 🚀 Installation

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ebrahimAlaaeldin/MedCare-Backend.git
cd MedCare-Backend
```

### 2️⃣ Set Up Database

```sql
CREATE DATABASE medcare_db;
USE medcare_db;
```

### 3️⃣ Configure Application Properties

Create `application.properties` in `src/main/resources/`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/medcare_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# JWT Configuration
app.jwt.secret=your_super_secret_key_here
app.jwt.expiration=86400000

# Server Configuration
server.port=8080
server.error.include-message=always
```

### 4️⃣ Build and Run

```bash
mvn clean install
mvn spring-boot:run
```

Application runs at: `http://localhost:8080`

---

## ⚙️ Configuration

### Environment Variables (Production)

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=medcare_db
export DB_USERNAME=your_username
export DB_PASSWORD=your_password
export JWT_SECRET=your_secret_key
export JWT_EXPIRATION=86400000
```

### Application Profiles

- Development: `application-dev.properties`  
- Production: `application-prod.properties`  
- Testing: `application-test.properties`  

Run with profile:

```bash
mvn spring-boot:run -Dspring.profiles.active=dev
```

---

## 📡 API Documentation

### 🔐 Authentication Endpoints

| Method | Endpoint           | Description              | Auth Required |
|--------|--------------------|--------------------------|---------------|
| POST   | /api/auth/register | Register a new user      | No            |
| POST   | /api/auth/login    | Authenticate user        | No            |
| POST   | /api/auth/refresh  | Refresh JWT token        | Yes           |
| POST   | /api/auth/logout   | Logout user              | Yes           |

### 👥 Patient Management

| Method | Endpoint              | Description            | Auth Required |
|--------|-----------------------|------------------------|---------------|
| GET    | /api/patients         | Get all patients       | Yes           |
| GET    | /api/patients/{id}    | Get patient by ID      | Yes           |
| POST   | /api/patients         | Create new patient     | Yes           |
| PUT    | /api/patients/{id}    | Update patient         | Yes           |
| DELETE | /api/patients/{id}    | Delete patient         | Yes           |
| GET    | /api/patients/search  | Search patients        | Yes           |

### 📅 Appointment Management

| Method | Endpoint                      | Description           | Auth Required |
|--------|-------------------------------|-----------------------|---------------|
| GET    | /api/appointments             | Get all appointments  | Yes           |
| GET    | /api/appointments/{id}        | Get appointment by ID | Yes           |
| POST   | /api/appointments             | Schedule appointment  | Yes           |
| PUT    | /api/appointments/{id}        | Update appointment    | Yes           |
| DELETE | /api/appointments/{id}        | Cancel appointment    | Yes           |
| GET    | /api/appointments/available   | Get available slots   | Yes           |

### 👨‍⚕️ Staff Management

| Method | Endpoint              | Description         | Auth Required |
|--------|-----------------------|---------------------|---------------|
| GET    | /api/staff            | Get all staff       | Yes           |
| GET    | /api/staff/{id}       | Get staff by ID     | Yes           |
| POST   | /api/staff            | Add new staff       | Yes (Admin)   |
| PUT    | /api/staff/{id}       | Update staff info   | Yes (Admin)   |
| DELETE | /api/staff/{id}       | Remove staff        | Yes (Admin)   |

### 🔄 Example: Login Request/Response

**POST /api/auth/login**

```json
{
  "username": "john.doe@medcare.com",
  "password": "securePassword123"
}
```

**Response**

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000,
  "user": {
    "id": 1,
    "username": "john.doe@medcare.com",
    "roles": ["ROLE_USER", "ROLE_ADMIN"]
  }
}
```

---

## 📁 Project Structure

```
MedCare-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/medcare/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── security/
│   │   │       ├── service/
│   │   │       └── util/
│   │   └── resources/
│   │       ├── application.properties
│   │       └── data.sql
├── pom.xml
└── README.md
```

---

## 🔒 Security

### Security Features

- JWT-based stateless authentication  
- BCrypt password encryption  
- Role-based access control (RBAC)  
- CORS configuration  
- Request validation and sanitization  
- Token refresh mechanism  
- Token expiration handling  

### Best Practices

- Use HTTPS in production  
- Keep dependencies updated  
- Use strong JWT secrets  
- Enable rate limiting  
- Enable audit logging  
- Regularly audit for vulnerabilities  

---

## 🧪 Testing

### Run Tests

```bash
# All tests
mvn test

# Specific test
mvn test -Dtest=PatientServiceTest

# With coverage report
mvn test jacoco:report
```

### Coverage

- Unit tests (services)  
- Integration tests (controllers)  
- Repository tests  
- Security configuration tests  

---

## 🤝 Contributing

We welcome contributions! Please follow the steps below:

1. Fork the repository  
2. Create a feature branch  
```bash
git checkout -b feature/AmazingFeature
```
3. Commit your changes  
```bash
git commit -m 'Add some AmazingFeature'
```
4. Push to GitHub  
```bash
git push origin feature/AmazingFeature
```
5. Open a Pull Request  

### Guidelines

- Follow Java conventions  
- Write clear commit messages  
- Add tests for new features  
- Update documentation  
- Ensure all tests pass  

---

## 🔗 Project Links

- 📂 Repository: [MedCare-Backend](https://github.com/ebrahimAlaaeldin/MedCare-Backend)  
- 🐛 Issues: [Report Bug](https://github.com/ebrahimAlaaeldin/MedCare-Backend/issues)
