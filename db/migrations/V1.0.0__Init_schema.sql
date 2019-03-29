CREATE TABLE users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  login_at datetime DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  password_hash varchar(255) NOT NULL,
  username varchar(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_users_username (username)
);

CREATE TABLE api_clients (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  client_id varchar(40) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  login_at datetime DEFAULT NULL,
  name varchar(20) NOT NULL,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_api_clients_client_id (client_id),
  FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE records (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  code varchar(40) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT NOW(),
  password_hash varchar(32) DEFAULT NULL,
  text varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_records_code (code)
);

