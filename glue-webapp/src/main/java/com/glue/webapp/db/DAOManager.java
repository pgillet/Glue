package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DAOManager {

	private DataSource dataSource;

	private Connection connection;

	protected UserDAO userDAO;

	// One instance per thread
	private static final ThreadLocal<DAOManager> localInstance = new ThreadLocal<DAOManager>() {
		protected DAOManager initialValue() {
			return new DAOManager();
		};
	};

	/**
	 * Returns a unique instance of DAOManager per thread.
	 * 
	 * @return
	 */
	public static DAOManager getInstance(DataSource dataSource) {
		DAOManager daoManager = localInstance.get();
		daoManager.setDataSource(dataSource);

		return daoManager;
	}

	/**
	 * Private constructor to prevent direct instantiation.
	 */
	private DAOManager() {
	}

	protected Connection getConnection() throws SQLException {
		if (this.connection == null /* || this.connection.isClosed() */) {
			this.connection = dataSource.getConnection();
		}

		return connection;
	}

	public UserDAO getUserDAO() throws SQLException {
		if (userDAO == null) {
			userDAO = new UserDAO(getConnection());
		}

		return userDAO;
	}

	public Object transaction(DAOCommand command) throws SQLException {
		try {
			Connection connection = getConnection();
			connection.setAutoCommit(false);

			Object returnValue = command.execute(this);

			connection.commit();
			return returnValue;
		} catch (SQLException e) {
			connection.rollback();
			throw e; // or wrap it before rethrowing it
		} finally {
			connection.setAutoCommit(true);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			if (connection != null) {
				connection.close();
			}
		} finally {
			super.finalize();
		}
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

}
