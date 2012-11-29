package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseManager is reponsible for handling JDBC connection.
 * 
 * @author Greg
 * 
 */
public class DatabaseManager {

	String driverClassName = "com.mysql.jdbc.Driver";
	String connectionUrl = "jdbc:mysql://localhost:3306/gluedb";
	String dbUser = "glue";
	String dbPwd = "glue";

	private static DatabaseManager connectionFactory = null;

	private DatabaseManager() {
		try {
			Class.forName(driverClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws SQLException {
		Connection conn = null;
		conn = DriverManager.getConnection(connectionUrl, dbUser, dbPwd);
		return conn;
	}

	public static DatabaseManager getInstance() {
		if (connectionFactory == null) {
			connectionFactory = new DatabaseManager();
		}
		return connectionFactory;
	}

}
