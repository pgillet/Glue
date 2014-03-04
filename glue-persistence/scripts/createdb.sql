-- Database and user
CREATE DATABASE gluedb;
ALTER DATABASE gluedb charset=utf8;
CREATE USER 'glue'@'localhost' IDENTIFIED BY 'glue';
GRANT ALL ON gluedb.* to 'glue'@'localhost';
USE gluedb;