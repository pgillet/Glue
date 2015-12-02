package com.glue.feed.html;

// JDK imports
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
// Commons Logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import crawlercommons.robots.BaseRobotRules;
import crawlercommons.robots.SimpleRobotRules;
import crawlercommons.robots.SimpleRobotRules.RobotRulesMode;
import crawlercommons.robots.SimpleRobotRulesParser;

/**
 * This class uses crawler-commons for handling the parsing of
 * {@code robots.txt} files. It emits SimpleRobotRules objects, which describe
 * the download permissions as described in SimpleRobotRulesParser.
 */
public class RobotRulesParser {

    public static final Logger LOG = LoggerFactory
	    .getLogger(RobotRulesParser.class);

    protected static final Hashtable<String, BaseRobotRules> CACHE = new Hashtable<String, BaseRobotRules>();

    protected static final String ROBOTS_TXT = "/robots.txt";

    /**
     * A {@link BaseRobotRules} object appropriate for use when the
     * {@code robots.txt} file is empty or missing; all requests are allowed.
     */
    public static final BaseRobotRules EMPTY_RULES = new SimpleRobotRules(
	    RobotRulesMode.ALLOW_ALL);

    /**
     * A {@link BaseRobotRules} object appropriate for use when the
     * {@code robots.txt} file is not fetched due to a {@code 403/Forbidden}
     * response; all requests are disallowed.
     */
    public static BaseRobotRules FORBID_ALL_RULES = new SimpleRobotRules(
	    RobotRulesMode.ALLOW_NONE);

    private static SimpleRobotRulesParser robotParser = new SimpleRobotRulesParser();

    public RobotRulesParser() {
    }

    public boolean isAllowed(String url) throws IOException {
	URL urlObj = new URL(url);
	// Get the base URL
	// String hostId = urlObj.getProtocol() + "://" + urlObj.getHost()
	// + (urlObj.getPort() > -1 ? ":" + urlObj.getPort() : "");

	String hostId = urlObj.getProtocol() + "://" + urlObj.getAuthority();

	BaseRobotRules rules = CACHE.get(hostId);
	if (rules == null) {
	    HttpGet httpget = new HttpGet(hostId + ROBOTS_TXT);
	    HttpContext context = new BasicHttpContext();
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpResponse response = httpclient.execute(httpget, context);
	    if (response.getStatusLine() != null
		    && response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) { // 404
		rules = EMPTY_RULES;
		// consume entity to deallocate connection
		EntityUtils.consumeQuietly(response.getEntity());
	    } else {
		BufferedHttpEntity entity = new BufferedHttpEntity(
			response.getEntity());
		rules = robotParser.parseContent(hostId,
			IOUtils.toByteArray(entity.getContent()), "text/plain",
			HTMLFetcher.USER_AGENT);
	    }
	    CACHE.put(hostId, rules);
	}

	boolean allowed = rules.isAllowed(url);

	LOG.debug(url + (allowed ? " allowed" : " not allowed"));

	return allowed;
    }

    public static void main(String[] args) throws IOException {
	String url = "http://www.google.com/catalogs";

	RobotRulesParser parser = new RobotRulesParser();
	boolean b = parser.isAllowed(url);

	System.out.println(b ? "Allowed" : "Not allowed");
    }
}
