package com.glue.api.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.glue.api.conf.Configuration;
import com.google.gson.Gson;

public class HttpHelper {

	protected static <T> T sendGlueObject(HttpClient http, Configuration conf, T glueObject, Type objectType, String url) {
		T result = null;

		// JSON
		Gson gson = new Gson();
		String gsonStream = gson.toJson(glueObject);

		// Send it
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(conf.getBaseUrl() + url);
			StringEntity entity = new StringEntity(gsonStream);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(entity);
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				gsonStream = EntityUtils.toString(response.getEntity());
				if (gsonStream != null) {
					result = gson.fromJson(gsonStream, objectType);
				}
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

}
