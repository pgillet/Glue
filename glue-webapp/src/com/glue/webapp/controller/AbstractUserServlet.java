package com.glue.webapp.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.glue.struct.IUser;
import com.glue.struct.impl.User;
import com.glue.webapp.utilities.GSonHelper;
import com.google.gson.Gson;

/**
 * Root stream servlet.
 */
public abstract class AbstractUserServlet extends AbstractDatabaseServlet<IUser> {

	private static final long serialVersionUID = 2277316458046768364L;

	protected Gson gson = new Gson();

	public AbstractUserServlet() {
		super();
	}

	@Override
	protected IUser getGlueObjectFromRequest(HttpServletRequest request) throws IOException {
		return GSonHelper.getGsonObjectFromRequest(request, User.class);
	}

	@Override
	protected void sendResponse(HttpServletResponse response, IUser user) throws IOException {
		PrintWriter writer = response.getWriter();
		writer.write(gson.toJson(user));
	}

	@Override
	protected boolean isUserAuthorized(IUser user) {
		return true;
	}
}
