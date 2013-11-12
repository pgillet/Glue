package com.glue.webapp.search;

import java.util.Date;
import java.util.List;

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;

public interface SearchEngine {

	public static final int DEFAULT_ROWS = 10;

	List<IStream> search() throws InternalServerException;

	/**
	 * Returns the query string.
	 * 
	 * @return
	 */
	String getQueryString();

	/**
	 * Sets the query string.
	 * 
	 * @param queryString
	 */
	void setQueryString(String queryString);

	/**
	 * Returns the start date.
	 * 
	 * @return
	 */
	Date getStartDate();

	/**
	 * Sets the start date.
	 * 
	 * @param startDate
	 */
	void setStartDate(Date startDate);

	/**
	 * Returns the end date.
	 * 
	 * @return
	 */
	Date getEndDate();

	/**
	 * Sets the end date.
	 * 
	 * @param endDate
	 */
	void setEndDate(Date endDate);

	/**
	 * Returns the offset (by default, 0) at which the search engine should
	 * begin returning responses.
	 * 
	 * @return
	 */
	int getStart();

	/**
	 * Specifies an offset (by default, 0) into the responses at which the
	 * search engine should begin returning content.
	 * 
	 * @param start
	 */
	void setStart(int start);

	/**
	 * Returns the number of responses that should be returned at a time
	 * (default value: 10)
	 * 
	 * @return
	 */
	int getRows();

	/**
	 * Controls how many rows of responses are returned at a time (default
	 * value: 10).
	 * 
	 * @param rows
	 */
	void setRows(int rows);

}
