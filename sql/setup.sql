-- FILE: sql/setup.sql
-- Description: SQL script to create the necessary tables for the BlueVerse Bank application.
-- Designed for PostgreSQL.

-- --------------------------------------------------------
-- Table: account
-- Stores user login credentials and current balance.
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS account (
    user_name VARCHAR(50) PRIMARY KEY,
    password VARCHAR(4) NOT NULL,
    balance INT NOT NULL
    -- NOTE: You could add CHECK (balance >= 0) for extra data integrity
);

-- --------------------------------------------------------
-- Table: transaction
-- Stores a history of all deposits and withdrawals.
-- --------------------------------------------------------
CREATE TABLE IF NOT EXISTS transaction (
    transaction_id SERIAL PRIMARY KEY,
    user_name VARCHAR(50) NOT NULL,
    amount INT NOT NULL,
    transaction_type VARCHAR(10) NOT NULL, -- e.g., 'DEPOSIT', 'WITHDRAW'
    transaction_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key Constraint: Links transactions to a valid user account.
    CONSTRAINT fk_user_name
        FOREIGN KEY (user_name)
        REFERENCES account(user_name)
        ON DELETE CASCADE
);

-- Optional: Insert a sample account for quick testing
INSERT INTO account (user_name, password, balance) VALUES
('testuser', '1234', 5000)
ON CONFLICT (user_name) DO NOTHING;