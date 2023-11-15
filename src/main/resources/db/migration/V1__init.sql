CREATE TABLE users (
  id bigint PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL
);

create table product (
    id INTEGER PRIMARY KEY,
    calories_per_100_gram float(53) not null,
    weight_gram float(53) not null,
    brand varchar(255) not null,
    category varchar(255) not null,
    flavour varchar(255),
    name varchar(255) not null
);