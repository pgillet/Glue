package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractDAO {

	protected Connection connection;

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * @param connection
	 *            the connection to set
	 */
	public void setConnection(Connection connection) throws SQLException {
		this.connection = connection;
	}

}
