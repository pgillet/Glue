package com.glue.api.json;

import org.apache.http.HttpResponse;

import com.glue.IStream;
import com.glue.api.model.GlueException;

public class JSONObjectFactoryImpl implements JSONObjectFactory {

	@Override
	public IStream createStream(HttpResponse response) throws GlueException {
		return new StreamJSONImpl(response);
	}

}
