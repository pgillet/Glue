package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.IStream;
import com.glue.struct.impl.Stream;
import com.glue.webapp.auth.GlueRole;
import com.glue.webapp.auth.UserPrincipal;
import com.glue.webapp.db.DAOManager;
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
	protected void doOperation(HttpServletRequest request,
			HttpServletResponse response, DAOManager manager)
			throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = manager.getStreamDAO();
		
		UserPrincipal principal = (UserPrincipal) request.getUserPrincipal();

		// Join
		streamDAO.join(stream.getId(), principal.getId());
	}

	@Override
	protected void retrieveDatasFromRequest(HttpServletRequest request)
			throws IOException {
		stream = GSonHelper.getGsonObjectFromRequest(request, Stream.class);
	}

	@Override
	protected void sendResponse(HttpServletResponse response)
			throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(stream));
	}

	@Override
	protected boolean isUserAuthorized(HttpServletRequest request) {
		return request.isUserInRole(GlueRole.REGISTERED_USER.toString());
	}
}
