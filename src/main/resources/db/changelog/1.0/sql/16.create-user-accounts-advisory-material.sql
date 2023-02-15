-- liquibase formatted sql

-- changeset liquibase:16
create table if not exists user_accounts_advisory_materials (
 advisory_material_id bigint not null,
 user_account_id bigint not null
 );
