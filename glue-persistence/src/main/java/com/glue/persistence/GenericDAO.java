package com.glue.persistence;


public interface GenericDAO<T> {
    /**
     * Method that returns the number of entries from a table that meet some
     * criteria (where clause params)
     * 
     * @return the number of records meeting the criteria
     */
    long countAll();

    T create(T t);

    void delete(String id);

    T find(String id);

    T update(T t);
}
