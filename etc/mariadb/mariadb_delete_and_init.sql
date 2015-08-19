SET foreign_key_checks = 0;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS connections;
SET foreign_key_checks = 1;

CREATE TABLE users ( 
id bigint NOT NULL AUTO_INCREMENT, 
email varchar(50) NOT NULL, 
hash varchar(150) NOT NULL, 
role ENUM('ADMIN','NORMAL') NOT NULL,
PRIMARY KEY (id), 
UNIQUE KEY email (email) 
) AUTO_INCREMENT=1;

CREATE TABLE connections (
  lowerId bigint,
  higherId bigint,
  PRIMARY KEY (lowerId, higherId),
  CONSTRAINT FOREIGN KEY (lowerId) REFERENCES users (id),
  CONSTRAINT FOREIGN KEY (higherId) REFERENCES users (id),
  CONSTRAINT CHECK (lowerId < higherId)
);