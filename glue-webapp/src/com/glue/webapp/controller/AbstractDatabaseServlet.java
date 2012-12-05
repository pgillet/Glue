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

/**
 * Root database servlet.
 */
public abstract class AbstractDatabaseServlet<T> extends HttpServlet {

	private static final long serialVersionUID = 7672287885973304292L;

	@Resource(name = "jdbc/gluedb")
	DataSource dataSource;
	Connection connection;

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
			connection.setAutoCommit(false);

			// Execute Operation
			doOperation(myObject);

			connection.commit();
		} catch (SQLException e) {
			if (connection != null) {
				try {
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
}
