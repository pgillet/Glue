package com.glue.webapp.controller;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import com.glue.struct.impl.dto.InvitedParticipantDTO;
import com.glue.struct.impl.dto.StreamDTO;
import com.glue.webapp.db.InvitedParticipantDAO;
import com.glue.webapp.db.StreamDAO;

/**
 * CreateStream servlet.
 */
@WebServlet("/CreateOrUpdateStream")
public class CreateOrUpdateStreamServlet extends AbstractStreamServlet {

	private static final long serialVersionUID = -6187252800404277228L;

	@Override
	protected void doOperation(StreamDTO aStream) throws SQLException {

		// Create or update Stream
		StreamDAO streamDAO = new StreamDAO(connection);

		// I'm looking for an existing stream
		if (streamDAO.search(aStream.getId()) != null) {
			streamDAO.update(aStream);
		} else {
			streamDAO.create(aStream);
		}

		// Store invited participants
		// TODO delete previous participant ...
		InvitedParticipantDAO ipDAO = new InvitedParticipantDAO(connection);
		Map<String, String> ipList = aStream.getInvitedParticipants();
		if (ipList != null) {
			InvitedParticipantDTO ip = new InvitedParticipantDTO();
			for (Map.Entry<String, String> entry : ipList.entrySet()) {
				ip.setMail(entry.getKey());
				ip.setName(entry.getValue());
				ip.setStreamId(aStream.getId());
				ipDAO.create(ip);
			}
		}

		// Store tags
		// TODO

		// Set user as an administrator participant
		// TODO

	}

}
