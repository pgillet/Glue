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

	private static final String START_DATE_FIELD = "start_date";

	private final String DEFAULT_Q = "*:*";

	private SolrServer solr;

	public SolrSearchServer() {
		String urlString = "http://localhost:8080/solr"; // TODO: should be
															// configurable
		this.solr = new HttpSolrServer(urlString);
	}

	@Override
	public List<IStream> search(String str, Date start, Date end) throws InternalServerException {

		List<? extends IStream> items = null;

		String q = str.trim();
		if (q.length() == 0) {
			q = DEFAULT_Q;
		}

		SolrQuery query = new SolrQuery();
		query.setQuery(q);

		// Date criteria
		Number min = null;
		if (start != null) {
			min = start.getTime();
		} else {
			// Search from the current date by default
			TimeZone tz = TimeZone.getTimeZone("UTC");
			Calendar cal = Calendar.getInstance(tz);
			// reset hour, minutes, seconds and millis
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			System.out.println("DATE = " + cal.toString());

			min = cal.getTimeInMillis();
		}

		Number max = ((end != null) ? end.getTime() : Long.MAX_VALUE);

		query.addFilterQuery(START_DATE_FIELD + ":[" + min + " TO " + max + "]");

		// Sort
		query.addSort(START_DATE_FIELD, SolrQuery.ORDER.asc);

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

}
