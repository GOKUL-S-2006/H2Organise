# H2Organise

Water resource management project, team 8. Java 21, Spring Boot, one PostgreSQL database per service,
web pages in Thymeleaf.

| Service | Port | Database |
|---|---|---|
| auth-service | 8080 | auth_db |
| resource-service | 8081 | resource_db |
| distribution-service | 8082 | distribution_db |
| quality-service | 8083 | quality_db |
| maintenance-service | 8084 | maintenance_db |
| complaint-service | 8085 | complaint_db |
| web-app | 8090 | - |

## Setup

1. Create the 6 databases once in pgAdmin / psql:
   ```
   CREATE DATABASE auth_db;
   CREATE DATABASE resource_db;
   CREATE DATABASE distribution_db;
   CREATE DATABASE quality_db;
   CREATE DATABASE maintenance_db;
   CREATE DATABASE complaint_db;
   ```
2. Every service uses user `postgres` / password `root`. Change `application.properties` if yours is different.
3. Start the 6 services (any order), then the web-app. Each one: run its `*Application` class or `mvnw spring-boot:run`.
4. Open http://localhost:8090

Tables are made by Hibernate and `data.sql` in each service adds the demo data.

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
