package com.glue.webapp.controller;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import com.glue.struct.IStream;
import com.glue.webapp.db.StreamDAO;

/**
 * CreateStream servlet.
 */
@WebServlet("/CreateOrUpdateStream")
public class CreateOrUpdateStreamServlet extends AbstractStreamServlet {

	private static final long serialVersionUID = -6187252800404277228L;

	@Override
	protected void doOperation(IStream aStream) throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = new StreamDAO(connection);

		// I'm looking for an existing stream
		if (streamDAO.search(aStream.getId()) != null) {
			streamDAO.update(aStream);
		} else {
			streamDAO.create(aStream);
		}

	}
}
