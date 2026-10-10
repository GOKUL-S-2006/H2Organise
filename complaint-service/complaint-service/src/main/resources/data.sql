-- seed data for the demo, ids must match what the whole team agreed on
-- ON CONFLICT DO NOTHING so the app doesn't crash when we restart it
-- citizen_id 2 = Meena Iyer (auth-service), zone_id 1 = North Zone (distribution-service)
-- both are just numbers from other services (no foreign keys)

-- the leak complaint from the demo story
INSERT INTO complaints (id, citizen_id, zone_id, type, title, description, location, priority, status, assigned_to, created_at, resolved_at, resolution_note) VALUES
(101, 2, 1, 'LEAKAGE', 'Pipe burst near market', 'Water is flowing on the road near the market gate', 'Market Road, North Zone', 'HIGH', 'REGISTERED', NULL, '2026-10-10 09:00:00', NULL, NULL)
ON CONFLICT (id) DO NOTHING;

-- the tanker request that shows up on the zone status page
INSERT INTO supply_requests (id, citizen_id, zone_id, request_type, quantity_litres, required_date, reason, status, created_at) VALUES
(7, 2, 1, 'TANKER', 5000.0, '2026-10-11', 'Very low pressure for two days', 'PENDING', '2026-10-10 08:30:00')
ON CONFLICT (id) DO NOTHING;

-- first row of the history, this is what "My complaints" shows when tracking
INSERT INTO complaint_updates (id, complaint_id, status, comment, updated_by, updated_at) VALUES
(1, 101, 'REGISTERED', 'Complaint received', 'Meena Iyer', '2026-10-10 09:00:00')
ON CONFLICT (id) DO NOTHING;

-- we inserted ids by hand, so move the auto-id counters forward
-- otherwise the first new record created from the app would clash with the seed ids
SELECT setval(pg_get_serial_sequence('complaints', 'id'), (SELECT MAX(id) FROM complaints));
SELECT setval(pg_get_serial_sequence('supply_requests', 'id'), (SELECT MAX(id) FROM supply_requests));
SELECT setval(pg_get_serial_sequence('complaint_updates', 'id'), (SELECT MAX(id) FROM complaint_updates));
