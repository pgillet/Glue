package com.glue.webapp.db;


public interface IDAO<T> {

	
	T create(T obj) throws Exception;
	
	void update(T obj) throws Exception;
	
	void delete(long id) throws Exception;

}