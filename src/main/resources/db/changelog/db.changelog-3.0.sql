--liquibase formatted sql

--changeset vmamatsiuk:1
ALTER TABLE users
DROP COLUMN time_of_sending_verification_link,
DROP COLUMN activation_code;

--changeset vmamatsiuk:2
ALTER TABLE users
DROP COLUMN email_verified;