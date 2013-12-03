package com.glue.feed.xml;

import com.glue.struct.IStream;

public interface StreamBuilder<T> {

	IStream buildStream(T obj) throws Exception;
	
}
