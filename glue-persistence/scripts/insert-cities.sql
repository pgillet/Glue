LOAD DATA LOCAL INFILE 'cities.csv' INTO TABLE city
FIELDS TERMINATED BY ';' 
(id, departement, slug, name, simple_name, real_name, soundex, metaphone, postal_code, population, longitude, latitude);