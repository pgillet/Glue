package com.glue.operations;

import java.util.Map;
import java.util.Set;

import com.glue.exceptions.GlueException;
import com.glue.struct.IStream;

public interface StreamOperations {

	IStream createStream(String title, String description, boolean publicc, boolean open, Set<String> guests,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException;

}
