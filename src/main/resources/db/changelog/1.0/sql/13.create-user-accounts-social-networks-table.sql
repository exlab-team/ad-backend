-- liquibase formatted sql

-- changeset liquibase:13
create table if not exists user_accounts_social_networks (
 social_network_id bigint not null,
 user_account_id bigint not null
 );