SET foreign_key_checks = 0;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS connections;
SET foreign_key_checks = 1;

CREATE TABLE users ( 
id bigint NOT NULL AUTO_INCREMENT, 
email varchar(50) NOT NULL, 
hash varchar(150) NOT NULL, 
role varchar(150) NOT NULL,
PRIMARY KEY (id), 
UNIQUE KEY email (email) 
) AUTO_INCREMENT=1;

CREATE TABLE connections (
  lowerId bigint,
  higherId bigint,
  PRIMARY KEY (lowerId, higherId),
  FOREIGN KEY (lowerId) REFERENCES users (id),
  FOREIGN KEY (higherId) REFERENCES users (id),
  CHECK (lowerId < higherId)
);

/* insert admin user with password adminpw */
INSERT INTO users (email, hash, role) VALUES ('admin@test.io','5b4f8788e3d7abbe847db2ac651aec742ecd142b6692e850b9bab3:2a7046a7af334f4e26e033a4b178b4a39ff591a22e19307ff08259','ADMIN');