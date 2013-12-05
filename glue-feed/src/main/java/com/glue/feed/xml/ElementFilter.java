package com.glue.feed.xml;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple filter that accepts elements with the specified name (ignoring
 * case), and all their children.
 * 
 * @author pgillet
 * 
 */
public class ElementFilter implements StreamFilter {

	static final Logger LOG = LoggerFactory.getLogger(ElementFilter.class);

	private String name;
	private boolean subElement;

	public ElementFilter(String name) {
		this.name = name;
	}

	/**
	 * Initializes the filter with the simple name of the given class.
	 * 
	 * @param name
	 */
	public ElementFilter(Class clazz) {
		this(clazz.getSimpleName());
	}

	@Override
	public boolean accept(XMLStreamReader reader) {
		boolean accepted = false;

		if (subElement) {
			accepted = true;
		} else if (reader.isStartElement()
				&& name.equalsIgnoreCase(reader.getLocalName())) {
			subElement = true;
			accepted = true;
		} else if (reader.isEndElement()
				&& name.equalsIgnoreCase(reader.getLocalName())) {
			subElement = false;
			accepted = true;
		}

		if (reader.isStartElement()) {
			LOG.debug("Accept element name '" + reader.getLocalName() + "' = "
					+ accepted);
		}

		return accepted;
	}

}
