package com.glue.db.index;

import java.io.Flushable;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;

public interface SolrDAO extends Flushable {

	SolrServer getSolrServer();

	/**
	 * Adds a document to be indexed.
	 * 
	 * @param doc
	 * @throws SolrServerException
	 * @throws IOException
	 */
	void addDoc(SolrInputDocument doc) throws SolrServerException, IOException;
	
}
