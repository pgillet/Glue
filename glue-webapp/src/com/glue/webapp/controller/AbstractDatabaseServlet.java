package com.glue.webapp.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.glue.struct.IUser;
import com.glue.webapp.db.UserDAO;
import com.google.gson.Gson;

/**
 * Root database servlet.
 */
public abstract class AbstractDatabaseServlet<T> extends HttpServlet {

	private static final long serialVersionUID = 7672287885973304292L;

	@Resource(name = "jdbc/gluedb")
	DataSource dataSource;
	Connection connection;
	IUser currentUser;
	Gson gson = new Gson();

	protected abstract T getGlueObjectFromRequest(HttpServletRequest request) throws IOException;

	protected abstract void doOperation(T myObject) throws SQLException;

	protected abstract void sendResponse(HttpServletResponse response, T myObject) throws IOException;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		T myObject = getGlueObjectFromRequest(request);

		try {
			// Get a database connection
			connection = dataSource.getConnection();

			currentUser = retrieveUser(request, connection);
			if (isUserAuthorized(myObject, connection)) {

				connection.setAutoCommit(false);

				// Execute Operation
				doOperation(myObject);

				connection.commit();
			}
			// User not authorized
			else {

			}

		} catch (SQLException e) {
			if (connection != null) {
				try {
					e.printStackTrace();
					System.out.print("Transaction is being rolled back");
					connection.rollback();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		sendResponse(response, myObject);
	}

	protected boolean isUserAuthorized(T myObject, Connection connection) throws SQLException {
		return false;
	}

	// TO be implemented
	private IUser retrieveUser(HttpServletRequest request, Connection connection) throws SQLException {

		// Pour le moment je cherche un user en dur ...
		UserDAO userDAO = new UserDAO(connection);
		IUser user = userDAO.search("gregoire.denis@glue.com");
		return user;
	}

	protected IUser getCurrentUser() {
		return currentUser;
	}

}
