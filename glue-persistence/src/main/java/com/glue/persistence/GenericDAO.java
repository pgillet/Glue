package com.glue.persistence;

import java.util.Map;

public interface GenericDAO<T> {
    /**
     * Method that returns the number of entries from a table that meet some
     * criteria (where clause params)
     * 
     * @param params
     *            sql parameters
     * @return the number of records meeting the criteria
     */
    long countAll(Map<String, Object> params);

    T create(T t);

    void delete(String id);

    T find(String id);

    T update(T t);
}
