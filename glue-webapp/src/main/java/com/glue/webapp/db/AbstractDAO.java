package com.glue.webapp.db;

import java.sql.Connection;

public class AbstractDAO implements IDAO {
	
	protected Connection connection;

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
