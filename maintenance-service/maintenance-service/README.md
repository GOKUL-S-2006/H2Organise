# maintenance-service

H2Organise infrastructure and maintenance service. It runs on port **8084**, uses PostgreSQL `maintenance_db`, and its
package is `com.h2organise.maintenance`.

**Setup:**
1. Run `CREATE DATABASE maintenance_db;`.
2. Set `DB_PASSWORD` (and `DB_USERNAME` / `DB_URL` if they differ from the defaults).
3. Run `MaintenanceServiceApplication`, or `./mvnw spring-boot:run`.

Demo rows from `data.sql`:
- pump 3 Pump P-3 (FAULTY)
- plant 1 North Treatment Plant
- fault 1 (PUMP 3, HIGH, OPEN) and fault 2 (PIPELINE 1, MEDIUM, OPEN)
- task 1 "Replace Pump P-3 motor" (Suresh, PENDING)

## Tables

| Table | Columns |
|---|---|
| pumps | id, name, zone_id (ref), source_id (ref), capacity_lph, installed_date, status (WORKING/FAULTY/UNDER_REPAIR) |
| treatment_plants | id, name, zone_id (ref), capacity_mld, last_serviced_date, status (OPERATIONAL/DOWN/UNDER_MAINTENANCE) |
| faults | id, asset_type (PUMP/PLANT/PIPELINE), asset_id, zone_id (ref), description, severity (LOW/MEDIUM/HIGH/CRITICAL), status (OPEN/IN_PROGRESS/RESOLVED), reported_at, resolved_at, reported_by |
| maintenance_tasks | id, fault_id (**FK** to faults, nullable), asset_type, asset_id, zone_id (ref), title, task_type (REPAIR/PREVENTIVE/INSPECTION), assigned_to, scheduled_date, completed_date, status (PENDING/IN_PROGRESS/COMPLETED), cost, remarks |

`fault_id` is the only foreign key. `asset_id` is a plain id, because a PIPELINE belongs to distribution-service.

## Endpoints

| Method | URL | Notes |
|---|---|---|
| GET, POST | /pumps | filters `?zoneId=&status=` |
| GET, PUT, DELETE | /pumps/{id} | |
| GET, POST | /plants | filter `?zoneId=` |
| GET, PUT, DELETE | /plants/{id} | |
| GET, POST | /faults | filters `?zoneId=&status=&severity=` |
| GET, PUT, DELETE | /faults/{id} | PUT can also change `status` |
| PUT | /faults/{id}/resolve | sets RESOLVED and `resolvedAt` |
| GET, POST | /tasks | filters `?zoneId=&status=&assignedTo=` |
| GET, PUT, DELETE | /tasks/{id} | |
| PUT | /tasks/{id}/complete | sets COMPLETED and `completedDate`; optional body `{ "cost": 12500, "remarks": "..." }` |

**Status fields.** `status` is optional in the request body. A new record gets WORKING, OPERATIONAL, OPEN or PENDING.
On update, an empty `status` keeps the old value.

**Linked records.** Resolving a fault or completing a task does not change the pump or plant status by itself. The
blueprint does not say it should, so the web-app updates them separately (for example `PUT /pumps/3` with
`"status": "WORKING"`). Deleting a fault keeps its tasks, but unlinks them.

**Tests:** run `./mvnw test`. They use H2 in memory, so PostgreSQL is not needed.
