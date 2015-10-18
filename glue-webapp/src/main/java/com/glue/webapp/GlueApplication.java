package com.glue.webapp;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.MediaType;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.Event;
import com.glue.persistence.CityDAO;
import com.glue.persistence.EventDAO;
import com.glue.webapp.beans.EventUtilBean;
import com.glue.webapp.logic.EventController;
import com.glue.webapp.logic.UserController;
import com.glue.webapp.search.SearchEngine;
import com.glue.webapp.search.SolrSearchServer;

//@ApplicationPath("services")
public class GlueApplication extends ResourceConfig {

    static final Logger LOG = LoggerFactory.getLogger(GlueApplication.class);

    private Map<String, MediaType> mediaTypeMap;

    public GlueApplication() {
	packages("com.glue.webapp.services");
	register(JacksonFeature.class);
	//http://stackoverflow.com/questions/18252990/uploading-file-using-jersey-over-restfull-service-and-the-resource-configuration
	register(MultiPartFeature.class);
	register(new DelegateBinder());
    }

    public Map<String, MediaType> getMediaTypeMappings() {
	if (mediaTypeMap == null) {
	    mediaTypeMap = new HashMap<String, MediaType>();
	    mediaTypeMap.put("json", MediaType.APPLICATION_JSON_TYPE);
	    mediaTypeMap.put("xml", MediaType.APPLICATION_XML_TYPE);
	    mediaTypeMap.put("txt", MediaType.TEXT_PLAIN_TYPE);
	    mediaTypeMap.put("html", MediaType.TEXT_HTML_TYPE);
	    mediaTypeMap.put("xhtml", MediaType.APPLICATION_XHTML_XML_TYPE);
	    MediaType jpeg = new MediaType("image", "jpeg");
	    mediaTypeMap.put("jpg", jpeg);
	    mediaTypeMap.put("jpeg", jpeg);
	    mediaTypeMap.put("zip", new MediaType("application",
		    "x-zip-compressed"));
	}
	return mediaTypeMap;
    }

    /**
     * A HK2 binder that simply delegates injection to the underlying CDI
     * provider. The binder allows to declare bindings only for high level
     * injections, i.e. objects in resource classes that are annotated with
     * <code>@Inject</code>. if those objects inject themselves other objects,
     * this is completely managed by the CDI provider.
     * 
     * @author pgillet
     * 
     */
    public class DelegateBinder extends AbstractBinder {

	@Override
	protected void configure() {
	    try {
		BeanManager bm = getBeanManager();
		bind(getBean(bm, UserController.class))
			.to(UserController.class)/* .in(Singleton.class) */; // ?
		bind(getBean(bm, EventController.class)).to(
			EventController.class)/* .in(Singleton.class) */; // ?
		bind(getBean(bm, SolrSearchServer.class)).to(
			new TypeLiteral<SearchEngine<Event>>() {
			});
		bind(getBean(bm, EventDAO.class)).to(EventDAO.class);
		bind(getBean(bm, EventUtilBean.class)).to(EventUtilBean.class);
		bind(getBean(bm, CityDAO.class)).to(CityDAO.class);
	    } catch (NamingException e) {
		LOG.error(e.getMessage(), e);
		throw new RuntimeException(e);
	    }
	}

	private BeanManager getBeanManager() throws NamingException {
	    BeanManager beanManager = InitialContext
		    .doLookup("java:comp/BeanManager");
	    return beanManager;
	}

	private <T> T getBean(BeanManager bm, Class<T> clazz) {
	    Bean<T> bean = (Bean<T>) bm.getBeans(clazz).iterator().next();
	    CreationalContext<T> ctx = bm.createCreationalContext(bean);
	    return (T) bm.getReference(bean, clazz, ctx);
	}
    }

}
