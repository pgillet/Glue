package com.glue.webapp.db;


public interface DAOCommand<T> {

	public T execute(DAOManager manager) throws Exception;

}
