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
import com.glue.feed.error.ErrorDispatcher;
import com.glue.feed.error.ErrorHandler;
import com.glue.feed.error.ErrorLevel;
import com.glue.feed.error.ErrorListener;
import com.glue.feed.error.ErrorManager;
import com.glue.feed.listener.DefaultFeedMessageListener;

public class XMLFeedParser<T> implements ErrorHandler, ErrorManager,
		FeedParser<T> {

	static final Logger LOG = LoggerFactory.getLogger(XMLFeedParser.class);

	private FeedMessageListener<T> feedMessageListener = new DefaultFeedMessageListener<T>();

	private ErrorDispatcher errorDispatcher = new ErrorDispatcher();

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
		int num = 0;

		while (xsr.hasNext()) {
			try {
				T msg = next();
				num++;
				feedMessageListener.newMessage(msg);
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				errorDispatcher.fireErrorEvent(ErrorLevel.ERROR,
						e.getMessage(), e, "xml", num);
			}
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

	@Override
	public void flush() throws IOException {
		errorDispatcher.flush();
	}

	@Override
	public ErrorListener[] getErrorListeners() {
		return errorDispatcher.getErrorListeners();
	}

	@Override
	public void addErrorListener(ErrorListener l) {
		errorDispatcher.addErrorListener(l);
	}

	@Override
	public void removeErrorListener(ErrorListener l) {
		errorDispatcher.removeErrorListener(l);
	}

	@Override
	public void fireErrorEvent(ErrorLevel lvl, String message, Throwable cause,
			String source, int lineNumber) {
		errorDispatcher.fireErrorEvent(lvl, message, cause, source, lineNumber);
	}

}
