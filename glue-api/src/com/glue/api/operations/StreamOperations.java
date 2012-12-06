package com.glue.api.operations;

import java.util.Map;

import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;

public interface StreamOperations {

	IStream createStream(String title, String description, boolean publicc, boolean open,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException;

	IStream createStream(IStream stream) throws GlueException;

	IStream updateStream(IStream stream) throws GlueException;

}
