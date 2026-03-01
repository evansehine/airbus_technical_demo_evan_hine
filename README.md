# Satellites — Technical Demonstrator

Spring Boot REST API to manage satellites

## What it contains
- Spring Boot 3.2.5, Java 17
- H2 in-memory database
- JPA entities: `Satellite`, `SatelliteParameters`, `OrbitType`
- Layers: Controller, Service, Repository, DTOs, Mapper
- Validation and centralized error handling (`RestExceptionHandler`)
- Unit tests (JUnit 5 + Mockito)
- Integration tests (SpringBootTest + MockMvc)

## Run locally

### Prerequisites
- JDK 17
- Maven (bundled wrapper available: `./mvnw` / `.\mvnw.cmd`)
- (Windows) PowerShell or Git Bash recommended for the examples below

### Start the app
From project root:
- ./mvnw clean package
- ./mvnw spring-boot:run

Other useful commands:
- ./mvnw clean install (when updating pom)

### Running Tests

Run all tests (unit + integration):
- ./mvnw test

### H2 Database
Open: http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:testdb
- User: sa
- Password: (empty)

### API endpoints (base /api/satellites)
- GET /api/satellites — list all satellites
- GET /api/satellites/{id} — get by id
- POST /api/satellites — create
- PUT /api/satellites/{id} — update
- DELETE /api/satellites/{id} — delete
- GET /api/satellites/{id}/position — return {lat, lon, alt} or 204 if no parameters

