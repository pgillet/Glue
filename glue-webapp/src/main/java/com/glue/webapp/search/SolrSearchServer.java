package com.glue.webapp.search;

import java.util.List;

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

	private final String DEFAULT_Q = "*:*";

	private SolrServer solr;

	public SolrSearchServer() {
		String urlString = "http://localhost:8080/solr"; // TODO: should be
															// configurable
		this.solr = new HttpSolrServer(urlString);
	}

	@Override
	public List<IStream> search(String str) throws InternalServerException {

		List<? extends IStream> items = null;

		String q = str.trim();
		if (q.length() == 0) {
			q = DEFAULT_Q;
		}

		SolrQuery query = new SolrQuery();
		query.setQuery(q);

		try {
			QueryResponse rsp = solr.query(query);
			items = rsp.getBeans(SolrStream.class);

		} catch (SolrServerException e) {
			throw new InternalServerException(e);
		}

		return (List<IStream>) items;
	}

}
