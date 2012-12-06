package com.glue.webapp.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.annotation.WebServlet;

import com.glue.struct.IMedia;
import com.glue.webapp.db.MediaDAO;
import com.glue.webapp.repository.RepositoryManager;

/**
 * Create media servlet.
 */
@WebServlet("/CreateMedia")
public class CreateMediaServlet extends AbstractMediaServlet {

	private static final long serialVersionUID = -36455345605937845L;

	@Override
	protected void doOperation(IMedia media) throws SQLException {

		// Create a media
		MediaDAO mediaDAO = new MediaDAO(connection);

		mediaDAO.create(media, getCurrentUser());

		// Create associated file
		try {
			if (!RepositoryManager.createMedia(media)) {

				// Exception?
				System.out.println("Media Not OK");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
