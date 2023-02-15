-- liquibase formatted sql

-- changeset liquibase:8
create table if not exists personal_accounts (
  id bigint not null,
  account_number varchar(100) not null,
  constraint personal_accounts_pk primary key(id)
 );
