package com.glue.api.application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glue.api.operations.StreamOperations;
import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;
import com.glue.struct.impl.Stream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class StreamOperationsImpl implements StreamOperations {

	private static final String CREATE_OR_UPDATE_STREAM = "CreateOrUpdateStream";
	private static final String JOIN_STREAM = "JoinStream";
	private static final String SEARCH_STREAM = "SearchStream";
	private final Gson gson = new Gson();
	
	private GlueClientContext ctx;

	public StreamOperationsImpl(GlueClientContext ctx) {
		this.ctx = ctx;
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

	@Override
	public void joinStream(long streamID) {
		IStream stream = new Stream();
		stream.setId(streamID);
		joinStream(stream);
	}

	@Override
	public void joinStream(IStream stream) {
		HttpHelper.sendGlueObject(ctx.getHttpClient(), stream, Stream.class, JOIN_STREAM);
	}

	private IStream createOrUpdateStream(IStream stream) {
		return HttpHelper.sendGlueObject(ctx.getHttpClient(), stream, Stream.class, CREATE_OR_UPDATE_STREAM);
	}

	@Override
	public List<IStream> searchStreams(String query) throws GlueException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("query", "query");
		String response = HttpHelper.send(ctx.getHttpClient(), params, SEARCH_STREAM);
		return gson.fromJson(response, new TypeToken<List<Stream>>() {
		}.getType());
	}
}
