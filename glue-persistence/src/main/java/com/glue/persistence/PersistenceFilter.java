package com.glue.persistence;

/**
 * A filter for objects returned by criteria queries.
 * 
 * <p>
 * Such a filter may be used to filter the query results returned by a SELECT
 * query where the filtering cannot be easily expressed using the Criteria API.
 * </p>
 * 
 * @author pgillet
 * 
 * @param <T>
 */
public interface PersistenceFilter<T> {

    /**
     * Tests whether or not the specified object should be included in a result
     * list.
     * 
     * @param obj
     * @return true if and only if <code>obj</code> should be included.
     */
    boolean accept(T obj);

}
