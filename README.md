# Swiftly

Swiftly is an application for managing SWIFT codes (Bank Identifier Codes) that:
1. **Imports** data from Excel files
2. **Stores** information in a PostgreSQL database
3. **Exposes** data via a REST API (Spring Boot)

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Project Structure](#project-structure)
3. [Installation and Running](#installation-and-running)
    - [Local Run (Maven + Java)](#local-run-maven--java)
    - [Run via Docker](#run-via-docker)
4. [Database Configuration](#database-configuration)
5. [REST Endpoints](#rest-endpoints)
6. [Importing Excel Files](#importing-excel-files)
7. [Tests](#tests)
8. [Common Issues & Solutions](#common-issues--solutions)

---

## Prerequisites

- **Java**: Java 17 or 21 (depending on the Dockerfile)
- **Maven**: 3.8+ (or bundled in the Docker image)
- **Docker** and **Docker Compose** (optional, for containerized deployment)
- **PostgreSQL**: Version 15 (as defined in `docker-compose.yml`)

---

## Project Structure

```plaintext
Swiftly/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/com/swiftly/swiftly/
    │   │   ├── SwiftlyApplication.java
    │   │   ├── controller/
    │   │   │   ├── HomeController.java
    │   │   │   └── SwiftCodeController.java
    │   │   ├── dto/
    │   │   │   └── SwiftCodeRequest.java
    │   │   ├── exception/
    │   │   │   └── GlobalExceptionHandler.java
    │   │   ├── model/
    │   │   │   └── SwiftCode.java
    │   │   ├── repository/
    │   │   │   └── SwiftCodeRepository.java
    │   │   └── service/
    │   │       ├── ExcelParserService.java
    │   │       └── SwiftCodeService.java
    │   └── resources/
    │       └── application.properties
    └── test/
        └── java/com/swiftly/swiftly/
            ├── SwiftlyApplicationTests.java
            └── service/
                └── SwiftCodeServiceTest.java
```

### Key Components

- **`SwiftCodeController`**: REST controller handling CRUD operations and file imports
- **`ExcelParserService`**: Logic for parsing Excel files and storing data
- **`SwiftCodeService`**: Business logic layer (e.g., uppercase conversions, identifying HQ vs. branch)
- **`SwiftCodeRepository`**: JPA interface for database queries
- **`SwiftCodeRequest`**: DTO for receiving request payloads (POST)
- **`application.properties`**: Data source configuration and Spring settings
- **`docker-compose.yml`**: Sets up a Postgres container
- **`Dockerfile`**: Multi-stage build (compilation + final runtime)

---

## Installation and Running

### Local Run (Maven + Java)

1. **Database Setup**
   - Install PostgreSQL locally, or run it using Docker Compose:
     ```bash
     docker-compose up -d
     ```
   - By default, the database will be available on `localhost:5433`

2. **Build and Run**
   - Clone this repository and build the project:
     ```bash
     git clone https://github.com/<YourUsername>/Swiftly.git
     cd Swiftly
     mvn clean package
     mvn spring-boot:run
     ```
   - The application will start on `localhost:8080`
   - Default database URL in `application.properties` is `jdbc:postgresql://localhost:5433/swiftparserdb`

### Run via Docker

1. **Start the database**
   ```bash
   docker-compose up -d
   ```
   This starts a PostgreSQL 15 container on host port 5433.

2. **Build the application image**
   ```bash
   docker build -t swiftly-app .
   ```
   
   The Dockerfile uses a multi-stage build:
   
   ```Dockerfile
   FROM maven:3.9.3-amazoncorretto-17 AS build
   WORKDIR /app
   COPY pom.xml .
   COPY src ./src
   RUN mvn clean package -DskipTests
         
   FROM eclipse-temurin:21-jdk
   WORKDIR /app
   COPY --from=build /app/target/Swiftly-0.0.1-SNAPSHOT.jar app.jar
   EXPOSE 8080
   ENTRYPOINT ["java", "-jar", "app.jar"]
   ```

3. **Run the application container**
   ```bash
   docker run --name swiftly_container --rm \
     -p 8080:8080 \
     -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5433/swiftparserdb \
     -e SPRING_DATASOURCE_USERNAME=postgres \
     -e SPRING_DATASOURCE_PASSWORD=postgres \
     swiftly-app
   ```  
   
   - The app will start on port 8080, connecting to PostgreSQL at host.docker.internal:5433 (on Windows/Mac)
   - On Linux, you may need to adjust the hostname (use network bridging or the container name)

## Database Configuration

In `application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/swiftparserdb
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

- `ddl-auto=update` creates/updates the `swift_codes` table automatically
- When running in Docker, you may need to use `host.docker.internal` or the container's hostname in the connection URL

## REST Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | `/` | Simple welcome endpoint. Returns "Witaj w aplikacji Swiftly!" |
| POST   | `/v1/swift-codes/import` | Imports an Excel file. Requires form-data with key `file` |
| GET    | `/v1/swift-codes/{swiftCode}` | Returns details for a single SWIFT code. Includes "branches" for HQ codes |
| GET    | `/v1/swift-codes/country/{iso2}` | Returns all codes for the specified ISO2 country code |
| POST   | `/v1/swift-codes` | Creates a new SWIFT code entry |
| DELETE | `/v1/swift-codes/{swiftCode}` | Deletes the specified code |

### Examples

#### Import Excel File
- **Method**: POST
- **URL**: `http://localhost:8080/v1/swift-codes/import`
- **Body**: form-data with key `file` (type: File)
- **Response**: `{"message":"File imported successfully"}` on success

#### Create New SWIFT Code
- **Method**: POST
- **URL**: `http://localhost:8080/v1/swift-codes`
- **Body**:
  ```json
  {
    "swiftCode": "ABCDPLXXX",
    "bankName": "Test Bank",
    "countryISO2": "PL",
    "countryName": "Poland",
    "address": "Sample Address"
  }
  ```
- **Response**: `{"message":"SWIFT code added successfully","swiftCode":"ABCDPLXXX"}`

## Importing Excel Files

1. Use endpoint: `POST /v1/swift-codes/import`
2. The `ExcelParserService` class reads specific columns (index-based). Ensure your `.xlsx` file matches the expected format.
3. Successfully parsed data is inserted into the `swift_codes` table.
4. Parsing errors return a 400 status code with an error message.

## Tests

### Unit Tests
- `SwiftCodeServiceTest.java` verifies service methods (e.g., `isHeadquarterCode`)
- Run with `mvn test`

### Integration Tests
- `SwiftlyApplicationTests.java` ensures the application context loads correctly and verifies repository CRUD operations

## Common Issues & Solutions

1. **"release version 21 not supported"**
   - The image `maven:3.9.3-amazoncorretto-21` doesn't exist
   - Solution: Use the multi-stage approach in the Dockerfile (compile with Java 17, run with Java 21)

2. **Excel import failure**
   - Verify that column indexes in your Excel file match what `ExcelParserService` expects
   - Check file format (.xlsx vs .xls)

3. **Database connection issues**
   - Verify hostname/port: `localhost:5433` for local runs vs `host.docker.internal:5433` for Docker
   - Check logs for authentication failures or missing database errors
   - Ensure PostgreSQL is running and accepts connections

4. **"SwiftCodeRequest is flagged as unused"**
   - This is an IntelliJ false positive; the class is used in `POST /v1/swift-codes`

---

## Contact & Support

If you have questions or suggestions, please open an Issue in this repository or contact me at:
[antaroupost@gmail.com](mailto:antaroupost@gmail.com)

Enjoy using Swiftly!


