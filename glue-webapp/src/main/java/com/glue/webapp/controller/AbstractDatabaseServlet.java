package com.glue.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.glue.struct.IUser;
import com.glue.webapp.db.DAOCommand;
import com.glue.webapp.db.DAOManager;
import com.google.gson.Gson;

/**
 * Root database servlet.
 */
public abstract class AbstractDatabaseServlet extends HttpServlet {

	private static final long serialVersionUID = 7672287885973304292L;

	@Resource(name = "jdbc/gluedb")
	DataSource dataSource;
	IUser currentUser;

	protected final Gson gson = new Gson();

	protected abstract void retrieveDatasFromRequest(HttpServletRequest request)
			throws IOException;

	protected abstract void doOperation(HttpServletRequest request,
			HttpServletResponse response, DAOManager manager)
			throws SQLException;

	protected abstract void sendResponse(HttpServletResponse response)
			throws IOException;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(final HttpServletRequest request,
			final HttpServletResponse response) throws ServletException,
			IOException {

		retrieveDatasFromRequest(request);

		if (isUserAuthorized(request)) {

			DAOManager manager = DAOManager.getInstance(dataSource);
			try {
				manager.transaction(new DAOCommand() {
					public Object execute(DAOManager manager)
							throws SQLException {
						doOperation(request, response, manager);
						return null;
					}
				});
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		sendResponse(response);
	}

	protected boolean isUserAuthorized(HttpServletRequest request) {
		return false;
	}

}
