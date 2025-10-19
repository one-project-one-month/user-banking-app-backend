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
