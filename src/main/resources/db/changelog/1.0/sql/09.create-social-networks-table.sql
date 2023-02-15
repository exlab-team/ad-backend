-- liquibase formatted sql

-- changeset liquibase:9
create table if not exists social_networks (
  id bigint not null,
  name varchar(50) not null,
  link varchar(150) not null,
  constraint social_networks_pk primary key(id)
 );
