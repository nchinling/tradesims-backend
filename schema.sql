create table accounts(
account_id VARCHAR(10) NOT NULL PRIMARY KEY,
name VARCHAR(50) NOT NULL,
username VARCHAR(50) NOT NULL,
email VARCHAR(50) NOT NULL UNIQUE,
password VARCHAR(50) NOT NULL
);