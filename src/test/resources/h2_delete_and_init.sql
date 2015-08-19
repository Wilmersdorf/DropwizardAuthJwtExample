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
INSERT INTO users (email, hash, role) VALUES ('admin@test.io','518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af','ADMIN');