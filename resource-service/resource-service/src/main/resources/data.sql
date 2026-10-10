-- seed data for the demo, ids and zone ids must match what the whole team agreed on
-- ON CONFLICT DO NOTHING so the app doesn't crash when we restart it

-- source 1 serves North Zone (zone 1), source 2 serves South Zone (zone 2)
-- zone_id is just a number from distribution-service (no foreign key)
-- total_capacity is in ML (million litres)
INSERT INTO water_sources (id, name, type, location, latitude, longitude, total_capacity, min_threshold_percent, zone_id, status, commissioned_date) VALUES
(1, 'Hemavathi Reservoir', 'RESERVOIR', 'North Zone, main dam road', 13.050000, 77.590000, 500.0, 30.0, 1, 'ACTIVE', '2005-06-15'),
(2, 'South Lake', 'LAKE', 'South Zone, lake road', 12.900000, 77.580000, 320.0, 30.0, 2, 'ACTIVE', '2001-03-20'),
(3, 'East Zone Tank', 'TANK', 'East Zone, ward 12', 12.980000, 77.750000, 80.0, 30.0, 3, 'ACTIVE', '2015-09-01'),
(4, 'West Zone Borewell', 'WELL', 'West Zone, industrial area', 12.970000, 77.500000, 40.0, 30.0, 4, 'ACTIVE', '2018-01-10')
ON CONFLICT (id) DO NOTHING;

-- readings for source 1 go down over the week, 22% today is below the 30% limit
-- that's what makes North Zone CRITICAL in the demo
-- level_percent = current_volume / total_capacity * 100, inflow and outflow in ML per day
INSERT INTO level_readings (id, source_id, reading_time, current_volume, level_percent, inflow_rate, outflow_rate, recorded_by) VALUES
(1, 1, '2026-10-10 06:00:00', 110.0, 22.0, 40.0, 75.0, 'Operator'),
(2, 2, '2026-10-10 06:00:00', 217.6, 68.0, 55.0, 50.0, 'Operator'),
(3, 1, '2026-10-07 06:00:00', 200.0, 40.0, 60.0, 62.0, 'Operator'),
(4, 1, '2026-10-08 06:00:00', 165.0, 33.0, 55.0, 70.0, 'Operator'),
(5, 1, '2026-10-09 06:00:00', 135.0, 27.0, 50.0, 72.0, 'Operator'),
(6, 3, '2026-10-10 06:00:00', 56.0, 70.0, 12.0, 10.0, 'Operator'),
(7, 4, '2026-10-10 06:00:00', 22.0, 55.0, 6.0, 5.0, 'Operator')
ON CONFLICT (id) DO NOTHING;

-- we inserted ids by hand, so move the auto-id counters forward
-- otherwise the first new record created from the app would clash with id 1
SELECT setval(pg_get_serial_sequence('water_sources', 'id'), (SELECT MAX(id) FROM water_sources));
SELECT setval(pg_get_serial_sequence('level_readings', 'id'), (SELECT MAX(id) FROM level_readings));
