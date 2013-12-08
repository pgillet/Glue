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

import com.glue.feed.FeedMessageListener;
import com.glue.feed.FeedParser;
import com.glue.feed.listener.DefaultFeedMessageListener;

public class XMLFeedParser<T> implements FeedParser<T> {

	static final Logger LOG = LoggerFactory.getLogger(XMLFeedParser.class);

	private FeedMessageListener<T> feedMessageListener = new DefaultFeedMessageListener<T>();

	XMLStreamReader xsr;
	Class<T> clazz;
	Unmarshaller unmarshaller;

	/**
	 * Equivalent to <code>this(reader, new ElementFilter(clazz), clazz)</code>.
	 * 
	 * @param reader
	 * @param clazz
	 * @throws XMLStreamException
	 * @throws FactoryConfigurationError
	 * @throws JAXBException
	 */
	public XMLFeedParser(Reader reader, Class<T> clazz)
			throws XMLStreamException, FactoryConfigurationError, JAXBException {
		this(reader, new ElementFilter(clazz), clazz);
	}

	public XMLFeedParser(Reader reader, StreamFilter filter, Class<T> clazz)
			throws XMLStreamException, FactoryConfigurationError, JAXBException {
		this.clazz = clazz;
		this.unmarshaller = JAXBContext.newInstance(clazz).createUnmarshaller();

		XMLInputFactory xmlif = XMLInputFactory.newInstance();
		this.xsr = xmlif.createFilteredReader(
				xmlif.createXMLStreamReader(reader), filter);
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

	public void close() throws IOException {
		if (feedMessageListener != null) {
			feedMessageListener.close();
		}
		try {
			xsr.close();
		} catch (XMLStreamException e) {
			LOG.error(e.getMessage(), e);
			throw new IOException(e);
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws NullPointerException
	 *             If a listener has not been set for this parser
	 */
	@Override
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

	private void skipElements(Integer... elements) throws XMLStreamException {
		int eventType = xsr.getEventType();

		List<Integer> types = Arrays.asList(elements);
		while (types.contains(eventType)) {
			eventType = xsr.next();
		}
	}

	/**
	 * @return the feedMessageListener
	 */
	@Override
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
