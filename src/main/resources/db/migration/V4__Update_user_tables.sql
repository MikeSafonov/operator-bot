ALTER TABLE internal_users
ADD COLUMN role INT NOT NULL;

RENAME TABLE internal_users TO users;

DROP TABLE external_users;