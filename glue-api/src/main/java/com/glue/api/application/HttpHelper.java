package com.glue.api.application;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.glue.api.conf.Configuration;
import com.google.gson.Gson;

public class HttpHelper {

	protected static <T> T sendGlueObject(HttpClient http, T glueObject, Type objectType, String url) {
		T result = null;

		// JSON
		Gson gson = new Gson();
		String gsonStream = gson.toJson(glueObject);

		// Send it
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(Configuration.getBaseUrl() + url);
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

	protected static <T> T sendGlueObject(HttpClient http, T glueObject, Type objectType, String url, InputStream input) {

		T result = null;

		// JSON
		Gson gson = new Gson();
		String gsonStream = gson.toJson(glueObject);

		// Send it
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit

		MultipartEntity multipartEntity = new MultipartEntity();

		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(Configuration.getBaseUrl() + url);
			FormBodyPart jsonPart = new FormBodyPart("json", new StringBody(gsonStream));
			multipartEntity.addPart(jsonPart);
			FormBodyPart filePart = new FormBodyPart("file", new InputStreamBody(input, "jsonFile"));
			multipartEntity.addPart(filePart);
			post.setEntity(multipartEntity);
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

	protected static String send(HttpClient http, Map<String, String> params, String url) {
		String result = null;

		// Http connection
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(Configuration.getBaseUrl() + url);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity());
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
