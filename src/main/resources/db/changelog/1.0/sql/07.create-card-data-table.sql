-- liquibase formatted sql

-- changeset liquibase:7
create table if not exists card_data (
  id bigint not null,
  card_number varchar(16) not null,
  card_owner varchar(30) not null,
  cvv_code int not null,
  expiration_date varchar(5)not null,
  total_balance float,
  constraint card_data_pk primary key(id)
 );




