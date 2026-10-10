# quality-service

H2Organise water quality service. It runs on port **8083**, uses PostgreSQL `quality_db`, and its package is
`com.h2organise.quality`.

**Setup:**
1. Run `CREATE DATABASE quality_db;`.
2. Set `DB_PASSWORD` (and `DB_USERNAME` / `DB_URL` if they differ from the defaults).
3. Run `QualityServiceApplication`, or `./mvnw spring-boot:run`.

Tables are created by Hibernate. Demo rows come from `data.sql`:
- the four standards
- test 1 (zone 1, pH 7.2, COMPLIANT)
- inspection 1 (PASSED)

## Tables

| Table | Columns |
|---|---|
| quality_standards | id, parameter (PH/TURBIDITY/CHLORINE/TDS, unique), min_value, max_value |
| quality_tests | id, source_id (ref), zone_id (ref), test_date, tested_by, ph, turbidity, chlorine, tds, bacteria_present, compliance_status, remarks |
| inspections | id, source_id (ref), zone_id (ref), inspection_date, inspector_name, result (PASSED/FAILED/ACTION_REQUIRED), remarks, next_due_date |

`source_id` and `zone_id` are plain ids from resource-service and distribution-service, with no foreign keys.

## Endpoints

| Method | URL | Notes |
|---|---|---|
| GET | /standards | all ranges |
| PUT | /standards | body `{ "parameter": "PH", "minValue": 6.5, "maxValue": 8.5 }` |
| GET, POST | /tests | filters `?zoneId=&sourceId=&status=` (COMPLIANT/NON_COMPLIANT); POST sets `complianceStatus` |
| GET, PUT, DELETE | /tests/{id} | PUT checks compliance again |
| GET | /tests/latest | latest by test date, optional `?zoneId=` |
| GET, POST | /inspections | filters `?zoneId=&result=` |
| GET, PUT, DELETE | /inspections/{id} | |
| GET | /inspections/due | next due date within the next 30 days |
| GET | /compliance/summary | optional `?zoneId=`; returns total, compliant, non-compliant, percent |

**Compliance rule.** A test is `NON_COMPLIANT` if `bacteriaPresent` is true, or if any measured value is outside its
standard. Values that are left empty are not checked.

Successful POST returns 201 and DELETE returns 204. Errors return `{ "timestamp", "status", "message" }` with
400 or 404.

**Tests:** run `./mvnw test`. They use H2 in memory, so PostgreSQL is not needed.
