-- liquibase formatted sql

-- changeset liquibase:1
create table if not exists roles (
  id bigint not null,
  name VARCHAR(40) NOT NULL,
  constraint roles_pk primary key(id)
 );

