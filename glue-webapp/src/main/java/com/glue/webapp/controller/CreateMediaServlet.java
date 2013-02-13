package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.glue.struct.IMedia;
import com.glue.struct.impl.Media;
import com.glue.webapp.db.MediaDAO;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.repository.RepositoryManager;
import com.glue.webapp.utilities.GSonHelper;

/**
 * Create media servlet.
 */
@WebServlet("/CreateMedia")
@MultipartConfig
public class CreateMediaServlet extends AbstractDatabaseServlet {

	private static final long serialVersionUID = -36455345605937845L;

	private IMedia media;
	private Part streamPart;

	@Override
	protected void doOperation() throws SQLException {

		// Create a media
		MediaDAO mediaDAO = new MediaDAO(connection);

		mediaDAO.create(media, getCurrentUser());

		// Create associated file
		try {
			if (streamPart != null) {
				RepositoryManager.createMedia(media, streamPart, getServletContext().getRealPath("/Streams"));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void retrieveDatasFromRequest(HttpServletRequest request) throws IOException {
		media = GSonHelper.getGsonObjectFromMultiPartRequest(request, Media.class);
		streamPart = GSonHelper.getStreamPartFromMultiPartRequest(request);
	}

	@Override
	protected void sendResponse(HttpServletResponse response) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(media));
	}

	@Override
	protected boolean isUserAuthorized(Connection connection) throws SQLException {

		// Authorized if user is a participant of the stream
		StreamDAO streamDAO = new StreamDAO(connection);
		return streamDAO.isPartipant(getCurrentUser().getId(), media.getStreamId());
	}
}
