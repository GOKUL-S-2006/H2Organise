# API contract

All services send and take JSON. Field names are camelCase, dates are `2026-10-10`, date-times are
`2026-10-10T06:00:00`. POST gives 201, DELETE gives 204, the rest 200.

Errors always look like this, 400 for bad input and 404 for not found:

```
{ "timestamp": "2026-10-10T09:00:00", "status": 404, "message": "Fault not found with id 9" }
```

Every resource has `GET /things`, `GET /things/{id}`, `POST /things`, `PUT /things/{id}`, `DELETE /things/{id}`
unless written otherwise. PUT needs the full object, not only the changed field.

## auth-service (8080)

| Method | URL | Notes |
|---|---|---|
| POST | /auth/login | `{ email, password }` -> `{ id, fullName, email, role, zoneId }`, 401 if wrong |
| POST | /auth/register | `{ fullName, email, password, phone, zoneId }`, always makes a CITIZEN |
| GET, POST | /users | `?role=`, POST is for the admin to make staff accounts (needs `role`) |
| GET, PUT, DELETE | /users/{id} | DELETE only deactivates |

## resource-service (8081)

| Method | URL | Notes |
|---|---|---|
| GET, POST | /sources | `?zoneId=&type=&status=` |
| GET, PUT, DELETE | /sources/{id} | |
| GET | /sources/low | sources under their threshold right now |
| GET | /sources/{id}/capacity | total, current and available capacity |
| GET, POST | /levels | `?sourceId=&from=&to=`, POST works out `levelPercent` |
| GET | /levels/latest | `?sourceId=` or `?zoneId=`, newest reading per source |

## distribution-service (8082)

| Method | URL | Notes |
|---|---|---|
| GET, POST | /zones | |
| GET, PUT, DELETE | /zones/{id} | |
| GET, POST | /pipelines | `?zoneId=&status=` |
| GET, POST | /schedules | `?zoneId=&status=&date=`, PUT on one schedule is used to reduce it |
| GET, POST | /consumption | `?zoneId=&category=&from=&to=` |
| GET | /consumption/summary | totals by category, `?zoneId=` |

## quality-service (8083)

| Method | URL | Notes |
|---|---|---|
| GET, PUT | /standards | PUT `{ parameter, minValue, maxValue }` |
| GET, POST | /tests | `?zoneId=&sourceId=&status=`, POST sets `complianceStatus` |
| GET | /tests/latest | `?zoneId=`, 404 if the zone has no tests |
| GET, POST | /inspections | `?zoneId=&result=` |
| GET | /inspections/due | next due date in the next 30 days |
| GET | /compliance/summary | `?zoneId=` |

## maintenance-service (8084)

| Method | URL | Notes |
|---|---|---|
| GET, POST | /pumps | `?zoneId=&status=` |
| GET, POST | /plants | `?zoneId=` |
| GET, POST | /faults | `?zoneId=&status=&severity=` |
| PUT | /faults/{id}/resolve | RESOLVED + resolvedAt |
| GET, POST | /tasks | `?zoneId=&status=&assignedTo=` |
| PUT | /tasks/{id}/complete | COMPLETED + completedDate, optional body `{ cost, remarks }` |

For a fault or task with `assetType` PIPELINE the `assetId` is a pipeline id from distribution-service.

## complaint-service (8085)

| Method | URL | Notes |
|---|---|---|
| GET, POST | /complaints | `?zoneId=&status=&citizenId=&type=`, new ones are REGISTERED |
| GET | /complaints/summary | counts per status |
| PUT | /complaints/{id}/status | `{ status, comment, assignedTo, updatedBy }`, assignedTo needed for ASSIGNED |
| GET | /complaints/{id}/updates | status history |
| GET, POST | /supply-requests | `?zoneId=&status=&citizenId=` |
| PUT | /supply-requests/{id}/status | `{ status }` |

## Status values

| Item | Values |
|---|---|
| Zone | NORMAL, WARNING, CRITICAL |
| Source | ACTIVE, INACTIVE, UNDER_MAINTENANCE |
| Pipeline | ACTIVE, LEAKING, CLOSED |
| Schedule | PLANNED, ACTIVE, COMPLETED, REDUCED, CANCELLED |
| Fault | OPEN, IN_PROGRESS, RESOLVED |
| Task | PENDING, IN_PROGRESS, COMPLETED |
| Complaint | REGISTERED, ASSIGNED, IN_PROGRESS, RESOLVED, REJECTED |
| Supply request | PENDING, SCHEDULED, FULFILLED, REJECTED |
| Quality | COMPLIANT, NON_COMPLIANT |
| Role | ADMIN, ENGINEER, OPERATOR, CITIZEN |
