package com.glue.bot;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Annotation-based HTML to Object Mapper using JSoup Parser.
 * 
 * @author pgillet
 * 
 * @param <T>
 */
public class Crawler<T> implements Extractor {

    static final Logger LOG = LoggerFactory.getLogger(Crawler.class);

    private BrowsingStrategy browsingStrategy;
    private HtmlMapper<T> mappingStrategy;

    /**
     * For JSON pretty printing.
     */
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Pass in the class Java bean that will contain the mapped data from the
     * HTML source. The HTML source is described within the given site map, and
     * is traversed according to a PaginatedListStrategy.
     * 
     * @param classModel
     * @param siteMap
     */
    public Crawler(Class<T> classModel, SiteMap siteMap) {
	this.browsingStrategy = new PaginatedListStrategy(siteMap);
	this.browsingStrategy.setExtractor(this);

	this.mappingStrategy = new AnnotationMappingStrategy<T>(classModel);
    }

    public Crawler(SiteMap siteMap, HtmlMapper<T> mappingStrategy) {
	this.browsingStrategy = new PaginatedListStrategy(siteMap);
	this.browsingStrategy.setExtractor(this);

	this.mappingStrategy = mappingStrategy;
    }

    /**
     * Runs this spider.
     * 
     * @throws Exception
     */
    public void run() throws Exception {
	browsingStrategy.browse();
    }

    /**
     * Callback method from the <{@link BrowsingStrategy}.
     */
    @Override
    public void process(Element e) throws Exception {

	try {
	    T obj = mappingStrategy.parse(e);

	    if (obj != null) {
		String jsonObj = gson.toJson(obj);
		LOG.info("Item parsed = " + jsonObj);
	    }
	} catch (Exception ex) {
	    LOG.error(ex.getMessage(), ex);
	    throw ex;
	}
    }

}
