package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.glue.struct.IMedia;
import com.glue.struct.impl.Media;
import com.glue.webapp.auth.GlueRole;
import com.glue.webapp.auth.UserPrincipal;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.MediaDAO;
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
	protected void doOperation(HttpServletRequest request, HttpServletResponse response, DAOManager manager)
			throws SQLException {

		// Create a media
		MediaDAO mediaDAO = manager.getMediaDAO();

		UserPrincipal principal = (UserPrincipal) request.getUserPrincipal();
		media.setUser(principal);
		mediaDAO.create(media);

		// Create associated file
		try {
			if (streamPart != null) {
				RepositoryManager.createMedia(media, streamPart);
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
	protected boolean isUserAuthorized(HttpServletRequest request) {
		return request.isUserInRole(GlueRole.STREAM_PARTICIPANT.toString());
	}
}
