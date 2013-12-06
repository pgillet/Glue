package com.glue.feed.csv;

import java.beans.PropertyDescriptor;

public interface MappingStrategy {

	/**
	 * Returns a property descriptor from a bean based on the given column
	 * number.
	 * 
	 * @param columnName
	 * @return
	 */
	PropertyDescriptor findDescriptor(int col);

	/**
	 * Returns a property descriptor from a bean based on the given column name.
	 * 
	 * @param columnName
	 * @return
	 */
	PropertyDescriptor findDescriptor(String columnName);

}
