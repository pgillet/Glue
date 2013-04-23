package com.glue.webapp.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DAOManager {

	private DataSource dataSource;

	private Connection connection;

	protected UserDAO userDAO;

	protected StreamDAO streamDAO;

	protected MediaDAO mediaDAO;

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
	 * Returns a unique instance of DAOManager per thread.
	 * 
	 * @return
	 * @throws NamingException
	 */
	public static DAOManager getInstance() throws NamingException {
		DAOManager daoManager = localInstance.get();

		Context initContext = new InitialContext();
		Context envContext = (Context) initContext.lookup("java:/comp/env");
		DataSource ds = (DataSource) envContext.lookup("jdbc/gluedb");
		daoManager.setDataSource(ds);

		return daoManager;
	}

	/**
	 * Private constructor to prevent direct instantiation.
	 */
	private DAOManager() {
	}

	protected Connection getConnection() throws SQLException {
		if (this.connection == null || this.connection.isClosed()) {
			this.connection = dataSource.getConnection();
		}

		return connection;
	}

	public UserDAO getUserDAO() throws SQLException {
		if (userDAO == null) {
			userDAO = new UserDAO();
		}
		userDAO.setConnection(getConnection());

		return userDAO;
	}

	public StreamDAO getStreamDAO() throws SQLException {
		if (streamDAO == null) {
			streamDAO = new StreamDAO();
		}
		streamDAO.setConnection(getConnection());

		return streamDAO;
	}

	public MediaDAO getMediaDAO() throws SQLException {
		if (mediaDAO == null) {
			mediaDAO = new MediaDAO();
		}
		mediaDAO.setConnection(getConnection());

		return mediaDAO;
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
			closeConnection();
		}
	}

	/**
	 * Executes the given command, and closes the current connection. This is
	 * equivalent to the following code snippet: <code>
	 * DAOManager manager = DAOManager.getInstance(dataSource);  
	 * SomeDAO dao = manager.getSomeDAO();
	 * dao.doSomeOperation();
	 * manager.closeConnection();
	 * </code>
	 * 
	 * @param command
	 * @return
	 * @throws SQLException
	 */
	public Object executeAndClose(DAOCommand command) throws SQLException {
		try {
			return command.execute(this);
		} finally {
			connection.close();
		}
	}

	public void closeConnection() throws SQLException {
		if (connection != null) {
			connection.close();
		}
	}

	public void closeConnectionQuietly() {
		try {
			closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void finalize() throws Throwable {
		try {
			closeConnection();
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
