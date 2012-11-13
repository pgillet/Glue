package com.glue.api.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.glue.IStream;
import com.glue.api.conf.Configuration;
import com.glue.api.json.JSONObjectFactory;
import com.glue.api.json.JSONObjectFactoryImpl;
import com.glue.api.model.GlueException;

public class GlueImpl implements Glue {

	private static final long serialVersionUID = -8571499192744671742L;

	private Configuration conf;

	protected transient HttpClient http;

	protected JSONObjectFactory factory;

	GlueImpl(Configuration conf) {
		this.conf = conf;
		http = new DefaultHttpClient();
		factory = new JSONObjectFactoryImpl();
	}

	private HttpResponse post(String url, ArrayList<NameValuePair> parameters) throws GlueException {
		HttpResponse response = null;
		HttpPost httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(parameters));
			response = http.execute(httppost);
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
		return response;
	}

	protected final void ensureAuthorizationEnabled() {
		if (false) {
			throw new IllegalStateException("Authentication credentials are missing.");
		}
	}

	@Override
	public IStream createStream(String title) throws GlueException {
		ensureAuthorizationEnabled();
		ArrayList<NameValuePair> donnees = new ArrayList<NameValuePair>();
		donnees.add(new BasicNameValuePair("title", title));
		HttpResponse response = post(conf.getBaseUrl() + "streams/create.php", donnees);
		return factory.createStream(response);
	}

}
