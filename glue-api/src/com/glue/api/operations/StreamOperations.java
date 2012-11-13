package com.glue.api.operations;

import com.glue.IStream;
import com.glue.api.model.GlueException;

public interface StreamOperations {

	IStream createStream(String title) throws GlueException;

}
