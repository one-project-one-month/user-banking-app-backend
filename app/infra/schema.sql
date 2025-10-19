-- Create the table for nickname
CREATE TABLE nicknames (
    id BIGSERIAL PRIMARY KEY,
    from_account_id BIGINT NOT NULL,
    to_account_id BIGINT NOT NULL,
    nickname VARCHAR NOT NULL,
    created_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE,
    created_by BIGINT,
    updated_by BIGINT,
    UNIQUE (from_account_id, to_account_id)
);

-- Create the table for recent transfers
CREATE TABLE users (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- accounts table
CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY,
  account_number VARCHAR(64) NOT NULL UNIQUE,
  balance DOUBLE PRECISION NOT NULL DEFAULT 0.0,
  name VARCHAR(255)
);

-- transfers table
CREATE TABLE transfers (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
  account_id BIGINT REFERENCES accounts(id) ON DELETE SET NULL,
  amount DOUBLE PRECISION NOT NULL DEFAULT 0.0,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

INSERT INTO users (name) VALUES ('Alice'), ('Bob');

INSERT INTO accounts (account_number, balance, name)
VALUES ('ACC-0001', 1000.50, 'Alice main'),
       ('ACC-0002',  200.00, 'Bob savings');

INSERT INTO transfers (user_id, account_id, amount, created_at)
VALUES (1, 1, 50.00, now() - interval '1 day'),
       (2, 2, 100.00, now() - interval '2 hours'),
       (1, 2, 25.00, now());
