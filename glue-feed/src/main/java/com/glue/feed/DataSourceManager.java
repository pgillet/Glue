package com.glue.feed;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceManager {

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
			ds.setServerName("localhost");
			ds.setPortNumber(3306);
			ds.setDatabaseName("gluedb");
			ds.setUser("glue");
			ds.setPassword("glue");
		}

		return ds;
	}

}
