# Task Management System

A RESTful API for task management built with Spring Boot 4.0.2 and Java 17.

## Prerequisites

- **Java 17** or higher
- **Maven 3.6+** (or use the included Maven wrapper)

## Setup & Running

1. **Clone the repository**

   ```bash
   git clone <repository-url>
   cd TaskManagementSystem
   ```

2. **Build the project**

   ```bash
   ./mvnw clean install
   ```

   On Windows:

   ```bash
   mvnw.cmd clean install
   ```

3. **Run the application**

   ```bash
   ./mvnw spring-boot:run
   ```

   The API will start on `http://localhost:8080`.

4. **Run tests**

   ```bash
   ./mvnw test
   ```

## Accessing the Application

| Resource          | URL                                  |
|-------------------|--------------------------------------|
| API Base          | `http://localhost:8080/api/tasks`     |
| Swagger UI        | `http://localhost:8080/swagger-ui.html` |
| OpenAPI Docs      | `http://localhost:8080/api-docs`      |
| H2 Database Console | `http://localhost:8080/h2-console`  |

**H2 Console credentials:** JDBC URL: `jdbc:h2:mem:taskdb`, Username: `sa`, Password: *(empty)*

## API Endpoints

| Method   | Endpoint                  | Description              |
|----------|---------------------------|--------------------------|
| `GET`    | `/api/tasks`              | List tasks (with filtering, sorting, pagination) |
| `GET`    | `/api/tasks/{id}`         | Get a task by ID         |
| `POST`   | `/api/tasks`              | Create a new task        |
| `PUT`    | `/api/tasks/{id}`         | Update a task            |
| `PATCH`  | `/api/tasks/{id}/toggle`  | Toggle task completion   |
| `DELETE` | `/api/tasks/{id}`         | Delete a task            |

### Query Parameters for `GET /api/tasks`

| Parameter    | Type     | Default     | Description                        |
|-------------|----------|-------------|------------------------------------|
| `completed` | Boolean  | —           | Filter by completion status        |
| `search`    | String   | —           | Search by title (case-insensitive) |
| `assignedTo`| String   | —           | Filter by assignee                 |
| `dueDateFrom` | DateTime | —         | Filter from due date               |
| `dueDateTo` | DateTime | —           | Filter to due date                 |
| `page`      | int      | 1           | Page number                        |
| `size`      | int      | 10          | Items per page                     |
| `sortBy`    | String   | `createdAt` | Field to sort by                   |
| `sortDir`   | String   | `desc`      | Sort direction (`asc` or `desc`)   |

## Architecture & Design Decisions

### Layered Architecture

The application follows a clean **Controller -> Service -> Repository** layered architecture with clear separation of concerns:

- **Controller layer** handles HTTP requests/responses and input validation.
- **Service layer** (interface + implementation) encapsulates all business logic and transaction management.
- **Repository layer** uses Spring Data JPA for database access.

### Key Design Choices

- **UUID primary keys** instead of auto-increment integers — better suited for distributed systems and avoids exposing record counts.
- **DTO pattern** (TaskRequest / TaskResponse / TaskFilterRequest) — decouples the API contract from the internal domain model, allowing them to evolve independently.
- **JPA Specification pattern** — enables dynamic query composition for filtering without writing custom SQL or creating multiple repository methods.
- **H2 in-memory database** — chosen for simplicity and zero-configuration setup. Can be swapped to MySQL, PostgreSQL, or any JPA-compatible database by changing `application.properties`.
- **Rate limiting** (Bucket4j) — 20 requests per minute per IP address on all `/api/**` endpoints to prevent abuse.
- **Global exception handling** — centralized error responses with consistent structure (timestamp, status, error, message).
- **CORS pre-configured** for `localhost:3000` (React) and `localhost:4200` (Angular) to support frontend development.

### Assumptions & Trade-offs

- The H2 in-memory database resets on every restart. This simplifies development but means data is not persisted across runs.
- The `DataSeeder` seeds 8 sample tasks on startup when the database is empty, providing immediate test data.
- DDL auto-update mode (`spring.jpa.hibernate.ddl-auto=update`) is used for convenience; a production deployment would use migration tools like Flyway or Liquibase.
- Rate limiting is per-IP and in-memory — in a multi-instance deployment, a distributed rate limiter (e.g., Redis-backed) would be needed.

## AI Disclosure

Some of the **unit/integration tests**, the **data seeder**, and this **README** were generated using AI to save development time.
## Tech Stack

- **Spring Boot 4.0.2** — Application framework
- **Spring Data JPA** — Database access
- **H2 Database** — In-memory relational database
- **Bucket4j** — Rate limiting
- **SpringDoc OpenAPI** — API documentation (Swagger UI)
- **Lombok** — Boilerplate reduction
- **JUnit 5 / Mockito / MockMvc** — Testing
