package com.glue.webapp.search;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.glue.webapp.logic.InternalServerException;

public interface SearchEngine<T> {

    /**
     * Performs an autocomplete query .
     * 
     * @return a list of T.
     * @throws InternalServerException
     *             if communication or parsing issues occurred while talking to
     *             the search engine
     */
    List<T> searchForAutoComplete(String query) throws InternalServerException;

    /**
     * Performs a query to the search engine with all the sent parameters.
     * 
     * @return the <code>rows</code> elements from <code>start</code> offset.
     * @throws InternalServerException
     *             if communication or parsing issues occurred while talking to
     *             the search engine
     */
    List<T> search() throws InternalServerException;

    /**
     * Returns a map for which the <code>Map.values()</code> are the elements
     * returned by {@link #search()} in the given order, and each key is the id
     * of the corresponding value.
     * 
     * @return a map mapping the id on each value in the collection returned by
     *         {@link #search()}
     * @throws InternalServerException
     */
    Map<?, T> searchAsMap() throws InternalServerException;

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
    void setStartTime(Date startDate);

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
     * Returns the total number of found documents from the last search.
     * 
     * @return the total number of found documents from the last search
     */
    long getNumFound();

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
     * Returns the number of responses that should be returned at a time.
     * 
     * @return the number of responses that should be returned at a time
     */
    int getRows();

    /**
     * Controls how many rows of responses are returned at a time.
     * 
     * @param rows
     */
    void setRows(int rows);

    /**
     * Returns the selected categories.
     * 
     * @return
     */
    String[] getCategories();

    /**
     * Sets the wanted categories
     * 
     * @param categories
     */
    void setCategories(String[] categories);

    /**
     * Sets the latitude
     * 
     * @param latitude
     */
    public void setLatitude(double latitude);

    /**
     * Returns the latitude.
     * 
     * @return
     */
    public double getLatitude();

    /**
     * Sets the longitude
     * 
     * @param longitude
     */
    public void setLongitude(double longitude);

    /**
     * Sets the location
     * 
     * @return
     */
    public void setLocation(String location);

    /**
     * Returns the location.
     * 
     * @return
     */
    public String getLocation();

}
