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
import com.glue.webapp.repository.RepositoryManager;
import com.glue.webapp.utilities.GSonHelper;

/**
 * CreateStream servlet.
 */
@WebServlet("/CreateOrUpdateStream")
public class CreateOrUpdateStreamServlet extends AbstractDatabaseServlet {

	private static final long serialVersionUID = -6187252800404277228L;

	private IStream stream;

	@Override
	protected void doOperation() throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = new StreamDAO(connection);

		// I'm looking for an existing stream
		if (streamDAO.search(stream.getId()) != null) {
			streamDAO.update(stream);
		} else {
			streamDAO.create(stream);

			// Set user as administrator
			streamDAO.joinAsAdmin(stream.getId(), getCurrentUser().getId());

			// Create associated directory
			if (!RepositoryManager.createStream(stream.getId(), getServletContext().getRealPath("/Streams"))) {

				// Exception?
				System.out.println("Not OK");
			}
		}

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
