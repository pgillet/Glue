package com.glue.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.glue.struct.impl.dto.InvitedParticipantDTO;
import com.glue.struct.impl.dto.StreamDTO;
import com.glue.webapp.db.InvitedParticipantDAO;
import com.glue.webapp.db.StreamDAO;
import com.google.gson.Gson;

/**
 * CreateStream servlet.
 */
@WebServlet("/CreateStream")
public class CreateStreamServlet extends HttpServlet {

	private static final long serialVersionUID = 1584835694695014495L;

	@Resource(name = "jdbc/gluedb")
	DataSource dataSource;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		// Retrieve content
		StreamDTO aStream = null;
		Gson gson = new Gson();
		BufferedReader reader = request.getReader();
		StringBuilder sb = new StringBuilder();
		String line = reader.readLine();
		while (line != null) {
			sb.append(line + "\n");
			line = reader.readLine();
		}
		reader.close();
		if (sb.toString() != null) {
			aStream = gson.fromJson(sb.toString(), StreamDTO.class);
		}

		Connection connection = null;

		try {
			// Get a database connection
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			// Create Stream
			StreamDAO streamDAO = new StreamDAO(connection);
			streamDAO.create(aStream);

			// Store invited participants
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

			// End of transaction
			connection.commit();
		} catch (SQLException e) {
			if (connection != null) {
				try {
					System.out.print("Transaction is being rolled back");
					connection.rollback();
				} catch (SQLException excep) {
					excep.printStackTrace();
				}
			}
		} finally {
			try {
				connection.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		// Send Response
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(aStream));

	}
}
