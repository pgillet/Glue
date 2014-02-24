package com.glue.webapp.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.util.ClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.IStream;
import com.glue.webapp.logic.InternalServerException;

/**
 * A facade for a Solr server.
 * 
 * @author pgillet
 * 
 */
public class SolrSearchServer implements SearchEngine<IStream> {

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

    private static final String END_DATE_FIELD = "end_date";

    private static final String START_DATE_FIELD = "start_date";

    private final String DEFAULT_Q = "*:*";

    private SolrServer solr;

    public SolrSearchServer() {
	String urlString = "http://localhost:8080/solr"; // TODO: should be
							 // configurable
	this.solr = new HttpSolrServer(urlString);
    }

    public List<IStream> searchForAutoComplete(String q)
	    throws InternalServerException {

	List<? extends IStream> items = new ArrayList<IStream>();

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

	return (List<IStream>) items;
    }

    @Override
    public List<IStream> search() throws InternalServerException {

	List<? extends IStream> items = new ArrayList<IStream>();

	SolrQuery query = constructSolrQuery();

	try {
	    QueryResponse rsp = solr.query(query);
	    items = rsp.getBeans(SolrStream.class);

	    // Get the total number of results
	    numFound = rsp.getResults().getNumFound();

	    // Highlighting
	    if (!isFullQuery()) {
		summarize((List<IStream>) items, rsp);
	    }

	} catch (SolrServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new InternalServerException(e);
	}

	return (List<IStream>) items;
    }

    @Override
    public Map<Long, IStream> searchAsMap() throws InternalServerException {
	List<IStream> items = search();
	Map<Long, IStream> m = new HashMap<>();
	for (IStream item : items) {
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
    protected void summarize(List<IStream> items, QueryResponse rsp) {

	Iterator<IStream> iter = items.iterator();

	while (iter.hasNext()) {
	    IStream item = iter.next();

	    Map<String, List<String>> highlights = rsp.getHighlighting().get(
		    Long.toString(item.getId()));

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

	query.setParam("qt", "/stream_select");

	// Add location to query if no lat/lon parameter
	boolean addLocation = false;

	// Get dates
	String from = ((startDate != null) ? Long.toString(startDate.getTime())
		: "*");
	String to = ((endDate != null) ? Long.toString(endDate.getTime()) : "*");

	// If no begin date, begin is equal to "today"
	if ("*".equals(from)) {
	    // Search from the current date by default
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    Calendar cal = Calendar.getInstance(tz);
	    // reset hour, minutes, seconds and millis
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    cal.set(Calendar.MILLISECOND, 0);
	    from = Long.toString(cal.getTimeInMillis());
	}
	// From format sould be = 2000-01-01T00:00:00Z
	// http://wiki.apache.org/solr/FunctionQuery#ms
	SimpleDateFormat simpleDate = new SimpleDateFormat(
		"yyyy-MM-dd'T'hh:mm:ss'Z'");

	// Boost streams
	query.setParam(
		"bf",
		"recip(abs(ms("
			+ simpleDate.format(new Date(Long.valueOf(from)))
			+ ",start_date)),3.16e-11,1,1)");

	// Location filtering
	if ((getLatitude() != 0 || getLongitude() != 0)) {
	    query.addFilterQuery("{!geofilt sfield=latlng}");
	    query.add("pt", getLatitude() + "," + getLongitude());
	    query.add("d", "50");
	} else if (!StringUtils.isEmpty(location)) {
	    addLocation = true;
	}

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
	query.addFilterQuery(START_DATE_FIELD + ":[*" + " TO " + to + "}");
	query.addFilterQuery(END_DATE_FIELD + ":[" + from + " TO " + "*]");
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
	this.queryString = queryString == null ? "" : ClientUtils
		.escapeQueryChars(queryString);
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
    public void setStartDate(Date startDate) {
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
