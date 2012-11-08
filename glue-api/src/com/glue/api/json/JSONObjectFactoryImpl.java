package com.glue.api.json;

import org.apache.http.HttpResponse;

import com.glue.api.model.GlueException;
import com.glue.api.model.Stream;

public class JSONObjectFactoryImpl implements JSONObjectFactory {

	@Override
	public Stream createStream(HttpResponse response) throws GlueException {
		return new StreamJSONImpl(response);
	}

}
