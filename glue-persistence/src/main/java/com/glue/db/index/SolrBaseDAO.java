package com.glue.db.index;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrBaseDAO implements SolrDAO {

	static final Logger LOG = LoggerFactory.getLogger(SolrBaseDAO.class);

	private SolrServer solrServer;

	private Queue<SolrInputDocument> docs = new LinkedBlockingQueue<>(100);

	/**
	 * @return the solrServer
	 */
	@Override
	public SolrServer getSolrServer() {
		return solrServer;
	}

	/**
	 * @param solrServer
	 *            the solrServer to set
	 */
	public void setSolrServer(SolrServer solrServer) {
		this.solrServer = solrServer;
	}

	@Override
	public void addDoc(SolrInputDocument doc) throws SolrServerException,
			IOException {
		if (!docs.offer(doc)) {
			solrServer.add(docs);
			solrServer.commit();
			docs.clear();
			docs.add(doc);
		}
	}

	@Override
	public void flush() throws IOException {
		try {
			solrServer.add(docs);
			solrServer.commit();
		} catch (SolrServerException e) {
			LOG.error(e.getMessage(), e);
			throw new IOException(e);
		}
	}

}
