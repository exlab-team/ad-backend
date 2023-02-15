-- liquibase formatted sql

-- changeset liquibase:18
Alter TABLE user_accounts_advisory_materials ADD CONSTRAINT advisory_material_fk_user_accounts FOREIGN KEY (advisory_material_id) REFERENCES advisory_materials(id)