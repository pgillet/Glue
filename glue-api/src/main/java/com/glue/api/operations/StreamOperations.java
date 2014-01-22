package com.glue.api.operations;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.glue.exceptions.GlueException;
import com.glue.domain.IStream;

public interface StreamOperations {

	IStream createStream(String title, String description, boolean publicc, boolean open, Set<String> tags,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException;

	IStream createStream(IStream stream) throws GlueException;

	IStream updateStream(IStream stream) throws GlueException;

	List<IStream> searchStreams(String query) throws GlueException;

	void joinStream(long streamID) throws GlueException;

	void joinStream(IStream stream) throws GlueException;

}
