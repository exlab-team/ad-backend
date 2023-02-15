-- liquibase formatted sql

-- changeset liquibase:11
alter TABLE user_accounts ADD CONSTRAINT user_accounts_fk_users FOREIGN KEY (user_id) REFERENCES users(id)