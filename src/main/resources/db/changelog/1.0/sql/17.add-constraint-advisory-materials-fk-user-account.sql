-- liquibase formatted sql

-- changeset liquibase:17
alter TABLE user_accounts_advisory_materials ADD CONSTRAINT user_accounts_fk_advisory_materials FOREIGN KEY (user_account_id) REFERENCES user_accounts(id)