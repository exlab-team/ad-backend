--liquibase formatted sql

--changeset aplehanova:1
CREATE TABLE IF NOT EXISTS tariffs
(
    id SERIAL PRIMARY KEY,
    tariff_name VARCHAR(64) NOT NULL UNIQUE ,
    price FLOAT
);

--changeset aplehanova:2

ALTER TABLE user_accounts
ADD COLUMN tariff_id INT REFERENCES tariffs(id);