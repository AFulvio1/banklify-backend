INSERT INTO client (client_id, first_name, last_name, tax_code, email, password_hash, registration_date) VALUES
    (1, 'John', 'Doe', 'DOEJHN80A01H501X', 'john.doe@example.com', '$2a$10$dummyhashforjohn', '2024-10-01 09:15:00'),
    (2, 'Emily', 'Smith', 'SMIEMI85B02L219Y', 'emily.smith@example.com', '$2a$10$dummyhashforemily', '2024-11-05 14:30:00'),
    (3, 'Michael', 'Brown', 'BRNMIC90C03Z404Z', 'michael.brown@example.com', '$2a$10$dummyhashformichael', '2025-01-20 08:00:00');

INSERT INTO account (iban, client_id, ledger_balance, available_balance, status, opening_date) VALUES
    ('IT60AAAA0000000000000000001', 1, 1200.00, 1200.00, 'ACTIVE', '2024-10-02'),
    ('IT60AAAA0000000000000000002', 1, 250.50, 250.50, 'ACTIVE', '2024-12-01'),
    ('IT60AAAA0000000000000000003', 2, 5000.00, 4500.00, 'ACTIVE', '2024-11-10'),
    ('IT60AAAA0000000000000000004', 3, 0.00, 0.00, 'CLOSED', '2025-01-25');

INSERT INTO transaction (transaction_id, account_iban, event_timestamp, amount, transaction_type, description) VALUES
    (1, 'IT60AAAA0000000000000000001', '2024-10-02 09:20:00', 1200.00, 'DEPOSIT', 'Initial deposit'),
    (2, 'IT60AAAA0000000000000000001', '2024-10-05 12:00:00', -50.00, 'WITHDRAWAL', 'ATM withdrawal'),
    (3, 'IT60AAAA0000000000000000002', '2024-12-15 16:45:00', 300.00, 'DEPOSIT', 'Salary deposit'),
    (4, 'IT60AAAA0000000000000000003', '2024-11-15 10:00:00', -500.00, 'TRANSFER', 'Transfer to external account'),
    (5, 'IT60AAAA0000000000000000003', '2024-11-20 08:30:00', -0.50, 'FEE', 'Monthly maintenance fee'),
    (6, 'IT60AAAA0000000000000000004', '2025-02-01 09:00:00', -10.00, 'FEE', 'Account closure fee'),
    (7, 'IT60AAAA0000000000000000002', '2025-01-01 00:00:00', -49.50, 'PURCHASE', 'Online purchase'),
    (8, 'IT60AAAA0000000000000000001', '2025-02-10 14:00:00', -200.00, 'TRANSFER', 'Transfer to account IT60AAAA0000000000000000002');
