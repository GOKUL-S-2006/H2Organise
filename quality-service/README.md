# quality-service (H2Organise)

Water quality microservice: quality standards, test results with automatic compliance decision,
inspections with due dates, and a compliance summary.

- Java 21, Spring Boot 4.1.1, Maven, Spring Data JPA, PostgreSQL
- Port **8083**, database **quality_db**
- Package `com.h2organise.quality` (controller, service, repository, entity, dto, exception)

## Run it

1. Install JDK 21 and PostgreSQL. Start PostgreSQL.
2. Create the database (once):
   ```
   psql -U postgres -f db/01_create_database.sql
   ```
   or in pgAdmin: create a database named `quality_db`.
3. Check `src/main/resources/application.properties`. The user is `postgres`. The password can be given
   with the `DB_PASSWORD` environment variable, otherwise the value in the file is used.
4. Start the service from this folder:
   ```
   ./mvnw spring-boot:run          (Windows: mvnw.cmd spring-boot:run)
   ```
   Hibernate creates the three tables, then `data.sql` inserts the seed rows (safe on every restart).
5. Try it: open <http://localhost:8083/standards> or import `postman/quality-service.postman_collection.json`.

## Run the tests

```
./mvnw test
```
`ComplianceEvaluatorTest` needs no database. `QualityServiceApplicationTests` (context loads) needs PostgreSQL running.

## Endpoints

See `docs/api-contract-quality.md`.

## Project layout

```
src/main/java/com/h2organise/quality
  QualityServiceApplication.java
  controller/   StandardController, QualityTestController, InspectionController, ComplianceController
  service/      StandardService, QualityTestService, InspectionService, ComplianceService, ComplianceEvaluator
  repository/   QualityStandardRepository, QualityTestRepository, InspectionRepository
  entity/       QualityStandard, QualityTest, Inspection
  dto/          request and response records
  exception/    GlobalExceptionHandler, ApiError, ResourceNotFoundException, BadRequestException
src/main/resources  application.properties, data.sql
db/                 SQL helper scripts
docs/               API contract
postman/            Postman collection
```
