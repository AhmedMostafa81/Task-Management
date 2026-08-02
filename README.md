# 🎯 Full-Stack Task Management Application

A full-stack web application built with **Spring Boot** and **Angular** that allows users to register, log in, and seamlessly manage their personal tasks.

This project was developed as the final individual task for the Software Internship, demonstrating core concepts including Object-Oriented Programming (OOP), RESTful API design, stateless JWT security, Relational Database management, Docker containerization, and BDD testing.

---

## 📑 Table of Contents
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture Overview](#-architecture-overview)
- [Design Patterns](#-design-patterns)
- [External Libraries](#-external-libraries)
- [Setup Instructions](#-setup-instructions)
- [CI/CD Pipeline](#-cicd-pipeline)
- [Testing (BDD)](#-testing-bdd)

---

## ✅ Features
- **User Authentication:** Secure registration and login using stateless JWT (JSON Web Tokens).
- **Task Management:** Full CRUD (Create, Read, Update, Delete) capabilities for tasks.
- **Task Categorization:** Assign `Status` (TODO, IN_PROGRESS, DONE) and `Priority` (LOW, MEDIUM, HIGH) to tasks.
- **Advanced Filtering:** Filter the task list dynamically by status or priority.
- **Data Privacy:** Role-based isolation ensures users can only view, edit, and manage their own tasks.
- **Interactive UI Notifications:** Integrated SweetAlert2 for elegant success and error toast notifications.

---

## 🛠️ Tech Stack
- **Backend:** Java 25, Spring Boot 4.1.x, Spring Security, Spring Data JPA, Hibernate
- **Frontend:** Angular (Standalone Components), Reactive Forms, Custom CSS (Flexbox & Gradients)
- **Database:** PostgreSQL 16
- **Testing:** Cucumber (BDD), JUnit Platform
- **DevOps:** Docker, Docker Compose, Git, GitHub Actions
- **Build Tool:** Maven (Backend), npm (Frontend)

---

## 🏗️ Architecture Overview
The application follows a strict **N-Tier (Layered) Architecture** to ensure separation of concerns, scalability, and testability.

1. **Presentation Layer (Frontend):** Angular SPA (Single Page Application) utilizing modern Standalone Components. Communicates with the backend via HTTP REST calls. An `HttpInterceptor` automatically handles attaching JWT Bearer tokens to secured routes.
2. **Controller Layer (Backend):** Spring REST Controllers handle incoming HTTP requests, validate payloads, and route them to the appropriate business logic.
3. **Service Layer (Backend):** Contains the core business logic. It applies authorization rules (e.g., ensuring a user only fetches their own tasks using `@AuthenticationPrincipal`) and transforms Entities into DTOs.
4. **Data Access Layer (Backend):** Spring Data JPA Repositories interface with the PostgreSQL database using Hibernate as the ORM.

**Security Architecture:**
The application relies on **Stateless JWT Authentication**. Upon successful login, the server issues a cryptographically signed JWT. The Angular client stores this in `sessionStorage` and attaches it to the `Authorization` header of subsequent requests. A custom Spring Security filter intercepts requests to validate the token and populate the `SecurityContext`.

---

## 🧩 Design Patterns
The project utilizes several industry-standard design patterns to maintain clean code:

- **DTO (Data Transfer Object) Pattern:** Used extensively to prevent exposing raw database entities to the client, preventing over-posting attacks and decoupling the API contract from the database schema.
- **Repository Pattern:** Implemented via Spring Data JPA to abstract database interactions and queries behind simple interfaces.
- **Dependency Injection (DI) / Inversion of Control (IoC):** Managed by the Spring container and Angular's Injector to inject services and repositories, promoting loose coupling.
- **Builder Pattern:** Utilized via Lombok's `@Builder` annotation to instantiate complex objects cleanly without massive constructors.

---

## 📚 External Libraries
The following external libraries were used, specifically chosen to reduce boilerplate and align with modern industry standards:

- **Lombok (`org.projectlombok`):** Used to auto-generate Getters, Setters, Constructors, and Builders, keeping entity and DTO classes clean and readable.
- **JJWT (`io.jsonwebtoken`):** A secure, industry-standard library used in the backend for creating and parsing JSON Web Tokens.
- **Cucumber (`io.cucumber`):** Used to write human-readable BDD (Behavior-Driven Development) test scenarios in `.feature` files.
- **SweetAlert2:** Used in the Angular frontend to replace native browser alerts with highly customizable, non-blocking toast notifications.

---

## 🚀 Setup Instructions

### Prerequisites
- Docker & Docker Compose
- Java 25 & Maven (for local backend development)
- Node.js & Angular CLI (for local frontend development)

### 🐳 Using Docker (Recommended)
You can spin up the PostgreSQL database and backend API using the provided `docker-compose.yml`.

1. Clone the repository:
   ```bash
   git clone <YOUR_REPO_URL>
   cd <YOUR_REPO_FOLDER>
   ```

2. Start the database using Docker Compose:
   ```bash
   docker-compose up -d
   ```
   *(This starts a Postgres 16 instance on port 5432 with User: `ahmed` and Password: `1234`)*

3. Run the Spring Boot Backend:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   *(The backend API will be available at http://localhost:8080)*

4. Run the Angular Frontend (in a separate terminal):
   ```bash
   cd frontend
   npm install
   ng serve
   ```
   *(Access the web app at http://localhost:4200)*

---

## 🔄 CI/CD Pipeline
This repository utilizes GitHub Actions for Continuous Integration.
On every push or pull request, the pipeline automatically:

1. Checks out the source code.
2. Sets up the Java environment.
3. Builds the project using Maven (`mvn clean install`).
4. Executes all Unit and Cucumber BDD tests to ensure code reliability and prevent regressions before merging.

---

## 🧪 Testing (BDD)
Behavior-Driven Development (BDD) was practiced using Cucumber.
Features were written in standard Gherkin syntax (`.feature` files) before implementation to guide development (Test-Driven Development). Scenarios cover:

- User Registration & Login (Success / Failure)
- Task Creation & Validation
- Task Retrieval & Filtering by Status/Priority
- Role-based task isolation (Users accessing only their data)

To view the BDD tests, navigate to `src/test/resources/features/` in the backend module.
