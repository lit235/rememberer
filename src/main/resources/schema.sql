CREATE TABLE IF NOT EXISTS api_clients (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  client_id varchar(40) NOT NULL,
  created_at datetime NOT NULL,
  login_at datetime DEFAULT NULL,
  name varchar(20) NOT NULL,
  user_id bigint(20) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_api_clients_client_id (client_id),
  FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS records (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  code varchar(40) NOT NULL,
  created_at datetime NOT NULL,
  password_hash varchar(32) DEFAULT NULL,
  text varchar(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_records_code (code)
);


CREATE TABLE IF NOT EXISTS users (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  created_at datetime NOT NULL,
  login_at datetime DEFAULT NULL,
  name varchar(255) DEFAULT NULL,
  password_hash varchar(255) NOT NULL,
  username varchar(20) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY UK_users_username (username)
);

