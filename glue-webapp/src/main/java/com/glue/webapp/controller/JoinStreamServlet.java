package com.glue.webapp.controller;

import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import com.glue.struct.IStream;
import com.glue.webapp.db.StreamDAO;

/**
 * CreateStream servlet.
 */
@WebServlet("/JoinStream")
public class JoinStreamServlet extends AbstractStreamServlet {

	private static final long serialVersionUID = 3926873155727833997L;

	@Override
	protected void doOperation(IStream aStream) throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = new StreamDAO(connection);

		// Join
		streamDAO.join(aStream.getId(), getCurrentUser().getId());
	}
}
