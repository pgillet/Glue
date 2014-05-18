package com.glue.webapp.i18n;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.webapp.beans.LanguageBean;

/**
 * 
 * @author pgillet
 * @see LanguageBean#setLanguage(String)
 */
@WebFilter(filterName = "I18nFilter", urlPatterns = { "*.xhtml" })
public class I18nFilter implements Filter {

    public static final Logger LOG = LoggerFactory.getLogger(I18nFilter.class);

    public static final String USER_LANGUAGE = "User-Language";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {

	HttpServletRequest request0 = (HttpServletRequest) request;
	String path = request0.getServletPath();

	Locale locale = request.getLocale();
	HttpSession session = request0.getSession(false);
	if (session != null) {
	    String language = (String) session.getAttribute(USER_LANGUAGE);
	    if (language != null) {
		locale = new Locale(language);
	    }
	}

	if (LOG.isTraceEnabled()) {
	    LOG.trace("Request path = " + path);
	    LOG.trace("User locale = " + locale);
	}

	if (locale == null) {
	    chain.doFilter(request, response);
	} else if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	    // Default locale: Web pages are directly under the server's
	    // document root
	    chain.doFilter(request, response);
	} else if (path.startsWith("/" + locale.getLanguage() + "/")) {
	    // 2nd step process: the request has already been redirected
	    chain.doFilter(request, response);
	} else if (path.startsWith("/" + getLocaleTag(locale) + "/")) {
	    // 2nd step process: the request has already been redirected
	    chain.doFilter(request, response);
	} else {
	    // 1st step process: check if the requested page is localized in a
	    // directory named with the locale's tag (ex: en_US)
	    ServletContext context = request.getServletContext();
	    String localizedPath = buildPath(getLocaleTag(locale), path);

	    if (context.getResource(localizedPath) == null) {
		// Check if the requested page is localized in
		// a directory named with the locale's language (ex: fr)
		localizedPath = buildPath(locale.getLanguage(), path);
	    }

	    if (context.getResource(localizedPath) != null) {

		// We preserve the query string that is contained in the request
		// URL after the path.
		String newLocation = buildLocation(localizedPath.substring(1),
			request0.getQueryString());

		// TODO: Forward vs Redirect ?

		// RequestDispatcher dispatcher = request
		// .getRequestDispatcher(location);
		// dispatcher.forward(request, response);

		((HttpServletResponse) response).sendRedirect(newLocation);

	    } else {
		chain.doFilter(request, response);
	    }
	}
    }

    /**
     * There is no need to encode the URL built with the returned path.
     * 
     */
    private String buildPath(String prefix, String path) {
	StringBuilder sb = new StringBuilder("/").append(prefix).append(path);
	return sb.toString();
    }

    /**
     * Builds the redirect location URL by appending the query string to the
     * given path.
     * 
     * @param path
     * @param queryString
     * @return
     */
    private String buildLocation(String path, String queryString) {
	if (queryString != null) {
	    StringBuilder sb = new StringBuilder(path).append("?").append(
		queryString);
	    return sb.toString();
	}

	return path;
    }

    public static String getLocaleTag(Locale locale) {
	StringBuilder sb = new StringBuilder(locale.getLanguage());
	if (locale.getCountry().length() > 0) {
	    sb.append("_").append(locale.getCountry());
	}

	return sb.toString();
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}
