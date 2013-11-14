package com.glue.webapp.search;

import java.util.NoSuchElementException;

import com.glue.webapp.logic.InternalServerException;

public interface PageIterator<E> {

	/**
	 * The default number of rows per result page.
	 */
	public static final int DEFAULT_ROWS = 10;

	/**
	 * Returns true if this iterator has more elements when traversing the
	 * results in the forward direction.
	 * 
	 * @return <code>true</code> if the iterator has more elements when
	 *         traversing the results in the forward direction
	 */
	boolean hasNext();

	/**
	 * Returns the next elements in the results and advances the cursor
	 * position.
	 * 
	 * @return the next elements in the results
	 * @throws NoSuchElementException
	 *             if the iteration has no next elements
	 * @throws InternalServerException
	 */
	E next() throws InternalServerException, NoSuchElementException;

	/**
	 * Returns true if this iterator has more elements when traversing the
	 * results in the reverse direction.
	 * 
	 * @return <code>true</code> if the iterator has more elements when
	 *         traversing the results in the reverse direction
	 */
	boolean hasPrevious();

	/**
	 * Returns the previous elements in the results and moves the cursor
	 * position backwards.
	 * 
	 * @return the previous elements in the results
	 * @throws NoSuchElementException
	 *             if the iteration has no previous elements
	 * @throws InternalServerException
	 */
	E previous() throws InternalServerException, NoSuchElementException;

	/**
	 * Returns the first elements in the results and moves the cursor position
	 * backwards.
	 * 
	 * @return the first elements in the results
	 * @throws InternalServerException
	 */
	E first() throws InternalServerException;

	/**
	 * Returns the last elements in the results and advances the cursor
	 * position.
	 * 
	 * @return the last elements in the results
	 * @throws InternalServerException
	 */
	E last() throws InternalServerException;

	/**
	 * 
	 * @param pageNumber
	 * @return
	 * @throws NoSuchElementException
	 */
	E get(int pageNumber) throws NoSuchElementException;

	/**
	 * Returns the offset (by default, 0) at which the iterator should begin
	 * returning responses.
	 * 
	 * @return
	 */
	int getStart();

	/**
	 * Specifies an offset (by default, 0) into the responses at which the
	 * iterator should begin returning content.
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
	int getRowsPerPage();

	/**
	 * Controls how many rows of responses are returned at a time (default
	 * value: 10).
	 * 
	 * @param rows
	 */
	void setRowsPerPage(int rows);

	/**
	 * Returns the total number of results.
	 * 
	 * @return the total number of results
	 */
	long getTotalRows();

	/**
	 * Returns the index of the current page.
	 * 
	 * @return the index of the current page.
	 */
	int getPageIndex();

	/**
	 * Returns the number of result pages. A page contains <code>rows</code>
	 * elements.
	 * 
	 * @return the number of result pages.
	 */
	int getTotalPages();

}
