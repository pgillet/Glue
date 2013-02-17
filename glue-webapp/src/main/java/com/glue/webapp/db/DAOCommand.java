package com.glue.webapp.db;

import java.sql.SQLException;

public interface DAOCommand<T> {

	public T execute(DAOManager DAOManager) throws SQLException;

}
