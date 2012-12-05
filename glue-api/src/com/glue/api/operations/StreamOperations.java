package com.glue.api.operations;

import java.util.Map;

import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;
import com.glue.struct.impl.dto.StreamDTO;

public interface StreamOperations {

	IStream createStream(String title, String description, boolean publicc, boolean open,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException;

	IStream createStream(StreamDTO stream) throws GlueException;

	IStream updateStream(StreamDTO stream) throws GlueException;

}
