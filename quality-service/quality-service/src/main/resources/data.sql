-- seed data for the demo, ids must match what the whole team agreed on
-- ON CONFLICT DO NOTHING so the app doesn't crash when we restart it

-- allowed ranges used to decide if a test is COMPLIANT
INSERT INTO quality_standards (id, parameter, min_value, max_value) VALUES
(1, 'PH', 6.5, 8.5),
(2, 'TURBIDITY', 0, 5),
(3, 'CHLORINE', 0.2, 1.0),
(4, 'TDS', 0, 500)
ON CONFLICT (id) DO NOTHING;

-- latest test for North Zone, shown on the zone status page
INSERT INTO quality_tests (id, source_id, zone_id, test_date, tested_by, ph, turbidity, chlorine, tds, bacteria_present, compliance_status, remarks) VALUES
(1, 1, 1, '2026-10-09', 'Ravi Kumar', 7.2, 2.1, 0.5, 320, false, 'COMPLIANT', 'Routine weekly test')
ON CONFLICT (id) DO NOTHING;

INSERT INTO inspections (id, source_id, zone_id, inspection_date, inspector_name, result, remarks, next_due_date) VALUES
(1, 1, 1, '2026-10-05', 'Ravi Kumar', 'PASSED', 'Intake screens clean', '2026-11-04')
ON CONFLICT (id) DO NOTHING;

-- move the id counters past the ids above so new rows don't clash
SELECT setval(pg_get_serial_sequence('quality_standards', 'id'), (SELECT MAX(id) FROM quality_standards));
SELECT setval(pg_get_serial_sequence('quality_tests', 'id'), (SELECT MAX(id) FROM quality_tests));
SELECT setval(pg_get_serial_sequence('inspections', 'id'), (SELECT MAX(id) FROM inspections));
