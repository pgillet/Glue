-- Database and user
CREATE DATABASE gluedb;
ALTER DATABASE gluedb charset=utf8;
CREATE USER 'glue'@'localhost' IDENTIFIED BY 'glue';
GRANT ALL ON gluedb.* to 'glue'@'localhost';
USE gluedb;

-- Tables

-- User table
CREATE TABLE GLUE_USER (
			id BIGINT NOT NULL AUTO_INCREMENT,
			name VARCHAR(50) NOT NULL,
            email VARCHAR(50) NOT NULL,
            passwd VARCHAR(50) NOT NULL,
            PRIMARY KEY (id),
            UNIQUE (email)
			);
			
-- VENUE table
CREATE TABLE VENUE (
			id BIGINT NOT NULL AUTO_INCREMENT,
			name VARCHAR(100) DEFAULT NULL,
			address VARCHAR(255),
			latitude DECIMAL(10,8),
            longitude DECIMAL(10,8),
            url VARCHAR(2048),
            PRIMARY KEY(id),
            UNIQUE (address)
			);

-- Stream table
CREATE TABLE STREAM (
             id BIGINT NOT NULL AUTO_INCREMENT,
             title VARCHAR(200) NOT NULL,
             description VARCHAR(2000),
             url VARCHAR(2048),
             public BOOL NOT NULL,
             open BOOL NOT NULL,
             secret_question VARCHAR(100),
             secret_Answer VARCHAR(100),
             request_to_participate BOOL NOT NULL,
             start_date BIGINT UNSIGNED NOT NULL,
             end_date BIGINT UNSIGNED,
             thumb_path VARCHAR(255),
			 price VARCHAR(30),
			 category ENUM(  'OTHER', 'MUSIC', 'PERFORMING_ART', 'EXHIBITION', 'SPORT' , 'CONFERENCE') NOT NULL,
			 venue_id BIGINT,
             PRIMARY KEY (id),
             FOREIGN KEY (venue_id) REFERENCES VENUE(id) 
             		ON DELETE SET NULL
             );
			  
-- TAG table
CREATE TABLE TAG (
             id BIGINT NOT NULL AUTO_INCREMENT,
             tag VARCHAR(50) NOT NULL,
             PRIMARY KEY (id),
             UNIQUE(tag)
             );

-- TAG table
CREATE TABLE STREAM_TAG (
             id BIGINT NOT NULL AUTO_INCREMENT,
             tag_id BIGINT NOT NULL,
             stream_id BIGINT NOT NULL,
             PRIMARY KEY (id),
             FOREIGN KEY (tag_id) REFERENCES TAG(id)
                     ON DELETE CASCADE,
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
            UNIQUE(email, stream_id),
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
			UNIQUE(user_id, stream_id),
			FOREIGN KEY (user_id) REFERENCES GLUE_USER(id),
            FOREIGN KEY (stream_id) REFERENCES STREAM(id)
                     ON DELETE CASCADE
			);
			
-- MEDIA table
CREATE TABLE MEDIA (
			id BIGINT NOT NULL AUTO_INCREMENT,
			user_id BIGINT,
			stream_id BIGINT NOT NULL,
			external BOOL NOT NULL,
			url VARCHAR(100),
			extension VARCHAR(5),
			mime_type ENUM('Audio','Picture','Text','Video') NOT NULL,
			caption VARCHAR(50),
			latitude DECIMAL(10,8),
            longitude DECIMAL(10,8),
            creation_date BIGINT,
			PRIMARY KEY (id),
			FOREIGN KEY (user_id) REFERENCES GLUE_USER(id),
			FOREIGN KEY (stream_id) REFERENCES STREAM(id)
                     ON DELETE CASCADE
			);
	
-- VIEWS
CREATE OR REPLACE VIEW STREAM_VIEW AS
  SELECT STREAM.id, title, public, open, thumb_path, count(*) as nb_of_participant
  FROM STREAM
  INNER JOIN PARTICIPANT
  ON STREAM.id = PARTICIPANT.stream_id
  GROUP BY PARTICIPANT.stream_id;
  
  
  