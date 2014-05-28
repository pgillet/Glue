package com.glue.webapp.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.search.SearchEngine;

// The Java class will be hosted at the URI path "/autocomplete"
@Path("/autocomplete")
public class AutoCompleteResource {

    private static final String SOLR_DATE_PATTERN = "yyyy-MM-dd'T'00:00:00'Z'"; // "yyyy-MM-dd'T'hh:mm:ss'Z'";

    private SimpleDateFormat df = new SimpleDateFormat(SOLR_DATE_PATTERN);

    static final Logger LOG = LoggerFactory
	    .getLogger(AutoCompleteResource.class);

    @Inject
    private SearchEngine<Event> engine;

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> run(@QueryParam("query") String query,
	    @QueryParam("lat") String lat, @QueryParam("lng") String lng,
	    @QueryParam("startDate") String startDate,
	    @QueryParam("stopDate") String endDate) {

	List<String> results = new ArrayList<>();

	if (query.trim().length() > 1) {

	    // How many word in current query?
	    List<String> queryWords = Arrays.asList(StringUtils.split(query));

	    // Ask solr
	    List<Event> events = new ArrayList<Event>();
	    try {

		// Set engine parameters
		engine.setRows(10);
		engine.setQueryString(ClientUtils.escapeQueryChars(query));
		if (StringUtils.isNotEmpty(lat)) {
		    engine.setLatitude(Double.parseDouble(lat));
		}
		if (StringUtils.isNotEmpty(lng)) {
		    engine.setLongitude(Double.parseDouble(lng));
		}
		if (StringUtils.isNotEmpty(startDate)) {
		    try {
			engine.setStartTime(df.parse(startDate));
		    } catch (ParseException e) {
		    }
		}
		if (StringUtils.isNotEmpty(endDate)) {
		    try {
			engine.setEndDate(df.parse(endDate));
		    } catch (ParseException e) {
		    }
		}

		// Search for autocomplete
		events = engine.searchForAutoComplete();
		query = query.trim();
		results.add(query);

		// Get relevant results
		for (Event event : events) {
		    String response = extractResponse(queryWords, event
			    .getTitle().toLowerCase());
		    LOG.debug(response);
		    if (!"".equals(response) && response.contains(query)) {
			String str = rtrim(response.substring(query.length()));
			if (!results.contains(str)) {
			    results.add(str);
			}
		    }
		}
	    } catch (InternalServerException e) {
		LOG.error(e.getMessage(), e);
	    }
	}

	return results;
    }

    private String extractResponse(List<String> queryWords, String title) {

	// Split response into words and retrieve first word of the query
	List<String> responseWords = Arrays.asList(StringUtils.split(title));
	responseWords.removeAll(Collections.singleton(""));
	int i = 0;
	boolean trouve = false;
	while (i < responseWords.size() && !trouve) {
	    trouve = responseWords.get(i).startsWith(queryWords.get(0));
	    if (!trouve) {
		i++;
	    }
	}

	// Get only the next word
	responseWords = responseWords.subList(i,
		Math.min(responseWords.size(), i + queryWords.size() + 2));

	return StringUtils.join(responseWords, " ").trim();

    }

    public static String rtrim(String s) {
	int i = s.length() - 1;
	while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
	    i--;
	}
	return s.substring(0, i + 1);
    }
}