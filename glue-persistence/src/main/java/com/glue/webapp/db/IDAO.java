package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDAO {

	/**
	 * @return the connection
	 */
	Connection getConnection();

	/**
	 * @param connection the connection to set
	 */
	void setConnection(Connection connection) throws SQLException;

}