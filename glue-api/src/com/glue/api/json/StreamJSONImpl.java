package com.glue.api.json;

import static com.glue.api.json.JSONUtil.getUnescapedString;

import org.apache.http.HttpResponse;

import com.glue.api.external.org.json.JSONObject;
import com.glue.api.model.GlueException;
import com.glue.api.model.Stream;

public class StreamJSONImpl extends RootJSONImpl implements Stream {

	private String title;

	public StreamJSONImpl(HttpResponse response) throws GlueException {
		super(response);
	}

	protected void construct(JSONObject json) throws GlueException {
		title = getUnescapedString("title", json);
	}

	@Override
	public String getTitle() {
		return title;
	}

}
