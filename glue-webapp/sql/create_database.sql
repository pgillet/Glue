-- Drop database and user
DROP database gluedb;
DROP USER 'glue'@'localhost';

-- Database and user
CREATE database gluedb;
CREATE USER 'glue'@'localhost' IDENTIFIED BY 'glue';
GRANT ALL ON gluedb.* to 'glue'@'localhost';
USE gluedb;

-- Tables

-- User table
-- CREATE TABLE user (
-- );

-- Stream table
CREATE TABLE stream (
             id BIGINT NOT NULL AUTO_INCREMENT,
             title VARCHAR(30) NOT NULL,
             description VARCHAR(255),
             public BOOL NOT NULL,
             open BOOL NOT NULL,
             secretQuestion VARCHAR(100) DEFAULT NULL,
             secretAnswer VARCHAR(100) DEFAULT NULL,
             requestToParticipate BOOL NOT NULL,
             startDate BIGINT UNSIGNED DEFAULT NULL,
             endDate BIGINT UNSIGNED DEFAULT NULL,
             latitude DECIMAL(10,8) DEFAULT NULL,
             longitude DECIMAL(10,8) DEFAULT NULL,
             address VARCHAR(255) DEFAULT NULL,
             PRIMARY KEY (id)
             );