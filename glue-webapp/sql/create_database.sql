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
CREATE TABLE GLUE_USER (
			id BIGINT NOT NULL AUTO_INCREMENT,
			first_name VARCHAR(50) NOT NULL,
			last_name VARCHAR(50) NOT NULL,
            email VARCHAR(50) NOT NULL,
            passwd VARCHAR(50) NOT NULL,
            PRIMARY KEY (id),
            UNIQUE (email)
			);

-- Stream table
CREATE TABLE STREAM (
             id BIGINT NOT NULL AUTO_INCREMENT,
             title VARCHAR(50) NOT NULL,
             public BOOL NOT NULL,
             open BOOL NOT NULL,
             secret_question VARCHAR(100),
             secret_Answer VARCHAR(100),
             request_to_participate BOOL NOT NULL,
             start_date BIGINT UNSIGNED NOT NULL,
             end_date BIGINT UNSIGNED,
             latitude DECIMAL(10,8),
             longitude DECIMAL(10,8),
             address VARCHAR(255),
             PRIMARY KEY (id)
             );
             
-- TAG table
CREATE TABLE TAG (
             id BIGINT NOT NULL AUTO_INCREMENT,
             tag VARCHAR(50) NOT NULL,
             stream_id BIGINT NOT NULL,
             PRIMARY KEY (id),
             INDEX (tag),
             FOREIGN KEY (stream_id) REFERENCES STREAM(id)
                     ON DELETE CASCADE
             );
             
-- Invited table
CREATE TABLE INVITED (
			id BIGINT NOT NULL AUTO_INCREMENT,
			name VARCHAR(50) DEFAULT NULL,
			email VARCHAR(50) NOT NULL,
            stream_id BIGINT NOT NULL,
            PRIMARY KEY (id),
            FOREIGN KEY (stream_id) REFERENCES STREAM(id)
                     ON DELETE CASCADE
			);
			
-- PARTICIPANT table
CREATE TABLE PARTICIPANT (
			id BIGINT NOT NULL AUTO_INCREMENT,
			user_id BIGINT NOT NULL,
			stream_id BIGINT NOT NULL,
			admin BOOL NOT NULL,
			PRIMARY KEY (id),
			FOREIGN KEY (user_id) REFERENCES GLUE_USER(id),
            FOREIGN KEY (stream_id) REFERENCES STREAM(id)
                     ON DELETE CASCADE
			);
			
			
-- MEDIA table
CREATE TABLE MEDIA (
			id BIGINT NOT NULL AUTO_INCREMENT,
			user_id BIGINT NOT NULL,
			stream_id BIGINT NOT NULL,
			extension VARCHAR(5) NOT NULL,
			mime_type ENUM('Audio','Picture','Text') NOT NULL,
			caption VARCHAR(50),
			latitude DECIMAL(10,8),
            longitude DECIMAL(10,8),
            start_date BIGINT UNSIGNED NOT NULL,
			PRIMARY KEY (id),
			FOREIGN KEY (user_id) REFERENCES GLUE_USER(id),
			FOREIGN KEY (stream_id) REFERENCES STREAM(id)
                     ON DELETE CASCADE
			);