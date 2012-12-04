package com.glue.api.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.glue.api.conf.Configuration;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;
import com.glue.struct.impl.dto.StreamDTO;
import com.google.gson.Gson;

public class GlueImpl implements Glue {

	private static final long serialVersionUID = -8571499192744671742L;

	private Configuration conf;

	protected transient HttpClient http;

	GlueImpl(Configuration conf) {
		this.conf = conf;
		http = new DefaultHttpClient();
	}

	protected final void ensureAuthorizationEnabled() {
		if (false) {
			throw new IllegalStateException("Authentication credentials are missing.");
		}
	}

	@Override
	public IStream createStream(String title, String description, boolean publicc, boolean open,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException {

		IStream result = null;

		ensureAuthorizationEnabled();

		// Create Stream DTO
		StreamDTO aStream = new StreamDTO();
		aStream.setTitle(title);
		aStream.setDescription(description);
		aStream.setPublicc(publicc);
		aStream.setOpen(open);
		aStream.setSharedSecretQuestion(sharedSecretQuestion);
		aStream.setSharedSecretAnswer(sharedSecretAnswer);
		aStream.setShouldRequestToParticipate(shouldRequestToParticipate);
		aStream.setStartDate(startDate);
		aStream.setEndDate(endDate);
		aStream.setLatitude(latitude);
		aStream.setLongitude(longitude);
		aStream.setAddress(address);
		aStream.setInvitedParticipants(invitedParticipants);

		// JSON
		Gson gson = new Gson();
		String gsonStream = gson.toJson(aStream);

		// Send it
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(conf.getBaseUrl() + "CreateStream");
			StringEntity entity = new StringEntity(gsonStream);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(entity);
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				gsonStream = EntityUtils.toString(response.getEntity());
				if (gsonStream != null) {
					result = gson.fromJson(gsonStream, StreamDTO.class);
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
