package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.IStream;
import com.glue.struct.impl.Stream;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.utilities.GSonHelper;

/**
 * CreateStream servlet.
 */
@WebServlet("/JoinStream")
public class JoinStreamServlet extends AbstractDatabaseServlet {

	private static final long serialVersionUID = 3926873155727833997L;
	private IStream stream;

	@Override
	protected void doOperation() throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = new StreamDAO(connection);

		// Join
		streamDAO.join(stream.getId(), getCurrentUser().getId());
	}

	@Override
	protected void retrieveDatasFromRequest(HttpServletRequest request) throws IOException {
		stream = GSonHelper.getGsonObjectFromRequest(request, Stream.class);
	}

	@Override
	protected void sendResponse(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(stream));
	}

	@Override
	protected boolean isUserAuthorized(Connection connection) throws SQLException {
		return true;
	}
}
