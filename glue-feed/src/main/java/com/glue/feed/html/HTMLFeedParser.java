package com.glue.feed.html;

import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.feed.FeedMessageListener;
import com.glue.feed.FeedParser;

/**
 * Annotation-based HTML to Object Mapper using JSoup Parser.
 * 
 * @author pgillet
 * 
 * @param <T>
 */
public class HTMLFeedParser<T> implements FeedParser<T> {

    static final Logger LOG = LoggerFactory.getLogger(HTMLFeedParser.class);

    private final Class<T> classModel;
    private final String baseUri;

    // Pass in the class Java bean that will contain the mapped data from the
    // HTML source
    public HTMLFeedParser(final Class<T> classModel, final String baseUri) {
	this.classModel = classModel;
	this.baseUri = baseUri;
    }

    // Main method that will translate HTML to object
    @Override
    public void read() throws Exception {
	try {
	    Element rootElem = Jsoup.connect(baseUri).get();
	    T model = this.classModel.newInstance();

	    // Check if Selector annotation is present at the class level
	    if (this.classModel.isAnnotationPresent(Selector.class)) {
		final String cssQuery = classModel
			.getAnnotation(Selector.class).value();
		Elements elems = rootElem.select(cssQuery);
		if (!elems.isEmpty()) {
		    rootElem = elems.first();
		}
	    }

	    // Public fields
	    for (Field f : this.classModel.getFields()) {
		String value = null;
		// Check if Selector annotation is present in any of the methods
		if (f.isAnnotationPresent(Selector.class)) {
		    value = parseValue(rootElem, f);
		}

		if (value != null) {
		    f.set(model, convertValue(value, f));
		}
	    }

	    // Methods
	    for (Method m : this.classModel.getMethods()) {
		String value = null;
		// Check if Selector annotation is present in any of the methods
		if (m.isAnnotationPresent(Selector.class)) {
		    value = parseValue(rootElem, m);
		}

		if (value != null) {
		    m.invoke(model, convertValue(value, m));
		}
	    }

	    LOG.info(model.toString());

	} catch (Exception e) {
	    LOG.error(e.getMessage(), e);
	    throw e;
	}
    }

    private Object convertValue(final String value, final Field f) {

	Class<?> type = f.getType();

	// Set the parameter
	return toObject(type, value);
    }

    private Object convertValue(final String value, final Method m) {
	Class<?>[] parameterTypes = m.getParameterTypes();

	if (parameterTypes.length != 1) {
	    throw new IllegalArgumentException("Method " + m.getName()
		    + " is not a setter");
	}

	// Set the parameter
	return toObject(parameterTypes[0], value);
    }

    private Object toObject(Class<?> clazz, String value) {
	if (Boolean.class == clazz)
	    return Boolean.parseBoolean(value);
	if (Byte.class == clazz)
	    return Byte.parseByte(value);
	if (Short.class == clazz)
	    return Short.parseShort(value);
	if (Integer.class == clazz)
	    return Integer.parseInt(value);
	if (Long.class == clazz)
	    return Long.parseLong(value);
	if (Float.class == clazz)
	    return Float.parseFloat(value);
	if (Double.class == clazz)
	    return Double.parseDouble(value);
	return value;
    }

    private String parseValue(final Element ancestor, final AnnotatedElement m) {
	final String selector = m.getAnnotation(Selector.class).value();

	final Elements elems = ancestor.select(selector);

	if (!elems.isEmpty()) {
	    // no support for multiple selected elements yet. Just get the first
	    // element.
	    final Element elem = elems.first();

	    // Check which value annotation is present and retrieve data
	    // depending on the type of annotation
	    if (m.isAnnotationPresent(HtmlValue.class)) {
		return elem.html();
	    } else if (m.isAnnotationPresent(AttributeValue.class)) {
		return elem.attr(m.getAnnotation(AttributeValue.class).name());
	    } else /* if (m.isAnnotationPresent(TextValue.class)) */{
		// Default: text value
		return elem.text();
	    }
	}

	return null;
    }

    @Override
    public FeedMessageListener<T> getFeedMessageListener() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void close() throws IOException {
	// TODO Auto-generated method stub

    }

}
