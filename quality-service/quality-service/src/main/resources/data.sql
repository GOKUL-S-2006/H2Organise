-- Demo data for quality_db (runs on every start, safe to run again).
-- zone_id 1 = North Zone (distribution-service), source_id 1 = Hemavathi Reservoir (resource-service).

INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (1, 'PH', 6.5, 8.5) ON CONFLICT (id) DO NOTHING;
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (2, 'TURBIDITY', 0, 5) ON CONFLICT (id) DO NOTHING;
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (3, 'CHLORINE', 0.2, 1.0) ON CONFLICT (id) DO NOTHING;
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES (4, 'TDS', 0, 500) ON CONFLICT (id) DO NOTHING;

INSERT INTO quality_tests (id, source_id, zone_id, test_date, tested_by, ph, turbidity, chlorine, tds,
                           bacteria_present, compliance_status, remarks)
VALUES (1, 1, 1, '2026-10-09', 'Ravi Kumar', 7.2, 2.1, 0.5, 320, false, 'COMPLIANT', 'Routine weekly test')
ON CONFLICT (id) DO NOTHING;

INSERT INTO inspections (id, source_id, zone_id, inspection_date, inspector_name, result, remarks, next_due_date)
VALUES (1, 1, 1, '2026-10-05', 'Ravi Kumar', 'PASSED', 'Intake screens clean', '2026-11-04')
ON CONFLICT (id) DO NOTHING;

-- Move the id counters past the ids used above, so new rows do not clash with them.
SELECT setval(pg_get_serial_sequence('quality_standards', 'id'), (SELECT MAX(id) FROM quality_standards));
SELECT setval(pg_get_serial_sequence('quality_tests', 'id'), (SELECT MAX(id) FROM quality_tests));
SELECT setval(pg_get_serial_sequence('inspections', 'id'), (SELECT MAX(id) FROM inspections));
