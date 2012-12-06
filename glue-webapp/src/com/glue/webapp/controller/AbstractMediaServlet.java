package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.glue.struct.IMedia;
import com.glue.struct.impl.Media;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.utilities.GSonHelper;
import com.google.gson.Gson;

/**
 * Root media servlet.
 */
public abstract class AbstractMediaServlet extends AbstractDatabaseServlet<IMedia> {

	private static final long serialVersionUID = 5385604630898659097L;

	protected Gson gson = new Gson();

	public AbstractMediaServlet() {
		super();
	}

	protected IMedia getObjectFromRequest(HttpServletRequest request) throws IOException {

		return GSonHelper.getGsonObjectFromMultiPartRequest(request, Media.class);
	}

	@Override
	protected Part getStreamPartFromRequest(HttpServletRequest request) throws IOException {
		return GSonHelper.getStreamPartFromMultiPartRequest(request);
	}

	@Override
	protected void sendResponse(HttpServletResponse response, IMedia media) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(media));
	}

	@Override
	protected boolean isUserAuthorized(IMedia media, Connection connection) throws SQLException {

		// Authorized if user is a participant of the stream
		StreamDAO streamDAO = new StreamDAO(connection);
		return streamDAO.isPartipant(getCurrentUser().getId(), media.getStreamId());
	}
}
