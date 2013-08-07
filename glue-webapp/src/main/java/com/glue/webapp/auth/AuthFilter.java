package com.glue.webapp.auth;

import java.io.IOException;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.faces.application.ResourceHandler;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A filter for user authentication. The filter is mapped to all paths, except
 * for those specified in the "excludes" initialization parameter (comma
 * separated list of URL patterns).
 * 
 * @author pgillet
 * 
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = { "/*" }, initParams = {
		@WebInitParam(name = "form-login-page", value = "/login.xhtml"),
		@WebInitParam(name = "excludes", value = "/main.xhtml, /register.xhtml") })
public class AuthFilter implements Filter {

	private static final String RESOURCES = "/resources";

	static final Logger LOG = LoggerFactory.getLogger(AuthFilter.class);

	private static final String REGEX = "\\s*,\\s*";
	private static final String EXCLUDES = "excludes";
	private static final String APACHE_HTTP_CLIENT = "Apache-HttpClient";
	private static final String USER_AGENT = "user-agent";
	private static final String FORM_LOGIN_PAGE = "form-login-page";
	private static final String BASIC = "Basic ";
	private static final String AUTHORIZATION = "Authorization";

	private FilterConfig filterConfig;
	private String[] excluded;

	@Resource(name = "jdbc/gluedb")
	private DataSource dataSource;

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request0 = (HttpServletRequest) servletRequest;
		MyHttpServletRequest request = new MyHttpServletRequest(request0);
		request.setDataSource(getDataSource());

		String path = ((HttpServletRequest) servletRequest).getServletPath();
		if (excludeFromFilter(path)) {
			chain.doFilter(request, servletResponse);
			return;
		}

		HttpServletResponse response = (HttpServletResponse) servletResponse;

		if (LOG.isDebugEnabled()) {
			Enumeration<String> myenum = request0.getHeaderNames();
			while (myenum.hasMoreElements()) {
				String headerName = (String) myenum.nextElement();
				String header = request0.getHeader(headerName);

				LOG.debug(headerName + "=" + header);
			}
		}

		try {

			// If the user is not authenticated
			if (request.getUserPrincipal() == null) {

				String user = null;
				String password = null;

				// Try Basic authentication
				String authorization = request.getHeader(AUTHORIZATION);
				if (authorization != null) {
					// Authorization headers looks like "Basic blahblah",
					// where blahblah is the base64 encoded username and
					// password. We want the part after "Basic ".
					final String prefix = BASIC;
					String userInfo = authorization.substring(prefix.length())
							.trim();
					String nameAndPassword = new String(
							Base64.decodeBase64(userInfo));
					// Decoded part looks like "username:password".
					int index = nameAndPassword.indexOf(":");
					if (index != -1) {
						user = nameAndPassword.substring(0, index);
						password = nameAndPassword.substring(index + 1);
					}
				}

				if (user != null && password != null) {
					request.login(user, password);
				}
			}

			if (request.getUserPrincipal() != null) {
				chain.doFilter(request, response);
			} else {
				authenticate(request, response);
			}
		} catch (Exception e) {
			authenticate(request, response);
		}
	}

	// If no Authorization header was supplied in the request.

	private void authenticate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // I.e., 401

		// Form-based authentication for classic browsers
		// and Basic authentication for mobile devices using the Glue API
		// TODO: may use a specific custom parameter to make the diff instead of
		// the user-agent string
		// String userAgent = request.getHeader(USER_AGENT);
		// if (userAgent != null && userAgent.indexOf(APACHE_HTTP_CLIENT) != -1)
		// {
		response.setHeader("WWW-Authenticate", "BASIC realm=\"Glue-App\"");
		// }

		// String formLoginPage =
		// filterConfig.getInitParameter(FORM_LOGIN_PAGE);
		// filterConfig.getServletContext().getRequestDispatcher(formLoginPage)
		// .forward(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		// Exclusions
		String formLoginPage = filterConfig.getInitParameter(FORM_LOGIN_PAGE);

		final String[] internalExcludes = new String[] {
				ResourceHandler.RESOURCE_IDENTIFIER, RESOURCES, formLoginPage, };

		String initParam = filterConfig.getInitParameter(EXCLUDES);
		String[] userSpecifiedExcludes = (initParam != null) ? initParam
				.split(REGEX) : new String[0];

		this.excluded = new String[internalExcludes.length
				+ userSpecifiedExcludes.length];

		System.arraycopy(internalExcludes, 0, this.excluded, 0,
				internalExcludes.length);
		System.arraycopy(userSpecifiedExcludes, 0, this.excluded,
				internalExcludes.length, userSpecifiedExcludes.length);
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

	private boolean excludeFromFilter(String path) {
		for (String exclude : this.excluded) {
			if (path.startsWith(exclude)) { // equals?
				return true;
			}
		}

		return false;
	}
}
