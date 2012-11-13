package com.glue.api.json;

import org.apache.http.HttpResponse;

import com.glue.IStream;
import com.glue.api.model.GlueException;

public interface JSONObjectFactory {

	IStream createStream(HttpResponse response) throws GlueException;

}
