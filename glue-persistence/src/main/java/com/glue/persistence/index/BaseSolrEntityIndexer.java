package com.glue.persistence.index;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSolrEntityIndexer implements SolrEntityIndexer {

    static final Logger LOG = LoggerFactory
	    .getLogger(BaseSolrEntityIndexer.class);

    private Queue<SolrInputDocument> docs = new LinkedBlockingQueue<>(100);

    private boolean autoFlush = false;

    /**
     * @return the solrServer
     */
    @Override
    public SolrServer getSolrServer() {
	return SolrServerManager.getSolrServer();
    }

    @Override
    public void addDoc(SolrInputDocument doc) throws SolrServerException,
	    IOException {
	if (!docs.offer(doc)) {
	    flush();
	    docs.add(doc);
	}
	if (autoFlush) {
	    flush();
	}
    }

    @Override
    public void flush() throws IOException {
	try {
	    if (!docs.isEmpty()) {
		getSolrServer().add(docs);
		getSolrServer().commit();
		docs.clear();
	    }
	} catch (SolrServerException e) {
	    LOG.error(e.getMessage(), e);
	    throw new IOException(e);
	}
    }

}
