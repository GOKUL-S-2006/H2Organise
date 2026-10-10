# maintenance-service

Pumps, treatment plants, faults and repair tasks. Runs on port 8084 with the `maintenance_db` postgres database.

## Run

1. In pgAdmin / psql: `CREATE DATABASE maintenance_db;`
2. Put your postgres password in `application.properties`
3. Run `MaintenanceServiceApplication` (or `mvnw spring-boot:run`)

`data.sql` adds the demo rows: Pump P-3 (FAULTY), North Treatment Plant, fault 1 (pump 3), fault 2 (pipeline 1) and task 1 "Replace Pump P-3 motor" for Suresh.

## Endpoints

| Method | URL | Notes |
|---|---|---|
| GET, POST | /pumps | `?zoneId=&status=` |
| GET, PUT, DELETE | /pumps/{id} | |
| GET, POST | /plants | `?zoneId=` |
| GET, PUT, DELETE | /plants/{id} | |
| GET, POST | /faults | `?zoneId=&status=&severity=` |
| GET, PUT, DELETE | /faults/{id} | |
| PUT | /faults/{id}/resolve | sets RESOLVED and resolvedAt |
| GET, POST | /tasks | `?zoneId=&status=&assignedTo=` |
| GET, PUT, DELETE | /tasks/{id} | |
| PUT | /tasks/{id}/complete | sets COMPLETED and completedDate, body `{ "cost", "remarks" }` is optional |

Resolving a fault does not change the pump status, the web-app does `PUT /pumps/3` with `"status": "WORKING"` for that.

Tests use H2 in memory: `mvnw test`
