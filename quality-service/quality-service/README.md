# quality-service

Water quality standards, test results and inspections. Runs on port 8083 with the `quality_db` postgres database.

## Run

1. In pgAdmin / psql: `CREATE DATABASE quality_db;`
2. Put your postgres password in `application.properties`
3. Run `QualityServiceApplication` (or `mvnw spring-boot:run`)

`data.sql` adds the four standards (pH, turbidity, chlorine, TDS), test 1 for North Zone (COMPLIANT) and inspection 1 (PASSED).

## Endpoints

| Method | URL | Notes |
|---|---|---|
| GET, PUT | /standards | PUT body `{ "parameter": "PH", "minValue": 6.5, "maxValue": 8.5 }` |
| GET, POST | /tests | `?zoneId=&sourceId=&status=`, POST works out complianceStatus |
| GET, PUT, DELETE | /tests/{id} | |
| GET | /tests/latest | `?zoneId=` |
| GET, POST | /inspections | `?zoneId=&result=` |
| GET, PUT, DELETE | /inspections/{id} | |
| GET | /inspections/due | next due date in the next 30 days |
| GET | /compliance/summary | `?zoneId=` optional |

A test is NON_COMPLIANT if bacteria are present or any value is outside its standard range. Empty values are skipped.

Tests use H2 in memory: `mvnw test`
