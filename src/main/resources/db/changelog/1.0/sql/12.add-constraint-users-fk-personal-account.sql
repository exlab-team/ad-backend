-- liquibase formatted sql

-- changeset liquibase:12
alter TABLE user_accounts ADD CONSTRAINT users_fk_user_accounts FOREIGN KEY (personal_account_id) REFERENCES personal_accounts(id)