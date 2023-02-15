-- liquibase formatted sql

-- changeset liquibase:2
create table if not exists users (
  id bigint not null,
  username varchar(50) not null,
  password varchar(60) not null,
  email varchar(70) not null unique,
  confirmed boolean not null,
  created_at timestamp,
  time_of_sending_the_confirmation_link timestamp,
  activation_code varchar(50),
  constraint users_pk primary key(id)
 );

