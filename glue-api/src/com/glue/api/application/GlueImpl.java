package com.glue.api.application;

import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import com.glue.api.conf.Configuration;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.struct.impl.Stream;
import com.glue.struct.impl.User;

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
		return HttpHelper.sendGlueObject(http, conf, stream, Stream.class, "CreateOrUpdateStream");
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
		return HttpHelper.sendGlueObject(http, conf, user, User.class, "CreateOrUpdateUser");
	}

	@Override
	public void joinStream(long streamID) {
		IStream stream = new Stream();
		stream.setId(streamID);
		joinStream(stream);
	}

	@Override
	public void joinStream(IStream stream) {
		HttpHelper.sendGlueObject(http, conf, stream, Stream.class, "JoinStream");
	}
}
