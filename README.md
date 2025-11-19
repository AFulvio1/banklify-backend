# Banklify Backend API 🏦

**Banklify** is a robust RESTful API designed for a modern fintech application. It serves as the core engine for a Digital Banking platform, managing user authentication, account details, and secure financial transactions.

Built with **Spring Boot 3**, it enforces strict security standards using **JWT (JSON Web Tokens)** and ensures data integrity with atomic transactions for money transfers.

---

## 🚀 Tech Stack

* **Language:** Java 21
* **Framework:** Spring Boot 3.5.6
* **Security:** Spring Security 6, JWT (Stateless Auth)
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA (Hibernate)
* **Documentation:** SpringDoc OpenAPI (Swagger UI)
* **Build Tool:** Maven
* **Utils:** Lombok, Jackson, Validation API, MapStruct

---

## 🏗 Architecture & Design Patterns

The project follows a clean **Layered Architecture** to ensure separation of concerns, testability, and maintainability:

1.  **Controller Layer (`/controller`):** Handles incoming HTTP requests, validates DTOs using `@Valid`, and returns standardized JSON responses.
2.  **Service Layer (`/service`):** Encapsulates business logic. It uses `@Transactional` to ensure **ACID properties** during critical operations like fund transfers.
3.  **Repository Layer (`/repository`):** Manages data persistence and database interactions via Spring Data JPA interfaces.
4.  **Security Layer:** Implements a custom filter chain to intercept requests and validate JWT tokens before they reach the controllers.

---

## ✨ Key Features

### 🔐 Security & Identity
* **User Registration:** Creation of new users with automatic IBAN assignment.
* **Stateless Authentication:** Secure login returning a **JWT Bearer Token**.
* **Password Hashing:** Uses `BCrypt` for storing passwords securely.
* **Role-Based Access:** scalable design for User/Admin roles.

### 💸 Banking Operations
* **Internal Transfers:** Send money to other users within the bank system using their IBAN.
* **Atomic Transactions:** Ensures that if a debit fails, the credit is rolled back to prevent data inconsistency.
* **Balance Check:** Real-time retrieval of available and ledger balance.

### 📜 History & Data
* **Transaction History:** Retrieve list of movements (incoming/outgoing) with timestamps.
* **Pagination:** APIs support pagination to handle large datasets efficiently.

---

## 🛠 Getting Started

### Prerequisites
* **Java JDK 21** or higher installed
* **Maven 3.6+** installed
* **Git**

### Installation Steps

1.  **Clone the repository**
    ```bash
    git clone https://github.com/AFulvio1/banklify-backend.git
    cd banklify-backend
    ```

2.  **Build the project**
    ```bash
    mvn clean install
    ```

3.  **Run the application**
    ```bash
    mvn spring-boot:run
    ```
    The server will start at `http://localhost:8080`.

---

## 📖 API Documentation (Swagger)

This project integrates **OpenAPI 3.0**. Once the application is running, you can explore, visualize, and test the API endpoints using the interactive Swagger UI.

👉 **Access Swagger UI:** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Core Endpoints Overview

| Method | Endpoint                              | Description | Access |
| :--- |:--------------------------------------| :--- | :--- |
| `POST` | `/api/v1/auth/register`               | Register a new user | Public |
| `POST` | `/api/v1/auth/login`                  | Authenticate and get Token | Public |
| `GET` | `/api/v1/accounts/balance`            | Get current user balance | 🔒 User |
| `POST` | `/api/v1/transactions/transfer`       | Execute a money transfer | 🔒 User |
| `GET` | `/api/v1/transactions/{iban}/movements` | Get transaction movements | 🔒 User |

---

## 🎓 Author

**Developed by:** Antonio Fulvio

* **University:** Università Telematica Pegaso
* **Degree Course:** Informatica per le Aziende Digitali (L-31)
* **Subject:** Project Work
* **Project Title:** "Banklify: Full Stack Digital Banking System"
* **Academic Year:** 2024/2025

---

*⚠️ **Disclaimer:** This application is a prototype developed for educational purposes. It simulates financial transactions using test data and is not intended for real-world banking usage.*