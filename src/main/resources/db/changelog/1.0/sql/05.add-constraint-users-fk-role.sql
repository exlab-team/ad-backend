-- liquibase formatted sql

-- changeset liquibase:5
alter TABLE users_roles ADD CONSTRAINT users_roles_fk_roles FOREIGN KEY(role_id) REFERENCES roles(id)