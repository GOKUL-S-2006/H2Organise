-- Seed data for quality_db. Safe to run on every start: ON CONFLICT DO NOTHING skips rows that exist.
-- IDs and values follow the team blueprint (zone 1 = North, source 1 = Hemavathi Reservoir).

-- Quality standards (acceptable ranges)
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (1, 'PH', 6.5, 8.5) ON CONFLICT DO NOTHING;
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (2, 'TURBIDITY', 0.0, 5.0) ON CONFLICT DO NOTHING;
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (3, 'CHLORINE', 0.2, 1.0) ON CONFLICT DO NOTHING;
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (4, 'TDS', 0.0, 500.0) ON CONFLICT DO NOTHING;

-- Test 1: North Zone, pH 7.2, COMPLIANT (blueprint demo data)
INSERT INTO quality_tests (id, source_id, zone_id, test_date, tested_by, ph, turbidity, chlorine, tds, bacteria_present, compliance_status, remarks)
VALUES (1, 1, 1, '2026-10-09', 'Lab Technician', 7.2, 1.2, 0.5, 280.0, FALSE, 'COMPLIANT', 'Routine test, all parameters within limits')
ON CONFLICT DO NOTHING;

-- Inspection 1: North Zone, PASSED, next due within 30 days so /inspections/due shows it
INSERT INTO inspections (id, source_id, zone_id, inspection_date, inspector_name, result, remarks, next_due_date)
VALUES (1, 1, 1, '2026-10-05', 'Ravi Kumar', 'PASSED', 'Reservoir inlet and sampling point in good condition', '2026-11-04')
ON CONFLICT DO NOTHING;

-- Move the ID counters past the seeded rows so new records do not collide
SELECT setval(pg_get_serial_sequence('quality_standards', 'id'), (SELECT COALESCE(MAX(id), 1) FROM quality_standards));
SELECT setval(pg_get_serial_sequence('quality_tests', 'id'), (SELECT COALESCE(MAX(id), 1) FROM quality_tests));
SELECT setval(pg_get_serial_sequence('inspections', 'id'), (SELECT COALESCE(MAX(id), 1) FROM inspections));
