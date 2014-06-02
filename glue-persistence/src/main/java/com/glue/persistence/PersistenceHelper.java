package com.glue.persistence;

import java.util.List;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PersistenceHelper {

    public static <T> T getSingleResultOrNull(Query query) {
	List<?> results = query.getResultList();
	if (results.isEmpty()) {
	    return null;
	} else if (results.size() == 1) {
	    return (T) results.get(0);
	}
	throw new NonUniqueResultException();
    }

    /**
     * Execute a SELECT query and return the first of the query results.
     * <p>
     * The given query is responsible for ordering the query results so that the
     * first result is the one wanted as a priority.
     * </p>
     * 
     * 
     * @param query
     * @return
     */
    public static <T> T getFirstResultOrNull(Query query) {
	List<?> results = query.getResultList();
	if (results.isEmpty()) {
	    return null;
	} else {
	    return (T) results.get(0);
	}
    }

    /**
     * Execute a SELECT query and return the first of the query results that
     * matches the filter, null otherwise.
     * 
     * @param query
     * @return
     */
    public static <T> T getFirstResultOrNull(TypedQuery<T> query,
	    PersistenceFilter<T> filter) {

	List<T> results = query.getResultList();
	if (!results.isEmpty()) {
	    for (T object : results) {
		if (filter.accept(object)) {
		    return object;
		}
	    }
	}

	return null;
    }
}
