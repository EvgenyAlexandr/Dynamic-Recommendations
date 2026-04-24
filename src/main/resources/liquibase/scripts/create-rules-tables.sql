-- liquibase formatted sql

-- changeset author EA:1
CREATE TABLE IF NOT EXISTS rules (
    id UUID PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    product_id UUID NOT NULL,
    product_text TEXT NOT NULL
);

-- changeset author EA:2
CREATE TABLE IF NOT EXISTS queries (
    id BIGSERIAL PRIMARY KEY,
    rule_id UUID NOT NULL REFERENCES rules(id) ON DELETE CASCADE,
    query VARCHAR(50) NOT NULL,
    negate BOOLEAN NOT NULL
);

-- changeset author EA:3
CREATE TABLE IF NOT EXISTS query_arguments (
    query_id BIGINT NOT NULL REFERENCES queries(id) ON DELETE CASCADE,
    argument VARCHAR(255) NOT NULL
);