package com.glue.webapp.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.webapp.logic.InternalServerException;

/**
 * A facade for a Solr server.
 * 
 * @author pgillet
 * 
 */
public class SolrSearchServer implements SearchEngine<Event> {

    private static final String SOLR_DATE_PATTERN = "yyyy-MM-dd'T'00:00:00'Z'"; // "yyyy-MM-dd'T'hh:mm:ss'Z'";

    private SimpleDateFormat df = new SimpleDateFormat(SOLR_DATE_PATTERN);

    static final Logger LOG = LoggerFactory.getLogger(SolrSearchServer.class);

    private String queryString;

    private String[] categories;

    private Date startDate;

    private Date endDate;

    private int start;

    private int rows;

    private long numFound;

    private double latitude;

    private double longitude;

    private String location;

    private static final String FIELD_STOP_TIME = "stopTime";

    private static final String FIELD_START_TIME = "startTime";

    private final String DEFAULT_Q = "*:*";

    private SolrServer solr;

    public SolrSearchServer() {
	this.solr = new HttpSolrServer(
		com.glue.persistence.index.SolrParams.getSolrServerUrl());
    }

    private List<Event> search(String requester) throws InternalServerException {

	List<? extends Event> items = new ArrayList<Event>();

	SolrQuery query = buildQuery();
	query.setParam("qt", requester);

	try {
	    QueryResponse rsp = solr.query(query);
	    items = rsp.getBeans(SolrStream.class);

	    // Get the total number of results
	    numFound = rsp.getResults().getNumFound();

	    // Highlighting
	    if (hasQueryString()) {
		summarize((List<Event>) items, rsp);
	    }

	} catch (SolrServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new InternalServerException(e);
	}

	return (List<Event>) items;

    }

    @Override
    public List<Event> searchForAutoComplete() throws InternalServerException {
	return search("/suggest");
    }

    @Override
    public List<Event> search() throws InternalServerException {
	return search("/event_select");
    }

    @Override
    public Map<String, Event> searchAsMap() throws InternalServerException {
	List<Event> items = search();
	Map<String, Event> m = new LinkedHashMap<>();
	for (Event item : items) {
	    m.put(item.getId(), item);
	}

	return m;
    }

    /**
     * Constructs a summary made of highlighted snippets for each item in the
     * query response.
     * 
     * @param items
     */
    protected void summarize(List<Event> items, QueryResponse rsp) {

	Iterator<Event> iter = items.iterator();

	while (iter.hasNext()) {
	    Event item = iter.next();

	    Map<String, List<String>> highlights = rsp.getHighlighting().get(
		    item.getId());

	    if (highlights != null) {
		List<String> snippets = highlights.get("description");
		if (snippets != null) {

		    StringBuilder sb = new StringBuilder();
		    final String ellipsis = "...";

		    for (String snippet : snippets) {
			sb.append(snippet);
			sb.append(ellipsis);
		    }
		    item.setSummary(sb.toString());
		}
	    }
	}
    }

    private SolrQuery buildQuery() {
	SolrQuery query = new SolrQuery();

	// Highlighting
	if (hasQueryString()) {
	    query.setHighlight(true);
	    query.addHighlightField("description"); // param hl.fl
	    query.setHighlightSnippets(2); // Default 1
	    query.setHighlightSimplePre("<strong>");
	    query.setHighlightSimplePost("</strong>");
	    // query.setHighlightFragsize(100); // Default
	    // query.setHighlightRequireFieldMatch(false); // Default
	}

	// Add location to query if no lat/lon parameter
	boolean hasLatLon = (getLatitude() != 0 || getLongitude() != 0);

	// Get dates
	String from = ((startDate != null) ? df.format(startDate) : "NOW/DAY");
	String to = ((endDate != null) ? df.format(endDate) : "*");

	// If no explicit search with keywords, then search events only in the
	// surrounding area, otherwise we extend the search everywhere
	String distance = hasQueryString() ? "20000" : "50"; // km

	if (hasLatLon) {
	    query.addFilterQuery("{!geofilt}");
	    query.add("sfield", "latlng");
	    query.add("pt", getLatitude() + "," + getLongitude());
	    query.add("d", distance);
	} else if (!StringUtils.isEmpty(location)) {
	    query.addFilterQuery("city:" + location);
	}

	// Boost the events that have or will soon begin, and/or those which are
	// about to end
	final String boostDateFuncFormat = "recip(abs(ms(%s,%s)),3.16e-11,1,1)";

	String boostStartTime = String.format(boostDateFuncFormat, from,
		FIELD_START_TIME);

	String boostStopTime = String.format(boostDateFuncFormat, from,
		FIELD_STOP_TIME);

	String boostDates = "sum(" + boostStartTime + "," + boostStopTime + ")";

	// Boost the events from the nearest to the farthest
	String boostLocation = null;
	if (hasLatLon && hasQueryString()) {

	    int m = 1, a = 2, b = 1;
	    boostLocation = String.format("recip(geodist(),%d,%d,%d)", m, a, b);
	}
	query.setParam("boost",
		doSum(boostStartTime, boostStopTime, boostLocation));

	// Category filtering
	if (categories != null && categories.length > 0) {
	    LOG.debug("cat = " + constructCategoriesFilter());
	    query.addFilterQuery("category:(" + constructCategoriesFilter()
		    + ")");
	}

	// Square brackets [ ] denote an inclusive range query that matches
	// values including the upper and/or lower bound.
	// Curly brackets { } denote an exclusive range query that matches
	// values between the upper and lower bounds, but excluding the upper
	// and/or lower bounds themselves.
	query.addFilterQuery(FIELD_START_TIME + ":[*" + " TO " + to + "}");
	query.addFilterQuery(FIELD_STOP_TIME + ":[" + from + " TO " + "*]");
	query.setStart(start);
	query.setRows(rows);

	String finalQuery = queryString;
	if (!hasQueryString()) {
	    finalQuery = DEFAULT_Q;
	}

	query.setParam("q", finalQuery);
	return query;
    }

    private String doSum(String... funcs) {

	StringBuilder sb = new StringBuilder();
	sb.append("sum(");

	Iterator<String> itemIterator = Arrays.asList(funcs).iterator();
	if (itemIterator.hasNext()) {
	    // special-case first item. in this case, no comma
	    sb.append(itemIterator.next());
	    while (itemIterator.hasNext()) {
		// process the rest
		String func = itemIterator.next();
		if (func != null) {
		    sb.append(",");
		    sb.append(func);
		}
	    }
	}

	sb.append(")");

	return sb.toString();
    }

    /**
     * Return true if the query criteria in null or * or *:*
     * 
     * @param query
     * @return
     */
    private boolean hasQueryString() {
	return (queryString != null && queryString.length() != 0
		&& !"*".equals(queryString) && !"*:*".equals(queryString));
    }

    @Override
    public long getNumFound() {
	return numFound;
    }

    @Override
    public String getQueryString() {
	return queryString;
    }

    /**
     * @param queryString
     *            the queryString to set
     */
    @Override
    public void setQueryString(String queryString) {
	this.queryString = queryString == null ? "" : /*
						       * ClientUtils
						       * .escapeQueryChars(
						       */queryString/* ) */;
    }

    /**
     * @return the categories
     */
    public String[] getCategories() {
	return categories;
    }

    /**
     * @param categories
     *            the categories to set
     */
    public void setCategories(String[] categories) {
	this.categories = categories;
    }

    @Override
    public Date getStartDate() {
	return startDate;
    }

    /**
     * @param startDate
     *            the startDate to set
     */
    @Override
    public void setStartTime(Date startDate) {
	this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
	return endDate;
    }

    /**
     * @param endDate
     *            the endDate to set
     */
    @Override
    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    @Override
    public int getStart() {
	return start;
    }

    @Override
    public void setStart(int start) {
	this.start = start;
    }

    @Override
    public int getRows() {
	return rows;
    }

    @Override
    public void setRows(int rows) {
	this.rows = rows;
    }

    public double getLatitude() {
	return latitude;
    }

    public void setLatitude(double latitude) {
	this.latitude = latitude;
    }

    public double getLongitude() {
	return longitude;
    }

    public void setLongitude(double longitude) {
	this.longitude = longitude;
    }

    public String getLocation() {
	return location;
    }

    public void setLocation(String location) {
	this.location = location;
    }

    private String constructCategoriesFilter() {
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < categories.length; i++) {
	    sb.append(categories[i]);
	    sb.append(" ");
	}

	return sb.toString().trim();
    }

}
