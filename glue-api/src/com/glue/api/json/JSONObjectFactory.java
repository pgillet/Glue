package com.glue.api.json;

import org.apache.http.HttpResponse;

import com.glue.api.model.GlueException;
import com.glue.api.model.Stream;

public interface JSONObjectFactory {

	Stream createStream(HttpResponse response) throws GlueException;

}
