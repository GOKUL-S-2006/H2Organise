-- Optional checks after the service has started once.
--   psql -U postgres -d quality_db -f db/02_verify.sql
\dt
SELECT * FROM quality_standards ORDER BY id;
SELECT id, zone_id, test_date, ph, turbidity, chlorine, tds, bacteria_present, compliance_status FROM quality_tests ORDER BY id;
SELECT id, zone_id, inspection_date, result, next_due_date FROM inspections ORDER BY id;
