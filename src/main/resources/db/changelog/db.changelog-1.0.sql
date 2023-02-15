-- liquibase formatted sql

-- changeset ynekhaichyk:1
create table if not exists roles (
  id bigint not null,
  role_name varchar(40) not null,
  constraint roles_pk primary key(id)
 );


-- changeset ynekhaichyk:2
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

 -- changeset ynekhaichyk:3
create table if not exists users_roles (
 role_id bigint not null,
  user_id bigint not null,

  constraint users_roles_fk_users FOREIGN KEY(user_id) REFERENCES users(id),
  constraint users_roles_fk_roles FOREIGN KEY(role_id) REFERENCES roles(id)
 );

-- changeset ynekhaichyk:4
create table if not exists advisory_materials (
  id bigint not null,
  topic varchar(45) not null,
  content text not null,
  constraint advisory_materials_pk primary key(id)
 );

-- changeset ynekhaichyk:5
create table if not exists card_data (
  id bigint not null,
  card_number varchar(16) not null,
  card_owner varchar(30) not null,
  cvv_code int not null,
  expiration_date varchar(5)not null,
  total_balance float,
  constraint card_data_pk primary key(id)
 );

-- changeset ynekhaichyk:6
create table if not exists personal_accounts (
  id bigint not null,
  account_number varchar(100) not null,
  constraint personal_accounts_pk primary key(id)
 );

-- changeset ynekhaichyk:7
create table if not exists social_networks (
  id bigint not null,
  name varchar(50) not null,
  link varchar(150) not null,
  constraint social_networks_pk primary key(id)
 );

-- changeset ynekhaichyk:8
create table if not exists user_accounts (
  id bigint not null,
  personal_account_id bigint not null,
  tariff varchar(30) not null,
  user_id bigint not null,
  constraint users_accounts_pk primary key(id),
  constraint user_accounts_fk_users FOREIGN KEY (user_id) REFERENCES users(id),
  constraint users_fk_user_accounts FOREIGN KEY (personal_account_id) REFERENCES personal_accounts(id)
 );

 -- changeset  ynekhaichyk:9
create table if not exists user_accounts_social_networks (
 social_network_id bigint not null,
 user_account_id bigint not null,
 CONSTRAINT user_accounts_fk_social_networks FOREIGN KEY (user_account_id) REFERENCES user_accounts(id),
 CONSTRAINT social_networks_fk_user_accounts FOREIGN KEY (social_network_id) REFERENCES social_networks(id)
 );

 -- changeset ynekhaichyk:10
create table if not exists user_accounts_advisory_materials (
 advisory_material_id bigint not null,
 user_account_id bigint not null,
 CONSTRAINT user_accounts_fk_advisory_materials FOREIGN KEY (user_account_id) REFERENCES user_accounts(id),
 CONSTRAINT advisory_material_fk_user_accounts FOREIGN KEY (advisory_material_id) REFERENCES advisory_materials(id)
 );
