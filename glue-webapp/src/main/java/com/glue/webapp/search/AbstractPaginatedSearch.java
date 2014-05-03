package com.glue.webapp.search;

import java.io.Serializable;
import java.util.NoSuchElementException;

public abstract class AbstractPaginatedSearch<E> implements PageIterator<E>,
	Serializable {

    protected int start;

    protected int rowsPerPage = PageIterator.DEFAULT_ROWS;

    protected long totalRows;

    /**
     * Executes a search with the maximum number of results and the position of
     * the first result this query object was set to retrieve.
     * 
     * <p>
     * This method is responsible for updating the pagination info once the
     * search is done, typically the total number of results.
     * </p>
     * 
     * @return The <i>rowsPerPage</i> elements at the given start position
     * @throws Exception
     * @see {@link #getRowsPerPage()}
     * @see #getStart()
     */
    public abstract E search() throws Exception;

    public boolean hasNext() {
	return getPageIndex() < (getTotalPages() - 1);
    }

    public E next() throws Exception, NoSuchElementException {
	if (!hasNext()) {
	    throw new NoSuchElementException();
	}

	start += rowsPerPage;
	return search();
    }

    public boolean hasPrevious() {
	return getPageIndex() > 0;
    }

    public E previous() throws Exception, NoSuchElementException {
	if (!hasPrevious()) {
	    throw new NoSuchElementException();
	}

	start -= rowsPerPage;
	return search();
    }

    public E first() throws Exception {
	start = 0;
	return search();
    }

    public E last() throws Exception {
	start = (getTotalPages() - 1) * rowsPerPage; // last page index
	return search();
    }

    public E get(int pageNumber) throws Exception, NoSuchElementException {
	if (pageNumber < 1 || pageNumber > getTotalPages()) {
	    throw new NoSuchElementException();
	}

	start = (pageNumber - 1) * rowsPerPage;
	return search();
    }

    public int getStart() {
	return start;
    }

    public void setStart(int start) {
	this.start = start;
    }

    public int getRowsPerPage() {
	return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
	this.rowsPerPage = rowsPerPage;
    }

    public long getTotalRows() {
	return totalRows;
    }

    public void setTotalRows(long totalRows) {
	this.totalRows = totalRows;
    }

    public int getPageIndex() {
	return start / rowsPerPage;
    }

    public int getTotalPages() {
	int num = (int) totalRows / rowsPerPage;
	if (totalRows % rowsPerPage > 0) {
	    num++;
	}

	return num;
    }

}
