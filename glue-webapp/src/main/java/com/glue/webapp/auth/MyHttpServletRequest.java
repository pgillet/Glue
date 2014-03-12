package com.glue.webapp.auth;

import java.security.Principal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.domain.User;
import com.glue.persistence.UserDAO;

class MyHttpServletRequest extends HttpServletRequestWrapper {

    static final Logger LOG = LoggerFactory
	    .getLogger(MyHttpServletRequest.class);

    private static final String USER_PRINCIPAL = "user_principal";
    private static final String CURRENT_STREAM = "current_stream";

    /**
     * Should we cache authenticated Principals if the request is part of an
     * HTTP session?
     */
    protected boolean cache = true;

    Principal principal;
    Event event;

    private UserDAO userDAO;

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
     * TODO: register the current event
     * 
     * @return
     */
    protected Event getCurrentEvent() {
	// Have we got a cached current event?
	if (cache) {
	    if (event == null) {
		HttpSession session = getSession(false);
		if (session != null) {
		    event = (Event) session.getAttribute(CURRENT_STREAM);
		}
	    }
	}

	return event;
    }

    @Override
    public void login(String username, String password) throws ServletException {
	if (getAuthType() != null || getRemoteUser() != null
		|| getUserPrincipal() != null) {
	    throw new ServletException("Already authenticated");
	}

	User user = null;
	try {
	    user = userDAO.authenticate(username, password);
	} finally {
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
     * @return the userDAO
     */
    protected UserDAO getUserDAO() {
	return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    protected void setUserDAO(UserDAO userDAO) {
	this.userDAO = userDAO;
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
		// TODO: handle the other roles...
	    }
	}

	return false;
    }
}
