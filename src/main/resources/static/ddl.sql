CREATE TABLE client (
    client_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    tax_code VARCHAR(16) UNIQUE NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
     iban VARCHAR(27) PRIMARY KEY,
     client_id INTEGER NOT NULL REFERENCES client(client_id),
     ledger_balance NUMERIC(15, 2) DEFAULT 0.00 NOT NULL,
     available_balance NUMERIC(15, 2) DEFAULT 0.00 NOT NULL,
     status VARCHAR(50) DEFAULT 'ACTIVE' NOT NULL,
     opening_date DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TABLE transaction (
     transaction_id BIGSERIAL PRIMARY KEY,
     account_iban VARCHAR(27) NOT NULL REFERENCES account(iban),
     event_timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
     amount NUMERIC(15, 2) NOT NULL,
     transaction_type VARCHAR(100) NOT NULL,
     description TEXT
);

CREATE INDEX idx_transaction_iban ON transaction(account_iban);