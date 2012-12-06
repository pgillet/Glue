package com.glue.api.application;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;

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
import com.glue.struct.IUser;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.User;
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
	public IStream createStream(String title, String description, boolean publicc, boolean open, Set<String> tags,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException {

		// Create Stream DTO
		Stream aStream = new Stream();
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
		aStream.setTags(tags);
		return createOrUpdateStream(aStream);
	}

	@Override
	public IStream createStream(IStream stream) throws GlueException {
		return createOrUpdateStream(stream);
	}

	@Override
	public IStream updateStream(IStream stream) throws GlueException {
		return createOrUpdateStream(stream);
	}

	private IStream createOrUpdateStream(IStream stream) {

		IStream result = null;

		// JSON
		Gson gson = new Gson();
		String gsonStream = gson.toJson(stream);

		// Send it
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(conf.getBaseUrl() + "CreateOrUpdateStream");
			StringEntity entity = new StringEntity(gsonStream);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(entity);
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				gsonStream = EntityUtils.toString(response.getEntity());
				if (gsonStream != null) {
					result = gson.fromJson(gsonStream, Stream.class);
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

	@Override
	public IUser createUser(IUser user) throws GlueException {
		return createOrUpdateUser(user);
	}

	@Override
	public IUser createUser(String firstName, String lastName, String email, String password) throws GlueException {
		// Create User DTO
		IUser user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setMail(email);
		user.setPassword(password);
		return createOrUpdateUser(user);
	}

	@Override
	public IUser updateUser(IUser user) throws GlueException {
		return createOrUpdateUser(user);
	}

	private IUser createOrUpdateUser(IUser user) {
		IUser result = null;

		// JSON
		Gson gson = new Gson();
		String gsonStream = gson.toJson(user);

		// Send it
		HttpConnectionParams.setConnectionTimeout(http.getParams(), 10000); // Timeout
																			// limit
		HttpResponse response = null;
		try {
			HttpPost post = new HttpPost(conf.getBaseUrl() + "CreateOrUpdateUser");
			StringEntity entity = new StringEntity(gsonStream);
			entity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(entity);
			response = http.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				gsonStream = EntityUtils.toString(response.getEntity());
				if (gsonStream != null) {
					result = gson.fromJson(gsonStream, User.class);
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
