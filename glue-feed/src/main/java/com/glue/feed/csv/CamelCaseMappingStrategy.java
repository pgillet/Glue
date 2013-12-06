package com.glue.feed.csv;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CamelCaseMappingStrategy implements MappingStrategy {

	static final Logger LOG = LoggerFactory
			.getLogger(CamelCaseMappingStrategy.class);

	Class<?> beanClass;

	public CamelCaseMappingStrategy(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	/**
	 * This strategy always returns null.
	 */
	public PropertyDescriptor findDescriptor(int col) {
		return null;
	}

	@Override
	public PropertyDescriptor findDescriptor(String columnName) {

		PropertyDescriptor descriptor = null;
		String[] tokens = columnName.split("[\\s\\p{Punct}]"); // A space or
																// punctuation
																// character
		if (tokens.length > 0) {
			tokens[0] = tokens[0].toLowerCase();
			for (int i = 1; i < tokens.length; i++) {
				tokens[i] = WordUtils.capitalizeFully(tokens[i]);
			}

			String propertyName = StringUtils.join(tokens);

			try {
				descriptor = new PropertyDescriptor(propertyName, beanClass);
			} catch (IntrospectionException e) {
				LOG.debug("Property " + propertyName + " not found in "
						+ beanClass.getName());
			}
		}

		return descriptor;
	}

}
