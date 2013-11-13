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

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;

/**
 * A facade for a Solr server.
 * 
 * @author pgillet
 * 
 */
public class SolrSearchServer implements SearchEngine {

	private String queryString;

	private Date startDate;

	private Date endDate;

	private int start;

	private int rows = DEFAULT_ROWS;

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

		SolrQuery query = new SolrQuery();
		query.setQuery("{!boost b=$bfunction v=$qq}");

		// Date criteria
		Number min = null;
		if (startDate != null) {
			min = startDate.getTime();
		} else {
			// Search from the current date by default
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);
			// reset hour, minutes, seconds and millis
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			min = cal.getTimeInMillis();
		}

		String max = ((endDate != null) ? Long.toString(endDate.getTime()) : "*");

		query.addFilterQuery(END_DATE_FIELD + ":[" + min + " TO " + max + "]");

		// 5.787037e-10 = 1 month
		// 2 boost functions =
		// 1 - end_date close to today
		// 2 - start_date has already started
		query.add("bfunction",
				"sum(recip(abs(ms(NOW/DAY,end_date)),5.787037e-10,1,1),scale(ms(NOW/DAY,start_date),0,1))");
		query.add("qq", queryString.trim().length() == 0 ? DEFAULT_Q : queryString.trim());

		// Temp
		query.setRows(1000);

		try {
			QueryResponse rsp = solr.query(query);
			items = rsp.getBeans(SolrStream.class);

		} catch (SolrServerException e) {
			throw new InternalServerException(e);
		}

		return (List<IStream>) items;
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

	/**
	 * @param start
	 *            the start to set
	 */
	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public int getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            the rows to set
	 */
	@Override
	public void setRows(int rows) {
		this.rows = rows;
	}

}
