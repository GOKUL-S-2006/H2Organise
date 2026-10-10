-- seed data for the demo, ids must match what the whole team agreed on
-- ON CONFLICT DO NOTHING so the app doesn't crash when we restart it

-- zone 1 (North) is CRITICAL because that's our demo scenario
-- east and west have no source yet, resource-service person can add later
INSERT INTO supply_zones (id, name, description, population, households, primary_source_id, status) VALUES
(1, 'North Zone', 'Northern residential and industrial area', 85000, 18000, 1, 'CRITICAL'),
(2, 'South Zone', 'Southern residential area', 62000, 13000, 2, 'NORMAL'),
(3, 'East Zone', 'Eastern residential area', 48000, 10000, NULL, 'NORMAL'),
(4, 'West Zone', 'Western commercial area', 41000, 8500, NULL, 'NORMAL')
ON CONFLICT (id) DO NOTHING;

-- north main line is leaking, this shows up on the zone status page
INSERT INTO pipelines (id, name, zone_id, source_id, length_km, diameter_mm, material, installed_date, status) VALUES
(1, 'North Main Line', 1, 1, 12.5, 600, 'Iron', '2012-04-10', 'LEAKING'),
(2, 'South Main Line', 2, 2, 9.0, 450, 'PVC', '2016-08-21', 'ACTIVE'),
(3, 'East Distribution Line', 3, NULL, 7.2, 300, 'PVC', '2018-02-15', 'ACTIVE'),
(4, 'West Distribution Line', 4, NULL, 6.8, 300, 'Steel', '2015-11-05', 'ACTIVE')
ON CONFLICT (id) DO NOTHING;

-- planned_volume is in ML (million litres)
INSERT INTO distribution_schedules (id, zone_id, supply_date, start_time, end_time, planned_volume, status, remarks) VALUES
(1, 1, '2026-10-10', '18:00:00', '20:00:00', 12.0, 'PLANNED', 'Evening supply'),
(2, 2, '2026-10-10', '06:00:00', '08:00:00', 10.0, 'PLANNED', 'Morning supply'),
(3, 3, '2026-10-10', '07:00:00', '09:00:00', 8.0, 'PLANNED', 'Morning supply'),
(4, 4, '2026-10-10', '17:00:00', '19:00:00', 7.0, 'PLANNED', 'Evening supply')
ON CONFLICT (id) DO NOTHING;

INSERT INTO consumption_records (id, zone_id, record_date, category, volume_consumed) VALUES
(1, 1, '2026-10-09', 'DOMESTIC', 38.0),
(2, 1, '2026-10-09', 'INDUSTRIAL', 17.0),
(3, 2, '2026-10-09', 'DOMESTIC', 30.0),
(4, 2, '2026-10-09', 'INSTITUTIONAL', 8.0)
ON CONFLICT (id) DO NOTHING;

-- we inserted ids by hand, so move the auto-id counters forward
-- otherwise the first new record created from the app would clash with id 1
SELECT setval(pg_get_serial_sequence('supply_zones', 'id'), (SELECT MAX(id) FROM supply_zones));
SELECT setval(pg_get_serial_sequence('pipelines', 'id'), (SELECT MAX(id) FROM pipelines));
SELECT setval(pg_get_serial_sequence('distribution_schedules', 'id'), (SELECT MAX(id) FROM distribution_schedules));
SELECT setval(pg_get_serial_sequence('consumption_records', 'id'), (SELECT MAX(id) FROM consumption_records));
