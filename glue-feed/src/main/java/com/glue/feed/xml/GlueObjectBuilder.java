package com.glue.feed.xml;


public interface GlueObjectBuilder<T, V> {

	V build(T obj) throws Exception;
	
}
