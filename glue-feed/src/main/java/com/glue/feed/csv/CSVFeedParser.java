package com.glue.feed.csv;

import java.io.IOException;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.glue.feed.DefaultFeedMessageListener;
import com.glue.feed.FeedMessageListener;
import com.glue.feed.FeedParser;

public class CSVFeedParser<T> implements FeedParser<T> {

	static final Logger LOG = LoggerFactory.getLogger(CSVFeedParser.class);

	private FeedMessageListener<T> feedMessageListener = new DefaultFeedMessageListener<>();

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
		reader.close();
	}

}
