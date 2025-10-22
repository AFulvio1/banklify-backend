CREATE TABLE client (
    client_id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    tax_code VARCHAR(16) UNIQUE NOT NULL, -- Corrisponde a Codice Fiscale
    email VARCHAR(150) UNIQUE NOT NULL,
    -- La password DEVE essere salvata come HASH (es. BCrypt)
    password_hash VARCHAR(255) NOT NULL,
    registration_date TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account (
     iban VARCHAR(27) PRIMARY KEY, -- IBAN come chiave primaria
     client_id INTEGER NOT NULL REFERENCES client(client_id),
     -- Utilizzo di NUMERIC per precisione monetaria
     ledger_balance NUMERIC(15, 2) DEFAULT 0.00 NOT NULL, -- Saldo Contabile
     available_balance NUMERIC(15, 2) DEFAULT 0.00 NOT NULL, -- Saldo Disponibile
     status VARCHAR(50) DEFAULT 'ACTIVE' NOT NULL, -- ES. ACTIVE, BLOCKED, CLOSED
     opening_date DATE DEFAULT CURRENT_DATE NOT NULL
);

CREATE TABLE transaction (
     transaction_id BIGSERIAL PRIMARY KEY,
     account_iban VARCHAR(27) NOT NULL REFERENCES account(iban),
     timestamp TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP NOT NULL,
     -- Importo con segno: positivo per accredito, negativo per addebito
     amount NUMERIC(15, 2) NOT NULL,
     transaction_type VARCHAR(100) NOT NULL, -- Es. 'INCOMING_TRANSFER', 'OUTGOING_TRANSFER', 'WITHDRAWAL', 'SALARY_CREDIT'
     description TEXT -- Corrisponde a Causale
);

-- Indice per velocizzare le query per IBAN
CREATE INDEX idx_transaction_iban ON transaction(account_iban);