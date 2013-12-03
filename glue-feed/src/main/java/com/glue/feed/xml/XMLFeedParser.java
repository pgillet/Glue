package com.glue.feed.xml;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XMLFeedParser<T> {

	static final Logger LOG = LoggerFactory.getLogger(XMLFeedParser.class);

	private FeedMessageListener<T> feedMessageListener;

	XMLStreamReader xsr;
	Class<T> clazz;
	Unmarshaller unmarshaller;

	public XMLFeedParser(Reader reader, StreamFilter filter, Class<T> clazz)
			throws XMLStreamException, FactoryConfigurationError, JAXBException {
		this.clazz = clazz;
		this.unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();

		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		this.xsr = xmlif.createFilteredReader(
				xmlif.createXMLStreamReader(reader), filter);

		// Ignore headers
		//skipElements(XMLEvent.START_DOCUMENT, XMLEvent.DTD, XMLEvent.SPACE);
		// Ignore root element
//		xsr.nextTag();
		// If there's no tag, ignore root element's end
		//skipElements(XMLStreamReader.END_ELEMENT);
	}

	public T next() throws XMLStreamException, JAXBException {
		if (!hasNext())
			throw new NoSuchElementException();

		T value = unmarshaller.unmarshal(xsr, clazz).getValue();

		skipElements(XMLEvent.SPACE, XMLEvent.CHARACTERS, XMLEvent.END_ELEMENT);
		return value;
	}

	public boolean hasNext() throws XMLStreamException {
		return xsr.hasNext();
	}

	public void close() throws XMLStreamException {
		xsr.close();
	}

	/**
	 * 
	 * @throws IOException
	 * @throws NullPointerException
	 *             If a listener has not been set for this parser
	 */
	public void read() throws Exception {
		try {
			while (xsr.hasNext()) {
				T msg = next();
				feedMessageListener.newMessage(msg);
			}
		} catch (XMLStreamException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		} catch (JAXBException e) {
			LOG.error(e.getMessage(), e);
			throw e;
		}

	}

	void skipElements(Integer... elements) throws XMLStreamException {
		int eventType = xsr.getEventType();

		List<Integer> types = Arrays.asList(elements);
		while (types.contains(eventType)) {
			eventType = xsr.next();
		}
	}

	/**
	 * @return the feedMessageListener
	 */
	public FeedMessageListener<T> getFeedMessageListener() {
		return feedMessageListener;
	}

	/**
	 * @param feedMessageListener
	 *            the feedMessageListener to set
	 */
	public void setFeedMessageListener(
			FeedMessageListener<T> feedMessageListener) {
		this.feedMessageListener = feedMessageListener;
	}

}
