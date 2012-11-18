package com.glue.api.json;

import org.apache.http.HttpResponse;

import com.glue.api.model.GlueException;
import com.glue.struct.IStream;

public interface JSONObjectFactory {

	IStream createStream(HttpResponse response) throws GlueException;

}
