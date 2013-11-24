package com.glue.webapp.search;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.struct.IStream;
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

	private static final String END_DATE_FIELD = "end_date";

	private final String DEFAULT_Q = "*:*";

	private SolrServer solr;

	public SolrSearchServer() {
		String urlString = "http://localhost:8080/solr"; // TODO: should be
															// configurable
		this.solr = new HttpSolrServer(urlString);
	}

	@Override
	public List<IStream> search() throws InternalServerException {

		List<? extends IStream> items = null;

		SolrQuery query = constructSolrQuery();

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

	private SolrQuery constructSolrQuery() {
		SolrQuery query = new SolrQuery();

		// Get dates
		String begin = ((startDate != null) ? Long.toString(startDate.getTime()) : "*");
		String end = ((endDate != null) ? Long.toString(endDate.getTime()) : "*");

		if (isFullQuery()) {

			// Specific boost calculation for empty queries
			query.setQuery("{!boost b=$bfunction v=$qq}");

			// If no begin date, begin is equal to "today"
			if ("*".equals(begin)) {
				// Search from the current date by default
				TimeZone tz = TimeZone.getTimeZone("UTC");
				Calendar cal = Calendar.getInstance(tz);
				// reset hour, minutes, seconds and millis
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				begin = Long.toString(cal.getTimeInMillis());
			}

			// 5.787037e-10 = 1 / (30 days)
			// 3.8580247e-11 = 1 / (300 days)
			// product of 2 functions =
			// 1 - end_date close to today
			// 2 - start_date has already started
			query.add("bfunction",
					"product(recip(abs(ms(NOW/DAY,end_date)),5.787037e-10,1,1),recip(ms(start_date,NOW/DAY),3.8580247e-11,1,2))");
			query.add("qq", DEFAULT_Q);

		}

		// If we are looking for something specific, no boost!
		else {
			query.setQuery(queryString);
			// query.addSort(END_DATE_FIELD, ORDER.desc);
		}
		if (categories != null && categories.length > 0) {
			query.addFilterQuery("category:" + constructCategoriesFilter());
		}
		query.addFilterQuery(END_DATE_FIELD + ":[" + begin + " TO " + end + "]");
		query.setStart(start);
		query.setRows(rows);
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
				|| ("".equals(queryString) || ("*".equals(queryString)) || ("*:*").equals(queryString));
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
		this.queryString = queryString;
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

	private String constructCategoriesFilter() {
		String result = "";
		// Add first category
		if (categories.length > 0) {
			result += categories[0];
		}
		// For each other categories
		for (int i = 1; i < categories.length; i++) {
			result += " or " + categories[i];
		}
		return result;
	}
}
