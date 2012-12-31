package com.glue.webapp.servlet;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

class MyHttpServletRequest extends HttpServletRequestWrapper {

	private static final String USER_PRINCIPAL = "user_principal";

	/**
	 * Should we cache authenticated Principals if the request is part of an
	 * HTTP session?
	 */
	protected boolean cache = true;

	Principal principal;

	public MyHttpServletRequest(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getRemoteUser() {
		Principal p = getUserPrincipal();
		return (p != null ? p.getName() : null);
	}

	@Override
	public Principal getUserPrincipal() {
		// Have we got a cached authenticated Principal?
		if (cache) {
			if (principal == null) {
				HttpSession session = getSession(false);
				if (session != null) {
					principal = (Principal) session
							.getAttribute(USER_PRINCIPAL);
				}
			}
		}

		return principal;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		if (getAuthType() != null || getRemoteUser() != null
				|| getUserPrincipal() != null) {
			throw new ServletException("Already authenticated");
		}

		// High security: username must be equal to password.
		if (username.length() > 0 && username.equals(password)) {
			Principal p = new SimplePrincipal(username);
			register(p);
		} else {
			throw new ServletException("Authentication failed");
		}
	}

	@Override
	public void logout() throws ServletException {
		register(null);
	}

	private void register(Principal p) {

		// Cache the authentication information in our request
		principal = p;

		// Cache the authentication information in our session, if any
		if (cache) {
			HttpSession session = getSession();
			if (session != null) {
				session.setAttribute(USER_PRINCIPAL, principal);
				// If the value passed in is null, this has the same effect as
				// calling removeAttribute()
			}
		}
	}

}
