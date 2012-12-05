package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.impl.dto.StreamDTO;
import com.glue.webapp.utilities.GSonHelper;
import com.google.gson.Gson;

/**
 * Root stream servlet.
 */
public abstract class AbstractStreamServlet extends AbstractDatabaseServlet<StreamDTO> {

	private static final long serialVersionUID = 2591446307196720288L;

	protected Gson gson = new Gson();

	public AbstractStreamServlet() {
		super();
	}

	@Override
	protected StreamDTO getGlueObjectFromRequest(HttpServletRequest request) throws IOException {
		return GSonHelper.getGsonObjectFromRequest(request, StreamDTO.class);
	}

	@Override
	protected void sendResponse(HttpServletResponse response, StreamDTO myObject) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(myObject));
	}
}
