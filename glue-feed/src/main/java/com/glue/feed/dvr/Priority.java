package com.glue.feed.dvr;

/**
 * Class Priority defines an integer valued attribute that specifies a object's
 * priority relative to another object of the same type. A higher value
 * specifies a higher priority.
 * 
 * @author pgillet
 * 
 */
public interface Priority<T> {

    /**
     * Returns the priority value.
     * 
     * @return an int value.
     */
    int getValue(T obj);

}