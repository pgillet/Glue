package com.glue.feed.csv;

import java.beans.PropertyDescriptor;

public class MappingUtils {

	public static String[] getColumnMapping(String[] capturedHeader,
			MappingStrategy strategy) {

		int len = capturedHeader.length;
		String[] propertyNames = new String[len];

		for (int i = 0; i < len; i++) {
			PropertyDescriptor descriptor = strategy
					.findDescriptor(capturedHeader[i]);
			if (descriptor != null) {
				propertyNames[i] = descriptor.getName();
			}
		}

		return propertyNames;
	}

}
