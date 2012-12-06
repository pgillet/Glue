package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.struct.impl.Stream;
import com.glue.webapp.utilities.GSonHelper;
import com.google.gson.Gson;

/**
 * Root stream servlet.
 */
public abstract class AbstractStreamServlet extends AbstractDatabaseServlet<IStream> {

	private static final long serialVersionUID = 2591446307196720288L;

	protected Gson gson = new Gson();

	public AbstractStreamServlet() {
		super();
	}

	@Override
	protected Stream getGlueObjectFromRequest(HttpServletRequest request) throws IOException {
		return GSonHelper.getGsonObjectFromRequest(request, Stream.class);
	}

	@Override
	protected void sendResponse(HttpServletResponse response, IStream myObject) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(myObject));
	}

	@Override
	protected boolean isUserAuthorized(IUser user) {
		return true;
	}
}
