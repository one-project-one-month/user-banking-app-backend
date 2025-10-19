-- DROP TABLE IF THEY EXISTS
DROP TABLE IF EXISTS "role" CASCADE;
DROP TABLE IF EXISTS "user" CASCADE;
DROP TABLE IF EXISTS "group" CASCADE;
DROP TABLE IF EXISTS "account_type" CASCADE;

DROP TABLE IF EXISTS "account_detail" CASCADE;


-- CREATE TABLE ROLE
CREATE TABLE "role" (
    "unique_id" SERIAL PRIMARY KEY,
    "role_type" VARCHAR(10) NOT NULL,
    "name" VARCHAR(100) NOT NULL
);

-- CREATE TABLE USER
CREATE TABLE "user" (
    "id" SERIAL PRIMARY KEY,
    "username" VARCHAR(100) NOT NULL UNIQUE,
    "email" VARCHAR(150) NOT NULL UNIQUE,
    "password" VARCHAR(255) NOT NULL,
    "profile_id" INT,
    "role_id" INT REFERENCES "role"("unique_id") ON DELETE SET NULL
);

-- CREATE TABLE GROUP
CREATE TABLE "group" (
    "id" SERIAL PRIMARY KEY,
    "name" VARCHAR(100) NOT NULL,
    "shortcode" VARCHAR(50) UNIQUE NOT NULL
);

-- CREATE TABLE ACCOUNT_TYPE
CREATE TABLE "account_type" (
    "id" SERIAL PRIMARY KEY,
    "code" VARCHAR(50) UNIQUE NOT NULL
);

-- CREATE TABLE ACCOUNT_DETAIL
CREATE TABLE "account_detail" (
    "id" SERIAL PRIMARY KEY,
    "account_number" VARCHAR(30) UNIQUE NOT NULL,
    "user_id" INT NOT NULL,
    "account_type_id" INT NOT NULL REFERENCES "account_type"("id") ON DELETE CASCADE,
    "group_id" INT NOT NULL REFERENCES "group"("id") ON DELETE CASCADE,
    "current_balance" NUMERIC(15, 2) DEFAULT 0.00
);

-- INSERT INTO GROUP
INSERT INTO "group" ("name", "shortcode") VALUES
    ('Corporate Banking', 'CORP'),
    ('Retail Banking', 'RETL');

--INSERT INTO ACCOUNT_TYPE
INSERT INTO "account_type" ("code") VALUES
    ('SAV'),   -- Savings Account
    ('CUR'),   -- Current Account
    ('FD'),    -- Fixed Deposit
    ('LOAN');  -- Loan Account

--INSERT INTO ROLE
INSERT INTO "role" ("role_type", "name") VALUES
    ('USER', 'User'),
    ('ADMIN', 'Administrator');


-- INSERT INTO USER
INSERT INTO "user" ("username", "email", "password", "profile_id", "role_id") VALUES
    ('john_doe', 'john.doe@example.com', 'hashed_password_1', 1, 1),
    ('jane_smith', 'jane.smith@example.com', 'hashed_password_2', 2, 1),
    ('admin_user', 'admin@example.com', 'hashed_password_3', 3, 2),
    ('sophia_kim', 'sophia.kim@example.com', 'hashed_password_4', 4, 1);

-- INSERT INTO ACCOUNTDETAIL
INSERT INTO "account_detail" (
    "account_number", "user_id", "account_type_id", "group_id", "current_balance"
) VALUES
      ('ACC010102', 1, 1, 2, 5000.00),
      ('ACC010202', 1, 2, 2, 15000.50),
      ('ACC010302', 1, 3, 1, 250000.00),
      ('ACC10004', 4, 4, 2, -20000.00);