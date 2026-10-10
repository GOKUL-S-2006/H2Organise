# quality-service API contract (port 8083, database quality_db)

Base URL: `http://localhost:8083`

Rules shared with all services: JSON in camelCase, dates `2026-10-10`, POST returns 201, DELETE returns 204,
everything else 200. Errors look like `{ "timestamp": "...", "status": 404, "message": "..." }`
(400 = validation problem, 404 = not found).

## Standards

| Method | URL | Notes |
| --- | --- | --- |
| GET | `/standards` | All acceptable ranges (PH, TURBIDITY, CHLORINE, TDS) |
| PUT | `/standards` | Body `{ "parameter": "PH", "minValue": 6.5, "maxValue": 8.5 }`. Creates the row if that parameter is missing |

## Tests

| Method | URL | Notes |
| --- | --- | --- |
| GET | `/tests` | Filters `?zoneId=&sourceId=&status=` (status: COMPLIANT or NON_COMPLIANT). Newest first |
| POST | `/tests` | 201. `complianceStatus` is calculated by the service (never sent by the client) |
| GET | `/tests/{id}` | One test, 404 if missing |
| PUT | `/tests/{id}` | Replaces the test and recalculates compliance |
| DELETE | `/tests/{id}` | 204 |
| GET | `/tests/latest` | Latest test, optional `?zoneId=`. **404 if the zone has no test yet** |

Request body for POST and PUT `/tests`:

```json
{
  "sourceId": 1,
  "zoneId": 1,
  "testDate": "2026-10-10",
  "testedBy": "Lab Technician",
  "ph": 7.2,
  "turbidity": 1.2,
  "chlorine": 0.5,
  "tds": 280,
  "bacteriaPresent": false,
  "remarks": "Routine test"
}
```

Required: `zoneId`, `testDate`, and at least one of `ph`, `turbidity`, `chlorine`, `tds`, `bacteriaPresent`.
`sourceId` is optional.

Response (example):

```json
{ "id": 1, "sourceId": 1, "zoneId": 1, "testDate": "2026-10-09", "testedBy": "Lab Technician",
  "ph": 7.2, "turbidity": 1.2, "chlorine": 0.5, "tds": 280.0, "bacteriaPresent": false,
  "complianceStatus": "COMPLIANT", "remarks": "Routine test, all parameters within limits" }
```

### Compliance rule

A test is `NON_COMPLIANT` if any measured value is outside the standard's min/max (limits are inclusive)
or `bacteriaPresent` is true. Values that were not measured are skipped. Otherwise it is `COMPLIANT`.
Changing a standard later does not re-grade old tests.

## Inspections

| Method | URL | Notes |
| --- | --- | --- |
| GET | `/inspections` | Filters `?zoneId=&result=` (PASSED, FAILED, ACTION_REQUIRED). Newest first |
| POST | `/inspections` | 201 |
| GET | `/inspections/{id}` | One inspection |
| PUT | `/inspections/{id}` | Replace |
| DELETE | `/inspections/{id}` | 204 |
| GET | `/inspections/due` | `nextDueDate` is today or earlier (overdue) or within the next 30 days, soonest first. Optional `?zoneId=` |

Request body for POST and PUT `/inspections`:

```json
{ "sourceId": 1, "zoneId": 1, "inspectionDate": "2026-10-10", "inspectorName": "Ravi Kumar",
  "result": "PASSED", "remarks": "All good", "nextDueDate": "2026-11-09" }
```

Required: `sourceId`, `zoneId`, `inspectionDate`, `inspectorName`, `result`. `nextDueDate` must not be before `inspectionDate`.

## Compliance summary

| Method | URL | Notes |
| --- | --- | --- |
| GET | `/compliance/summary` | Optional `?zoneId=` |

```json
{ "zoneId": 1, "totalTests": 5, "compliantTests": 4, "nonCompliantTests": 1, "compliancePercent": 80.0 }
```

`compliancePercent` is a plain number (80.0 means 80%). It is 0.0 when no tests exist.

## For the web-app (Person 5)

The zone status page calls `GET /tests/latest?zoneId={id}`. Handle the 404 case ("no test recorded yet").
