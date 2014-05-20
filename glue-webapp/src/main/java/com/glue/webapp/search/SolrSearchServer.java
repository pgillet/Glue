package com.glue.webapp.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.apache.solr.client.solrj.util.ClientUtils;
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

    public List<Event> searchForAutoComplete(String q)
	    throws InternalServerException {

	List<? extends Event> items = new ArrayList<Event>();

	SolrQuery query = new SolrQuery();

	// Use this specific RequestHandler
	query.setParam("qt", "/suggest");
	query.setParam("q", ClientUtils.escapeQueryChars(q));

	try {
	    QueryResponse rsp = solr.query(query);
	    items = rsp.getBeans(SolrStream.class);

	    // Get the total number of results
	    numFound = rsp.getResults().getNumFound();
	} catch (SolrServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new InternalServerException(e);
	}

	return (List<Event>) items;
    }

    @Override
    public List<Event> search() throws InternalServerException {

	List<? extends Event> items = new ArrayList<Event>();

	SolrQuery query = constructSolrQuery();

	try {
	    QueryResponse rsp = solr.query(query);
	    items = rsp.getBeans(SolrStream.class);

	    // Get the total number of results
	    numFound = rsp.getResults().getNumFound();

	    // Highlighting
	    if (!isFullQuery()) {
		summarize((List<Event>) items, rsp);
	    }

	} catch (SolrServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new InternalServerException(e);
	}

	return (List<Event>) items;
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

    private SolrQuery constructSolrQuery() {
	SolrQuery query = new SolrQuery();

	// Highlighting
	if (!isFullQuery()) {
	    query.setHighlight(true);
	    query.addHighlightField("description"); // param hl.fl
	    query.setHighlightSnippets(2); // Default 1
	    query.setHighlightSimplePre("<strong>");
	    query.setHighlightSimplePost("</strong>");
	    // query.setHighlightFragsize(100); // Default
	    // query.setHighlightRequireFieldMatch(false); // Default
	}

	query.setParam("qt", "/event_select");

	// Add location to query if no lat/lon parameter
	boolean hasLatLon = false;
	boolean addLocation = false;

	// Get dates
	String from = ((startDate != null) ? df.format(startDate) : "NOW/HOUR");
	String to = ((endDate != null) ? df.format(endDate) : "*");

	// Location filtering
	if ((getLatitude() != 0 || getLongitude() != 0)) {
	    query.addFilterQuery("{!geofilt}");
	    query.add("sfield", "latlng");
	    query.add("pt", getLatitude() + "," + getLongitude());
	    query.add("d", "30");
	    hasLatLon = true;
	} else if (!StringUtils.isEmpty(location)) {
	    addLocation = true;
	}

	// Boost dates and location
	String boostDates = "if(max(ms(" + from + "," + FIELD_START_TIME
		+ "),0),10000,recip(abs(ms(" + from + "," + FIELD_START_TIME
		+ ")),3.16e-10,10000,1))";
	String boostLocation = "recip(geodist(),1,1000,10)";
	String boost = boostDates;
	if (hasLatLon) {
	    boost = "sum(" + boostDates + "," + boostLocation + ")";
	}
	query.setParam("boost", boost);

	// Category filtering
	if (categories != null && categories.length > 0) {
	    System.out.println("cat = " + constructCategoriesFilter());
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
	if (isFullQuery()) {
	    finalQuery = DEFAULT_Q;
	}
	if (addLocation) {
	    finalQuery = finalQuery + " city:" + location;
	}
	query.setParam("q", finalQuery);
	return query;
    }

    /**
     * Return true if the query criteria in null or * or *:*
     * 
     * @param query
     * @return
     */
    private boolean isFullQuery() {
	return (queryString == null)
		|| ("".equals(queryString) || ("*".equals(queryString)) || ("*:*")
			.equals(queryString));
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
	String result = "";
	// Add first category
	if (categories.length > 0) {
	    result += categories[0];
	}
	// For each other categories
	for (int i = 1; i < categories.length; i++) {
	    result += " " + categories[i];
	}
	return result;
    }
}
