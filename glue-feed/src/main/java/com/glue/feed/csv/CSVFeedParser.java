package com.glue.feed.csv;

import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.glue.feed.FeedMessageListener;
import com.glue.feed.FeedParser;
import com.glue.feed.error.ErrorDispatcher;
import com.glue.feed.error.ErrorHandler;
import com.glue.feed.error.ErrorLevel;
import com.glue.feed.error.ErrorListener;
import com.glue.feed.error.ErrorManager;
import com.glue.feed.listener.DefaultFeedMessageListener;

public class CSVFeedParser<T> implements ErrorHandler, ErrorManager,
		FeedParser<T> {

	static final Logger LOG = LoggerFactory.getLogger(CSVFeedParser.class);

	private FeedMessageListener<T> feedMessageListener = new DefaultFeedMessageListener<>();

	private ErrorDispatcher errorDispatcher = new ErrorDispatcher();

	private Reader reader;
	private Class<T> clazz;

	private MappingStrategy mappingStrategy;
	private CsvPreference csvPreference = CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE;

	public CSVFeedParser(Reader reader, Class<T> clazz) {
		this.reader = reader;
		this.clazz = clazz;
		this.mappingStrategy = new CamelCaseMappingStrategy(clazz);
	}

	@Override
	public void read() throws Exception {

		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(reader, csvPreference);

			// the header elements are used to map the values to the bean
			final String[] header = beanReader.getHeader(true);
			// final CellProcessor[] processors = getProcessors();

			// If no mapping strategy, the column names are the property names
			// in the bean class
			String[] nameMapping = header;
			if (mappingStrategy != null) {
				nameMapping = MappingUtils.getColumnMapping(header,
						mappingStrategy);
			}

			T msg = null;
			do {
				try {
					msg = beanReader.read(clazz, nameMapping);
					if (msg != null) {
						LOG.info(String.format("lineNo=%s, rowNo=%s, event=%s",
								beanReader.getLineNumber(),
								beanReader.getRowNumber(), msg));
						feedMessageListener.newMessage(msg);
					}
				} catch (Exception e) {
					LOG.error("A problem occured while reading at line %s: %s",
							beanReader.getLineNumber(),
							beanReader.getUntokenizedRow());
					LOG.error(e.getMessage(), e);

					errorDispatcher.fireErrorEvent(ErrorLevel.ERROR,
							e.getMessage(), e, "csv",
							beanReader.getLineNumber());
				}
			} while (msg != null);

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
	}

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

	/**
	 * Returns the strategy used to link the CSV columns to their corresponding
	 * field in the bean. CamelCaseMappingStrategy by default.
	 * 
	 * @return the mappingStrategy
	 * @see CamelCaseMappingStrategy
	 */
	public MappingStrategy getMappingStrategy() {
		return mappingStrategy;
	}

	/**
	 * Sets the strategy that will be used to link the CSV columns to their
	 * corresponding field in the bean.
	 * 
	 * @param mappingStrategy
	 *            the mappingStrategy to set
	 */
	public void setMappingStrategy(MappingStrategy mappingStrategy) {
		this.mappingStrategy = mappingStrategy;
	}

	/**
	 * @return the csvPreference
	 */
	public CsvPreference getCsvPreference() {
		return csvPreference;
	}

	/**
	 * @param csvPreference
	 *            the csvPreference to set
	 */
	public void setCsvPreference(CsvPreference csvPreference) {
		this.csvPreference = csvPreference;
	}

	@Override
	public void close() throws IOException {
		if (feedMessageListener != null) {
			feedMessageListener.close();
		}
		reader.close();
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

	@Override
	public void flush() throws IOException {
		errorDispatcher.flush();
	}

}
