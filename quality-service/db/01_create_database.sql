-- Run once as the postgres user. Spring Boot creates the TABLES for you, but not the database itself.
--   psql -U postgres -f db/01_create_database.sql
-- (If quality_db already exists you will see an error. You can ignore it.)
CREATE DATABASE quality_db;
