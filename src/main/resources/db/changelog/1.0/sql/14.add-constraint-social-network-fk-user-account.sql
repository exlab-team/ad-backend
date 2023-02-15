-- liquibase formatted sql

-- changeset liquibase:14
alter TABLE user_accounts_social_networks ADD CONSTRAINT user_accounts_fk_social_networks FOREIGN KEY (user_account_id) REFERENCES user_accounts(id)