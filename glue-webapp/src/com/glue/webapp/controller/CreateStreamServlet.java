package com.glue.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.impl.dto.StreamDTO;
import com.glue.webapp.db.StreamDAO;
import com.google.gson.Gson;

/**
 * CreateStream servlet.
 */
@WebServlet("/CreateStream")
public class CreateStreamServlet extends HttpServlet {

	private static final long serialVersionUID = 1584835694695014495L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		StreamDTO aStream = null;

		// Retrieve content
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

		// Save content
		StreamDAO streamDAO = new StreamDAO();
		streamDAO.create(aStream);

		// Send Response
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(aStream));

	}
}
