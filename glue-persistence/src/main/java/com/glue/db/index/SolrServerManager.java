package com.glue.db.index;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;

public class SolrServerManager {
	
	private static SolrServer solrServer;

	public synchronized SolrServer getSolrServer() {

		if (solrServer == null) {
			solrServer = createNewSolrServer();
		}

		return solrServer;
	}

	private SolrServer createNewSolrServer() {
		// TODO: should be configurable
		final String solrServerUrl = "http://localhost:8080/solr";
		int queueSize = 1000;
		int threadCount = 5;

		// Thread-safe
		return new ConcurrentUpdateSolrServer(solrServerUrl, queueSize,
				threadCount);
	}

}
