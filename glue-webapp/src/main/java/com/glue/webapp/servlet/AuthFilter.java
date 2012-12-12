package com.glue.webapp.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

public class AuthFilter implements Filter {

	private static final String AUTHORIZATION = "Authorization";

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		String authorization = request.getHeader(AUTHORIZATION);
		if (authorization == null) {
			request.authenticate(response);
		} else {
			// Authorization headers looks like "Basic blahblah",
			// where blahblah is the base64 encoded username and
			// password. We want the part after "Basic ".
			final String prefix = "Basic ";
			String userInfo = authorization.substring(prefix.length()).trim();
			String nameAndPassword = new String(Base64.decodeBase64(userInfo));
			// Decoded part looks like "username:password".
			int index = nameAndPassword.indexOf(":");
			String user = nameAndPassword.substring(0, index);
			String password = nameAndPassword.substring(index + 1);
			// High security: username must be equal to password.
			if (user.length() > 0 && user.equals(password)) {
				request.login(user, password);
			} else {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				request.authenticate(response);
			}
		}

		chain.doFilter(request, response);

		request.logout();
	}

	// If no Authorization header was supplied in the request.

	private void askForPassword(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // I.e., 401
		response.setHeader("WWW-Authenticate",
				"BASIC realm=\"Insider-Trading\"");
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

}
