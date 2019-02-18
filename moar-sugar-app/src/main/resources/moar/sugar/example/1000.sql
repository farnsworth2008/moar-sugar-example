CREATE TABLE pet (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20),
  owner VARCHAR(20),
  species VARCHAR(20),
  sex CHAR(1),
  birth DATE,
  death DATE,
  PRIMARY KEY (`id`),
  UNIQUE KEY name (name, owner)
);