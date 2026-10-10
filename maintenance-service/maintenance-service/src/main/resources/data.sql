-- Demo data for maintenance_db (runs on every start, safe to run again).
-- zone_id 1 = North Zone (distribution-service), source_id 1 = Hemavathi Reservoir (resource-service),
-- PIPELINE asset 1 = North Main Line (distribution-service).

INSERT INTO pumps (id, name, zone_id, source_id, capacity_lph, installed_date, status)
VALUES (3, 'Pump P-3', 1, 1, 45000, '2019-06-15', 'FAULTY')
ON CONFLICT (id) DO NOTHING;

INSERT INTO treatment_plants (id, name, zone_id, capacity_mld, last_serviced_date, status)
VALUES (1, 'North Treatment Plant', 1, 40, '2026-08-20', 'OPERATIONAL')
ON CONFLICT (id) DO NOTHING;

INSERT INTO faults (id, asset_type, asset_id, zone_id, description, severity, status, reported_at, resolved_at, reported_by)
VALUES (1, 'PUMP', 3, 1, 'Pump motor overheating and tripping', 'HIGH', 'OPEN', '2026-10-09 18:30:00', NULL, 'Operator')
ON CONFLICT (id) DO NOTHING;

INSERT INTO faults (id, asset_type, asset_id, zone_id, description, severity, status, reported_at, resolved_at, reported_by)
VALUES (2, 'PIPELINE', 1, 1, 'Leak on North Main Line near market', 'MEDIUM', 'OPEN', '2026-10-10 08:45:00', NULL, 'Operator')
ON CONFLICT (id) DO NOTHING;

INSERT INTO maintenance_tasks (id, fault_id, asset_type, asset_id, zone_id, title, task_type, assigned_to,
                               scheduled_date, completed_date, status, cost, remarks)
VALUES (1, 1, 'PUMP', 3, 1, 'Replace Pump P-3 motor', 'REPAIR', 'Suresh', '2026-10-10', NULL, 'PENDING', NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- Move the id counters past the ids used above, so new rows do not clash with them.
SELECT setval(pg_get_serial_sequence('pumps', 'id'), (SELECT MAX(id) FROM pumps));
SELECT setval(pg_get_serial_sequence('treatment_plants', 'id'), (SELECT MAX(id) FROM treatment_plants));
SELECT setval(pg_get_serial_sequence('faults', 'id'), (SELECT MAX(id) FROM faults));
SELECT setval(pg_get_serial_sequence('maintenance_tasks', 'id'), (SELECT MAX(id) FROM maintenance_tasks));
