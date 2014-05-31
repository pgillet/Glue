/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.glue.webapp.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Example filter that sets the character encoding to be used in parsing the
 * incoming request, either unconditionally or only if the client did not
 * specify a character encoding. Configuration of this filter is based on the
 * following initialization parameters:
 * </p>
 * <ul>
 * <li><strong>encoding</strong> - The character encoding to be configured for
 * this request, either conditionally or unconditionally based on the
 * <code>ignore</code> initialization parameter. This parameter is required, so
 * there is no default.</li>
 * <li><strong>ignore</strong> - If set to "true", any character encoding
 * specified by the client is ignored, and the value returned by the
 * <code>selectEncoding()</code> method is set. If set to "false,
 * <code>selectEncoding()</code> is called <strong>only</strong> if the client
 * has not already specified an encoding. By default, this parameter is set to
 * "false".</li>
 * </ul>
 * 
 * <p>
 * Although this filter can be used unchanged, it is also easy to subclass it
 * and make the <code>selectEncoding()</code> method more intelligent about what
 * encoding to choose, based on characteristics of the incoming request (such as
 * the values of the <code>Accept-Language</code> and <code>User-Agent</code>
 * headers, or a value stashed in the current user's session.
 * </p>
 * 
 * <p>
 * This filter is used to resolve URL encoding issues when redirecting
 * programmatically with
 * <code>FacesContext().getCurrentInstance().getExternalContext().redirect(url)</code>
 * when the given URL has query parameters.
 * </p>
 * 
 * <p>
 * The filter code is copied from the container provided filter
 * org.apache.catalina.filters.SetCharacterEncodingFilter, but this original
 * filter do not set the content type for responses neither include charset name
 * in the content type to be UTF-8, which is necessary to make it work.
 * </p>
 * 
 * @see https://issues.apache.org/jira/browse/TOMEE-1063
 * @see http://wiki.apache.org/tomcat/FAQ/CharacterEncoding
 * @see http://tomcat.apache.org/tomcat-7.0-doc/config/ajp.html
 * 
 */
@WebFilter(filterName = "SetCharacterEncodingFilter", urlPatterns = { "/*" }, initParams = {
	@WebInitParam(name = "ignore", value = "true"),
	@WebInitParam(name = "encoding", value = "UTF-8") })
public class SetCharacterEncodingFilter implements Filter {

    static final Logger LOG = LoggerFactory
	    .getLogger(SetCharacterEncodingFilter.class);

    /**
     * The default character encoding to set for requests that pass through this
     * filter.
     */
    private String encoding;

    /**
     * Should a character encoding specified by the client be ignored?
     */
    private boolean ignore = false;

    public void setEncoding(String encoding) {
	this.encoding = encoding;
    }

    public String getEncoding() {
	return encoding;
    }

    public void setIgnore(boolean ignore) {
	this.ignore = ignore;
    }

    public boolean isIgnore() {
	return ignore;
    }

    /**
     * Select and set (if specified) the character encoding to be used to
     * interpret request parameters for this request.
     * 
     * @param request
     *            The servlet request we are processing
     * @param response
     *            The servlet response we are creating
     * @param chain
     *            The filter chain we are processing
     * 
     * @exception IOException
     *                if an input/output error occurs
     * @exception ServletException
     *                if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
	    FilterChain chain) throws IOException, ServletException {

	// Conditionally select and set the character encoding to be used
	if (ignore || (request.getCharacterEncoding() == null)) {
	    String characterEncoding = selectEncoding(request);
	    if (characterEncoding != null)
		request.setCharacterEncoding(characterEncoding);
	}

	response.setContentType("text/html; charset=" + getEncoding());
	// response.setCharacterEncoding(getEncoding());

	// Pass control on to the next filter
	chain.doFilter(request, response);
    }

    /**
     * Select an appropriate character encoding to be used, based on the
     * characteristics of the current request and/or filter initialization
     * parameters. If no character encoding should be set, return
     * <code>null</code>.
     * <p>
     * The default implementation unconditionally returns the value configured
     * by the <strong>encoding</strong> initialization parameter for this
     * filter.
     * 
     * @param request
     *            The servlet request we are processing
     */
    protected String selectEncoding(ServletRequest request) {
	return this.encoding;
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
	setIgnore(Boolean.parseBoolean(config.getInitParameter("ignore")));
	setEncoding(config.getInitParameter("encoding"));
    }
}
