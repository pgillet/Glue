package com.glue.api.operations;

import com.glue.api.model.GlueException;
import com.glue.struct.IStream;

public interface StreamOperations {

	IStream createStream(String title) throws GlueException;

}
