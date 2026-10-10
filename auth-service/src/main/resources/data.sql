-- seed users for the demo, ids must match what the whole team agreed on
-- ON CONFLICT DO NOTHING so the app doesn't crash when we restart it
-- every demo account has the password:  password123
-- (password_hash is the BCrypt hash of that password, we never store the real password)
-- zone_id 1 = North Zone from distribution-service, just a number (no foreign key)

INSERT INTO users (id, full_name, email, password_hash, phone, role, zone_id, active, created_at) VALUES
(1, 'Ravi Kumar', 'ravi@aqua.in', '$2a$10$7D4S8XFousWv4eB4FQihee6ssDNeHAuDOiA8mp/e4JXCVffhvEuR.', '9876543210', 'ENGINEER', 1, true, '2026-10-01 10:00:00'),
(2, 'Meena Iyer', 'meena@mail.com', '$2a$10$7D4S8XFousWv4eB4FQihee6ssDNeHAuDOiA8mp/e4JXCVffhvEuR.', '9876500002', 'CITIZEN', 1, true, '2026-10-01 10:00:00'),
(3, 'Admin', 'admin@aqua.in', '$2a$10$7D4S8XFousWv4eB4FQihee6ssDNeHAuDOiA8mp/e4JXCVffhvEuR.', '9876500003', 'ADMIN', NULL, true, '2026-10-01 10:00:00'),
(4, 'Operator One', 'operator@aqua.in', '$2a$10$7D4S8XFousWv4eB4FQihee6ssDNeHAuDOiA8mp/e4JXCVffhvEuR.', '9876500004', 'OPERATOR', 1, true, '2026-10-01 10:00:00')
ON CONFLICT (id) DO NOTHING;

-- we inserted ids by hand, so move the auto-id counter forward
-- otherwise the first user registered from the app would clash with id 1
SELECT setval(pg_get_serial_sequence('users', 'id'), (SELECT MAX(id) FROM users));
