insert into users (id, username, name, password_hash, created_at) VALUES
(1, "admin","admin", "{bcrypt}$2a$10$1AnEssg1Bts9ANHGkpOXZ.xACnQKApeIolvV6PZYzn0/I5lpF/Sle",NOW());

insert into api_clients (id, name, client_id, created_at, user_id) VALUES
(1, "ui", "ui", NOW(), 1);