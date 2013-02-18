package com.glue.webapp.auth;

import java.security.Principal;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.webapp.db.DAOManager;
import com.glue.webapp.db.StreamDAO;
import com.glue.webapp.db.UserDAO;

class MyHttpServletRequest extends HttpServletRequestWrapper {

	private static final String USER_PRINCIPAL = "user_principal";
	private static final String CURRENT_STREAM = "current_stream";

	/**
	 * Should we cache authenticated Principals if the request is part of an
	 * HTTP session?
	 */
	protected boolean cache = true;

	Principal principal;
	IStream stream;

	private DataSource dataSource;

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

	/**
	 * TODO: register the current stream
	 * 
	 * @return
	 */
	protected IStream getCurrentStream() {
		// Have we got a cached current stream?
		if (cache) {
			if (stream == null) {
				HttpSession session = getSession(false);
				if (session != null) {
					stream = (IStream) session.getAttribute(CURRENT_STREAM);
				}
			}
		}

		return stream;
	}

	@Override
	public void login(String username, String password) throws ServletException {
		if (getAuthType() != null || getRemoteUser() != null
				|| getUserPrincipal() != null) {
			throw new ServletException("Already authenticated");
		}

		IUser user = null;
		DAOManager manager = DAOManager.getInstance(getDataSource());
		try {
			UserDAO userDAO = manager.getUserDAO();
			user = userDAO.authenticate(username, password);
		} catch (SQLException e) {
			throw new ServletException(e);
		} finally {
			manager.closeConnectionQuietly();
		}

		if (user != null) {
			Principal p = new UserPrincipal(user);
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

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public boolean isUserInRole(String arg) {

		Principal p = getUserPrincipal();
		if (p != null) {

			GlueRole role = GlueRole.valueOf(arg);

			if (role != null) {
				if (GlueRole.REGISTERED_USER.equals(role)) {
					return true;
				}
				if (GlueRole.STREAM_PARTICIPANT.equals(role)) {
					return isUserStreamParticipant();
				}
				// TODO: handle the other roles...
			}
		}

		return false;
	}

	/**
	 * Tells whether or not the current authenticated user is a participant of
	 * the current watched stream.
	 * 
	 * @return
	 */
	protected boolean isUserStreamParticipant() {

		IStream stream = getCurrentStream();
		boolean b = false;

		if (stream != null) {

			DAOManager manager = DAOManager.getInstance(dataSource);

			try {
				StreamDAO streamDAO = manager.getStreamDAO();
				UserPrincipal principal = (UserPrincipal) getUserPrincipal();

				b = streamDAO.isParticipant(principal.getId(), stream.getId());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				manager.closeConnectionQuietly();
			}
		}

		return b;
	}

}
