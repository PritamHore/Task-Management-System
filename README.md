# Task Management System
**Created & Maintained by:** Pritam Hore

This repo holds a Task Management System I built using Spring Boot. It's a standard REST API to handle creating, updating, and fetching tasks. It's running on Java 21 and Spring Boot 4.1.0, built with Maven, uses PostgreSQL for the database, and the whole setup is containerized with Docker to make life easier.

---

## 1. Getting It Running

### What you need installed
* Java 21
* Maven 3.9+
* Docker & Docker Compose

### The Easy Way (Docker Compose)
This is the recommended way. It spins up both the Spring Boot app and the Postgres database in their own separate containers.

1. Open your terminal in the root folder (where the `docker-compose.yml` lives).
2. Run this to build and start everything:
   `docker compose up -d --build`
3. The app will boot up on `http://localhost:10001`.
4. If you want to check the logs to see what's happening, run:
   `docker compose logs -f app`
5. When you're done, tear it down with:
   `docker compose down`

### The Dev Way (Local IDE + Docker DB)
If you want to run the Spring app in IntelliJ or Eclipse so you can easily step through code and debug, you can just use Docker for the database.

1. Start just the Postgres container:
   `docker compose up -d db`
2. Run the Spring Boot app directly from your IDE (or use `mvn spring-boot:run`).
3. The app is already configured with local defaults, so it'll automatically fall back and connect to `jdbc:postgresql://localhost:5432/task-management-db`.

---

## 2. API Endpoints

I've attached a PDF named "Task Management System API Documentation.pdf" with the full spec, but here’s the TL;DR. Everything exchanges data in JSON format.

### Quick Overview
* **POST /api/v1/task**: Creates a new task. (Returns 201 Created)
* **PUT /api/v1/task**: Updates the details of a task. (Returns 200 OK)
* **GET /api/v1/task?id={UUID}**: Fetches a single task by its UUID. (Returns 200 OK)
* **GET /api/v1/task/get-all**: Grabs all the tasks in the system. (Returns 200 OK)
* **PATCH /api/v1/task?id={UUID}**: Just updates a task's status. (Returns 200 OK)

### Status Codes You Might See
* **200**: All good.
* **201**: Successfully created the record.
* **400**: Bad request (usually validation failing).
* **404**: Couldn't find the task.

### Testing with Swagger
Once the app is running, you don't even need Postman. Just hit up `http://localhost:10001/swagger-ui.html` and you can test all the endpoints interactively right from the browser.

---

## 3. How It's Put Together (Architecture)

It is a standard Spring Boot multi-tier architecture.

### The Pieces
* **Client**: Postman, Swagger UI, or whatever front-end calls the API.
* **Controllers**: The REST endpoints (`/api/v1/task`). They take the incoming requests, validate the JSON payloads, and pass things down to the service layer.
* **Services**: Where the actual business logic lives (processing creation, updating statuses, handling task dependencies, etc.).
* **Global Exception Handler**: I threw in a `@RestControllerAdvice` component so we aren't duplicating try/catch blocks everywhere. If you throw a custom exception from anywhere in the app, this catches it and sends back a clean, standardized error response.
* **Repositories (Data Access)**: Standard Spring Data JPA interfaces hooking into Hibernate to map our Java objects to database rows.
* **Database**: `PostgreSQL 16` running neatly inside its own Docker container.

### How a Request Flows
1. A request comes in on port `10001`.
2. The Controller picks it up and parses the JSON/parameters.
3. It hands off the operation to the Service layer.
4. The Service does its checks (business rules) and tells the Repository what it needs.
5. Spring Data JPA translates that into SQL and hits the Postgres container running on port `5432` inside the Docker network.
6. The data comes back, gets mapped to a DTO (Data Transfer Object), and gets shipped back to the client as a JSON response.

## 4. Explanation of Design Decisions and Assumptions

### Design Decisions
* **Multi-Stage Dockerfile**: I went with a two-stage build here. Stage 1 uses Maven to compile the code, and Stage 2 just grabs the final `.jar` and drops it into a stripped-down Alpine JRE image. It keeps the final image way smaller since we don't need to ship the build tools, and it makes things a bit more secure.
* **Alpine Images**: Used Alpine Linux base images for both Java and Postgres. It just keeps everything lightweight so we aren't wasting disk space or waiting forever for images to pull.
* **UUIDs for Primary Keys**: Decided to use UUIDs for task IDs instead of the usual auto-incrementing integers. It's a good habit to get into so we aren't exposing our database row counts in the URLs, and it saves a lot of headaches if we ever need to migrate or merge data later.
* **Config Overrides**: I set up the `application.yml` with sensible local defaults (like `localhost:5432`) so you can just run it in IntelliJ without any fuss. But I wrapped them in environment variables so Docker Compose can easily inject the container networking stuff (like `db:5432`) without us having to change the code.

### Assumptions
* **Port Conflicts**: I'm assuming you don't already have something running on port `10001` (for the Spring app) or port `5432` (if you have local Postgres running). If you do, you'll just need to tweak the mappings in the `docker-compose.yml`.
* **Task Linking**: Based on the API docs, I assumed a task can depend on another task via the `dependson` field, so the system is built to handle that basic parent-child linking.
* **Data Storage**: Nobody wants to lose their test data every time they shut down a container. I mapped a Docker volume (`postgres-data`) so the database state actually persists on your hard drive between restarts.