CREATE TABLE t_target (
    id SERIAL PRIMARY KEY,
    c_alias VARCHAR(32) UNIQUE NOT NULL
);

CREATE TABLE t_user_profile (
    id UUID PRIMARY KEY,
    c_name VARCHAR(32) NOT NULL,
    c_surname VARCHAR(32) NOT NULL,
    c_gender VARCHAR(10) CHECK (c_gender IN ('MALE', 'FEMALE')),
    c_birth_date DATE NOT NULL,
    c_weight DECIMAL(5,2) CHECK (c_weight > 10 AND c_weight < 500),
    c_height SMALLINT CHECK (c_height > 50 AND c_height < 300),
    c_activity_level DECIMAL(4,3) NOT NULL DEFAULT 1.2,
    id_target INTEGER REFERENCES t_target(id),
    c_weekly_budget DECIMAL(10,2) DEFAULT 0.0
);