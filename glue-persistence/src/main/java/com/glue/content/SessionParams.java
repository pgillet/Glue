package com.glue.content;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SessionParams {
    
    static final Logger LOG = LoggerFactory.getLogger(SessionParams.class);

    private static final String CMIS_PROPERTIES = "/com/glue/content/cmis.properties";

    private static final String GLUE_CONTENT_CMIS_ATOMPUB_URL = "glue.content.cmis.atompub.url";
    private static final String GLUE_CONTENT_CMIS_EXTERNAL_INET_ADDRESS = "glue.content.cmis.external.inet.address";
    private static final String GLUE_CONTENT_CMIS_USER = "glue.content.cmis.user";
    private static final String GLUE_CONTENT_CMIS_PASSWORD = "glue.content.cmis.password";

    private static String atompubUrl;
    private static String externalInetAddress;
    private static String user;
    private static String password;

    static {
	// create and load default properties
	Properties props = new Properties();
	try (InputStream in = SessionParams.class
		.getResourceAsStream(CMIS_PROPERTIES)) {
	    props.load(in);

	    atompubUrl = props.getProperty(GLUE_CONTENT_CMIS_ATOMPUB_URL);
	    externalInetAddress = props
		    .getProperty(GLUE_CONTENT_CMIS_EXTERNAL_INET_ADDRESS);
	    user = props.getProperty(GLUE_CONTENT_CMIS_USER);
	    password = props.getProperty(GLUE_CONTENT_CMIS_PASSWORD);
	} catch (IOException e) {
	    LOG.error(e.getMessage(), e);
	    throw new RuntimeException(e);
	}
    }

    /**
     * @return the atompubUrl
     */
    protected static String getAtompubUrl() {
	return atompubUrl;
    }

    /**
     * @return the externalInetAddress
     */
    protected static String getExternalInetAddress() {
	return externalInetAddress;
    }

    /**
     * @return the user
     */
    protected static String getUser() {
	return user;
    }

    /**
     * @return the password
     */
    protected static String getPassword() {
	return password;
    }

}
