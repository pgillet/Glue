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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.webapp.logic.EventController;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.search.SearchEngine;

// The Java class will be hosted at the URI path "/autocomplete"
@Path("search")
public class SearchResource {

    private static final String SOLR_DATE_PATTERN = "yyyy-MM-dd'T'00:00:00'Z'"; // "yyyy-MM-dd'T'hh:mm:ss'Z'";

    private SimpleDateFormat df = new SimpleDateFormat(SOLR_DATE_PATTERN);

    static final Logger LOG = LoggerFactory.getLogger(SearchResource.class);

    @Inject
    private SearchEngine<Event> engine;

    @Inject
    private EventController controller;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("events")
    /**
     * 
     * @param bbox a string with bounding box coordinates in a 'southwest_lng,southwest_lat,northeast_lng,northeast_lat' format.
     * @param startDate the start date in the yyyyMMdd.
     * @return
     */
    public List<Event> getEvents(@QueryParam("q") String queryString,
	    @QueryParam("ql") String location,
	    @QueryParam("cat") String catSelection,
	    @QueryParam("lat") double latitude,
	    @QueryParam("lng") double longitude,
	    @QueryParam("bbox") String bbox,
	    @QueryParam("startdate") String startDateString,
	    @QueryParam("enddate") String endDateString,
	    @QueryParam("start") int start, @QueryParam("rows") int rows) {

	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

	List<Event> events = null;
	try {

	    boolean hasBounds = StringUtils.isNotBlank(bbox);

	    LocalDate startDate = null;
	    if (StringUtils.isNotBlank(startDateString)) {
		startDate = formatter.parseLocalDate(startDateString);
	    } else if (hasBounds) {
		// Map mode with events of the day only
		startDate = LocalDate.now();
	    }

	    LocalDate endDate = null;
	    if (StringUtils.isNotBlank(endDateString)) {
		endDate = formatter.parseLocalDate(endDateString);

	    } else if (hasBounds) {
		// Map mode with events of the day only
		endDate = startDate.plusDays(1);
	    }

	    controller.setQueryString(queryString);
	    controller.setLocation(location);
	    controller.setStartDate(startDate.toDate());
	    controller.setEndDate(endDate.toDate());
	    controller.setCategories(Arrays.asList(StringUtils.split(
		    catSelection, ",")));
	    controller.setLatitude(latitude);
	    controller.setLongitude(longitude);
	    controller.setBoundingBox(bbox);
	    controller.setRowsPerPage(rows);
	    controller.setStart(start);

	    events = controller.search();
	} catch (InternalServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new WebApplicationException();
	}

	return events;
    }

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "application/json"
    @Produces(MediaType.APPLICATION_JSON)
    @Path("autocomplete")
    public List<String> autocomplete(@QueryParam("query") String query,
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