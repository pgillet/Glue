package com.glue.persistence.index;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SolrParams {
    
    static final Logger LOG = LoggerFactory.getLogger(SolrParams.class);

    private static final String SOLR_PROPERTIES = "/com/glue/persistence/index/solr.properties";

    private static final String SOLR_SERVER_URL = "solr.server.url";

    private static String solrServerUrl;

    static {
	// create and load default properties
	Properties props = new Properties();
	try (InputStream in = SolrParams.class
		.getResourceAsStream(SOLR_PROPERTIES)) {
	    props.load(in);

	    solrServerUrl = props.getProperty(SOLR_SERVER_URL);

	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	    throw new RuntimeException(e);
	}
    }

    /**
     * @return the solrServerUrl
     */
    public static String getSolrServerUrl() {
	return solrServerUrl;
    }

}
