CREATE KEYSPACE IF NOT EXISTS student_wallet
WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

USE student_wallet;

CREATE TABLE IF NOT EXISTS budget_cycles (
    cycle_id UUID PRIMARY KEY,
    user_id TEXT,
    start_date DATE,
    end_date DATE,
    total_income DECIMAL,
    daily_base_budget DECIMAL,
    cycle_name TEXT
);

CREATE TABLE IF NOT EXISTS expenses (
    expense_id UUID PRIMARY KEY,
    user_id TEXT,
    cycle_id UUID,
    expense_date DATE,
    amount DECIMAL,
    category TEXT,
    description TEXT,
    is_essential BOOLEAN,
    created_at TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_expenses_user_date ON expenses (user_id);
CREATE INDEX IF NOT EXISTS idx_expenses_expense_date ON expenses (expense_date);

CREATE TABLE IF NOT EXISTS daily_aggregates (
    aggregate_id UUID PRIMARY KEY,
    user_id TEXT,
    cycle_id UUID,
    date DATE,
    total_spent DECIMAL
);

CREATE TABLE IF NOT EXISTS expenses_by_user_cycle (
    user_id TEXT,
    cycle_id UUID,
    expense_date DATE,
    expense_id TIMEUUID,
    amount DECIMAL,
    category TEXT,
    description TEXT,
    is_essential BOOLEAN,
    PRIMARY KEY ((user_id, cycle_id), expense_date, expense_id)
) WITH CLUSTERING ORDER BY (expense_date DESC);

CREATE TABLE IF NOT EXISTS daily_aggregates_by_user (
    user_id TEXT,
    cycle_id UUID,
    date DATE,
    total_spent DECIMAL,
    PRIMARY KEY ((user_id, cycle_id), date)
) WITH CLUSTERING ORDER BY (date DESC);

CREATE TABLE IF NOT EXISTS gamification_profiles (
    user_id TEXT PRIMARY KEY,
    display_name TEXT,
    current_streak INT,
    longest_streak INT,
    last_under_budget_date DATE,
    earned_badges LIST<TEXT>,
    savings_vault DECIMAL,
    total_daily_savings DECIMAL
);

CREATE TABLE IF NOT EXISTS users (
    user_id TEXT PRIMARY KEY,
    email TEXT,
    password_hash TEXT,
    display_name TEXT,
    created_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS users_by_email (
    email TEXT PRIMARY KEY,
    user_id TEXT
);
