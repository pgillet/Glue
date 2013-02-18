package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.IStream;
import com.glue.webapp.auth.GlueRole;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;

/**
 * SearchStream servlet.
 */
@WebServlet("/SearchStream")
public class SearchStreamServlet extends AbstractDatabaseServlet {

	private static final long serialVersionUID = 3926873155727833997L;
	private List<IStream> streams = new ArrayList<IStream>();
	private String query;

	@Override
	protected void retrieveDatasFromRequest(HttpServletRequest request) throws IOException {
		query = request.getParameter("query");
	}

	@Override
	protected void sendResponse(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(streams));
	}

	@Override
	protected boolean isUserAuthorized(HttpServletRequest request) {
		return request.isUserInRole(GlueRole.REGISTERED_USER.toString());
	}

	@Override
	protected void doOperation(HttpServletRequest request, HttpServletResponse response, DAOManager manager) throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = manager.getStreamDAO();

		// Search
		streams = streamDAO.search(query);
	}
}
