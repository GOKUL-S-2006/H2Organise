# H2Organise - Integrated Water & Natural Resource Management (Microservices)

Seven independent Spring Boot 3.5 applications (Java 21), one PostgreSQL 18 database with one schema per service,
REST between services, Thymeleaf web UI, JWT security.

| Module | Port | Schema | Owns |
|---|---|---|---|
| auth-service | 8081 | `auth_schema` | users, roles, login, JWT tokens |
| resource-service | 8082 | `resource_schema` | water sources, level readings, availability %, low-water warnings |
| distribution-service | 8083 | `distribution_schema` | supply zones, schedules (allocations), supply requests, consumption |
| quality-service | 8084 | `quality_schema` | quality tests, configurable limits |
| maintenance-service | 8085 | `maintenance_schema` | assets, fault reports, maintenance jobs |
| complaint-service | 8086 | `complaint_schema` | public complaints + workflow |
| web-app | 8080 | - | Thymeleaf pages and dashboard (calls the services) |

See [docs/architecture.md](docs/architecture.md) for the design and [docs/api-documentation.md](docs/api-documentation.md) for every endpoint.

## 1. One-time setup

1. **Java 21** must be used (not 25): `export JAVA_HOME=$(/usr/libexec/java_home -v 21)`
2. **Database**: run the script once (or paste it into pgAdmin's Query Tool):
   ```
   /Library/PostgreSQL/18/bin/psql -U postgres -h localhost -f infrastructure/create-schemas.sql
   ```
   (Optional alternative: `docker compose -f infrastructure/docker-compose.yml up -d`.)
3. **Password**: `cp db-local.properties.example db-local.properties` and set your PostgreSQL password.
   This file is git-ignored. Every service reads it.

Tables are created by Hibernate on first start. Sample data (4 resources with a week of readings, 4 assets,
quality limits, users `admin`/`admin123` and `staff`/`staff123`) is inserted automatically.

## 2. Build and test

```
./mvnw clean package
```
Builds all 7 jars and runs the JUnit/Mockito tests (no database needed for tests).

## 3. Start (one terminal tab per service, auth first)

```
./mvnw -pl auth-service spring-boot:run
./mvnw -pl resource-service spring-boot:run
./mvnw -pl distribution-service spring-boot:run
./mvnw -pl quality-service spring-boot:run
./mvnw -pl maintenance-service spring-boot:run
./mvnw -pl complaint-service spring-boot:run
./mvnw -pl web-app spring-boot:run
```
Open http://localhost:8080 and log in as `admin` / `admin123`.

**Eclipse Spring Tools:** File > Import > Maven > Existing Maven Projects > select `AquaMonitor` (all 8 POMs ticked).
Check Project > Properties > Java Build Path uses JRE 21. Start each `*Application` class with Run As > Spring Boot App
(the Boot Dashboard can start them all).

## 4. Roles

| Role | How | Can do |
|---|---|---|
| ADMIN | seeded `admin` | everything, incl. deletes and changing quality limits |
| STAFF | seeded `staff`, or created by admin via `POST /api/auth/users` | all daily work, no deletes |
| PUBLIC | `/register` page | submit and track complaints only |
| (no login) | `/complaints/public` | submit and track complaints |

## 5. Demo: water-shortage scenario

1. Dashboard: **Green Lake is at 22.5 %** (red bar, low-water warning), trend chart shows a 7-day drop.
2. Supply Zones: add zone "East Ward", source = Green Lake.
3. Schedules: plan 300 ML for East Ward -> accepted. Plan another 200 ML -> **rejected**
   ("Green Lake has 450 ML and 300 ML is already planned, so only 150 ML is free") - distribution-service asked
   resource-service over REST.
4. Complete the first schedule with 280 ML -> Green Lake drops to 170 ML (8.5 %); a reading is added automatically.
5. Consumption: record DOMESTIC 45.5 ML -> category chart updates.
6. Open `/complaints/public` in a private window (no login): report WATER_SHORTAGE -> gets number `CMP-...`, HIGH priority.
7. Complaints (staff): Start (assign) -> Resolve (remarks). Track the number on the public page -> RESOLVED.
8. Quality: record pH 7.1, turbidity 8, TDS 620 for DRINKING -> FAIL with reasons, labelled "not a certified lab result".
9. **Service failure:** stop quality-service. The dashboard still loads and says "Not available right now: Quality Service".

## 6. Git: suggested commit order

Commit from the `AquaMonitor` folder, one milestone at a time. Check `git status` first and make sure that
`db-local.properties` and `target/` do **not** appear.

```
git add pom.xml mvnw mvnw.cmd .mvn .gitignore db-local.properties.example infrastructure README.md docs
git commit -m "Initialize microservices Maven project"
git add auth-service && git commit -m "Add auth service with JWT login"
git add resource-service && git commit -m "Implement resource service and availability monitoring"
git add distribution-service && git commit -m "Implement distribution service"
git add quality-service && git commit -m "Add water quality service"
git add maintenance-service && git commit -m "Add maintenance service"
git add complaint-service && git commit -m "Implement complaint management"
git add web-app && git commit -m "Integrate dashboard and web interface"
git push
```
