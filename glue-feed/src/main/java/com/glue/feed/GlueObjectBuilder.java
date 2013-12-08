package com.glue.feed;


public interface GlueObjectBuilder<T, V> {

	V build(T obj) throws Exception;
	
}
