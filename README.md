# H2Organise

Water resource management project, team 8. Java 21, Spring Boot microservices, one PostgreSQL
database per service, web pages in Thymeleaf.

## Prerequisites

- **Java 21** (JDK) — check with `java -version`
- **PostgreSQL** running on `localhost:5432`, user `postgres`, password `root`
- Maven is **not** required; each service includes the Maven wrapper (`mvnw` / `mvnw.cmd`)

## Services

| Service | Folder | Port | Database |
|---|---|---|---|
| auth-service | `auth-service` | 8080 | auth_db |
| resource-service | `resource-service/resource-service` | 8081 | resource_db |
| distribution-service | `distribution-service/distribution-service` | 8082 | distribution_db |
| quality-service | `quality-service/quality-service` | 8083 | quality_db |
| maintenance-service | `maintenance-service/maintenance-service` | 8084 | maintenance_db |
| complaint-service | `complaint-service/complaint-service` | 8085 | complaint_db |
| web-app | `web-app` | 8090 | - |

## How to run

### 1. Create the databases (one time only)

In pgAdmin or `psql -U postgres`:

```sql
CREATE DATABASE auth_db;
CREATE DATABASE resource_db;
CREATE DATABASE distribution_db;
CREATE DATABASE quality_db;
CREATE DATABASE maintenance_db;
CREATE DATABASE complaint_db;
```

If your PostgreSQL username/password is not `postgres` / `root`, update
`spring.datasource.username` and `spring.datasource.password` in each service's
`src/main/resources/application.properties`.

Tables are created automatically by Hibernate, and each service's `data.sql` loads the demo data.

### 2. Start the 6 backend services

Open a separate terminal for each one (any order):

```bash
cd auth-service                              && ./mvnw spring-boot:run
cd resource-service/resource-service         && ./mvnw spring-boot:run
cd distribution-service/distribution-service && ./mvnw spring-boot:run
cd quality-service/quality-service           && ./mvnw spring-boot:run
cd maintenance-service/maintenance-service   && ./mvnw spring-boot:run
cd complaint-service/complaint-service       && ./mvnw spring-boot:run
```

On Windows use `mvnw.cmd spring-boot:run` instead of `./mvnw spring-boot:run`.
Alternatively, open the project in IntelliJ and run each service's `*Application` class.

### 3. Start the web app

After the services are up:

```bash
cd web-app && ./mvnw spring-boot:run
```

### 4. Open the app

Go to **http://localhost:8090** and log in with one of the demo accounts below.

## Demo logins

All passwords are `password123`

| Email | Role |
|---|---|
| ravi@aqua.in | ENGINEER |
| operator@aqua.in | OPERATOR |
| admin@aqua.in | ADMIN |
| meena@mail.com | CITIZEN |

## Demo story

1. Log in as Meena and report a leak in North Zone.
2. Log in as Ravi. The dashboard shows North Zone CRITICAL (22%, threshold 30%).
3. Open the zone: Pump P-3 is faulty, the main line is leaking, there is a tanker request and the leak complaint.
4. Reduce the evening supply to 9.6 ML, assign the pump repair to Suresh, schedule the tanker, assign the complaint.
5. On the maintenance page mark the repair Done. The fault is resolved and the pump goes back to WORKING.
6. Record a new reading on Hemavathi Reservoir (205 ML = 41%). North Zone shows NORMAL again.

## Troubleshooting

- **Port already in use** — stop whatever is using that port, or change `server.port` in that service's `application.properties`.
- **Database connection error** — make sure PostgreSQL is running and the databases from step 1 exist.
- **`./mvnw: Permission denied`** (Mac/Linux) — run `chmod +x mvnw` in that folder.
