# web-app

The website for H2Organise. Runs on port 8090, no database. It calls the 6 backend services with RestTemplate
and keeps the logged in user in the http session.

## Run

Start the backend services first (ports 8080 - 8085), then run `WebAppApplication` (or `mvnw spring-boot:run`)
and open http://localhost:8090

## Pages

| Page | URL | Who |
|---|---|---|
| Login / register | /login | everyone |
| Dashboard | /dashboard | ADMIN, ENGINEER, OPERATOR |
| Zone status | /zones/{id}/status | ADMIN, ENGINEER, OPERATOR |
| Water sources | /resources | ADMIN, ENGINEER |
| Distribution | /distribution | ADMIN, ENGINEER, OPERATOR |
| Water quality | /quality | ADMIN, ENGINEER, OPERATOR |
| Maintenance | /maintenance | ADMIN, ENGINEER, OPERATOR |
| Complaints | /complaints | ADMIN, ENGINEER |
| My complaints | /my/complaints | CITIZEN |
| Users, reports | /admin/users, /admin/reports | ADMIN |

The roles are checked in `config/LoginInterceptor`.

## Code

- `client` - one class per backend service, the json is kept as maps
- `controller` - one per page/module
- `service/ZoneStatusService` - collects the data for the zone status page and works out
  NORMAL / WARNING / CRITICAL from the lowest source level

Marking a task Done on the maintenance page also resolves its fault and sets the pump back to WORKING.

Tests fake the backend services and just check the pages load: `mvnw test`
