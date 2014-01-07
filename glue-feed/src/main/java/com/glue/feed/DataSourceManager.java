package com.glue.feed;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceManager {

	private static final String DEFAULT_SERVER = "localhost";
	private static final int DEFAULT_PORT_NUMBER = 3306;
	private static final String DEFAULT_DATABASE_NAME = "gluedb";
	private static final String DEFAULT_USER = "glue";
	private static final String DEFAULT_PASSWORD = "glue";
	
	private String serverName = DEFAULT_SERVER;
	private int portNumber = DEFAULT_PORT_NUMBER;
	private String databaseName = DEFAULT_DATABASE_NAME;
	private String user = DEFAULT_USER;
	private String password = DEFAULT_PASSWORD;
	
	private static DataSourceManager factory = new DataSourceManager();
	private MysqlDataSource ds;

	protected DataSourceManager() {
	}

	public static DataSourceManager getInstance() {
		return factory;
	}

	public DataSource getDataSource() {

		if (ds == null) {
			ds = new MysqlDataSource();
			ds.setServerName(serverName);
			ds.setPortNumber(portNumber);
			ds.setDatabaseName(databaseName);
			ds.setUser(user);
			ds.setPassword(password);
		}

		return ds;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
