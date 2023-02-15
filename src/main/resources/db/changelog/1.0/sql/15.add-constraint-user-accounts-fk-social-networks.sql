-- liquibase formatted sql

-- changeset liquibase:15
alter TABLE user_accounts_social_networks ADD CONSTRAINT social_networks_fk_user_accounts FOREIGN KEY (social_network_id) REFERENCES social_networks(id)