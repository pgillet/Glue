package com.glue.api.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.http.HttpResponse;

import com.glue.api.external.org.json.JSONException;
import com.glue.api.external.org.json.JSONObject;
import com.glue.api.external.org.json.JSONTokener;
import com.glue.api.model.GlueException;

public abstract class RootJSONImpl {

	private JSONObject json = null;

	private HttpResponse response;

	public RootJSONImpl(HttpResponse response) throws GlueException {
		this.response = response;
		json = asJSONObject();
		construct(json);
	}

	protected abstract void construct(JSONObject json) throws GlueException;

	private JSONObject asJSONObject() throws GlueException {
		if (json == null) {
			Reader reader = null;
			try {
				reader = asReader();
				json = new JSONObject(new JSONTokener(reader));

			} catch (JSONException jsone) {
				jsone.printStackTrace();
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException ignore) {
					}
				}
			}
		}
		return json;
	}

	private Reader asReader() {
		InputStream is = null;
		try {
			is = response.getEntity().getContent();
			return new BufferedReader(new InputStreamReader(is, "UTF-8"));
		} catch (java.io.UnsupportedEncodingException uee) {
			return new InputStreamReader(is);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
