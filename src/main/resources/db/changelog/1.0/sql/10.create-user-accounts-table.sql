-- liquibase formatted sql

-- changeset liquibase:10
create table if not exists user_accounts (
  id bigint not null,
  personal_account_id bigint not null,
  tariff varchar(30) not null,
  user_id bigint not null,
  constraint users_accounts_pk primary key(id)
 );

