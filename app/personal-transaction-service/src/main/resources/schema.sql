-- AccountType table
CREATE TABLE IF NOT EXISTS account_type (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    code VARCHAR(50) UNIQUE NOT NULL
    );

-- Nickname table
CREATE TABLE IF NOT EXISTS nickname (
    id BIGSERIAL PRIMARY KEY,
    nickname VARCHAR(100) NOT NULL
    );

-- AccountDetail table
CREATE TABLE IF NOT EXISTS account_detail (
      id BIGSERIAL PRIMARY KEY,
      user_id BIGINT NOT NULL,
      account_number VARCHAR(50) UNIQUE NOT NULL,
      account_type_id BIGINT NOT NULL,
      group_id BIGINT,
      current_balance DECIMAL(15,2) DEFAULT 0.00,
      role_id BIGINT,
      nickname_id BIGINT,
      kyc_id BIGINT,
      created_at VARCHAR(255) DEFAULT TO_CHAR(NOW(), 'YYYY-MM-DD HH24:MI:SS'),
      updated_at VARCHAR(255) DEFAULT TO_CHAR(NOW(), 'YYYY-MM-DD HH24:MI:SS'),
      CONSTRAINT fk_acc_type FOREIGN KEY (account_type_id) REFERENCES account_type(id),
      CONSTRAINT fk_nickname FOREIGN KEY (nickname_id) REFERENCES nickname(id)
    );

-- Transaction table
CREATE TABLE IF NOT EXISTS transactions (
    id BIGSERIAL PRIMARY KEY,
    credit_account_id BIGINT NOT NULL,
    debit_account_id BIGINT NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    created_at VARCHAR(255) DEFAULT TO_CHAR(NOW(), 'YYYY-MM-DD HH24:MI:SS'),
    updated_at VARCHAR(255) DEFAULT TO_CHAR(NOW(), 'YYYY-MM-DD HH24:MI:SS'),
    CONSTRAINT fk_tx_credit FOREIGN KEY (credit_account_id) REFERENCES account_detail(id),
    CONSTRAINT fk_tx_debit FOREIGN KEY (debit_account_id) REFERENCES account_detail(id)
    );

CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    profile_id BIGINT,
    role_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by BIGINT,
    updated_by BIGINT
    );

CREATE INDEX IF NOT EXISTS idx_account_detail_user_id ON account_detail(user_id);
CREATE INDEX IF NOT EXISTS idx_account_detail_nickname_id ON account_detail(nickname_id);
CREATE INDEX IF NOT EXISTS idx_bank_tx_status ON transactions(status);