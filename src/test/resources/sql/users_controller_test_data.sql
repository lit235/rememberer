insert into users (id, username, name, password_hash, created_at) VALUES
(1, "username1","name1", "{bcrypt}$2a$10$1AnEssg1Bts9ANHGkpOXZ.xACnQKApeIolvV6PZYzn0/I5lpF/Sle",NOW()),
(2, "username2","name2", "{bcrypt}$2a$10$1AnEssg1Bts9ANHGkpOXZ.xACnQKApeIolvV6PZYzn0/I5lpF/Sle",NOW()),
(3, "some_username","some_name", "{bcrypt}$2a$10$1AnEssg1Bts9ANHGkpOXZ.xACnQKApeIolvV6PZYzn0/I5lpF/Sle",NOW()),
(4, "some_username1","some_name", "{bcrypt}$2a$10$1AnEssg1Bts9ANHGkpOXZ.xACnQKApeIolvV6PZYzn0/I5lpF/Sle",NOW()),
(5, "username","name", "{bcrypt}$2a$10$1AnEssg1Bts9ANHGkpOXZ.xACnQKApeIolvV6PZYzn0/I5lpF/Sle",NOW());