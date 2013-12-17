package com.glue.webapp.db;

import java.sql.SQLException;

public interface IDAO<T> {

	
	T create(T obj) throws SQLException;
	
	void update(T obj) throws SQLException;
	
	void delete(long id) throws SQLException;

}