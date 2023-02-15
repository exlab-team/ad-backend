-- liquibase formatted sql

-- changeset liquibase:6
create table if not exists advisory_materials (
  id bigint not null,
  topic varchar(45) not null,
  content text not null,
  constraint advisory_materials_pk primary key(id)
 );

