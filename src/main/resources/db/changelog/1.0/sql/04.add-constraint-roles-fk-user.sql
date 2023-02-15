-- liquibase formatted sql

-- changeset liquibase:4
ALTER TABLE users_roles ADD CONSTRAINT users_roles_fk_users FOREIGN KEY (user_id) REFERENCES users(id)