-- liquibase formatted sql

-- changeset liquibase:3
create table if not exists users_roles (
 role_id bigint not null,
  user_id bigint not null
 );
