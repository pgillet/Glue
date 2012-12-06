package com.glue.api.operations;

import java.io.InputStream;

import com.glue.exceptions.GlueException;
import com.glue.struct.IMedia;

public interface MediaOperations {

	IMedia createMedia(long streamId, String caption, String extension, String mimeType, double latitude,
			double longitude, long startDate, InputStream input) throws GlueException;

	IMedia createMedia(IMedia media, InputStream input) throws GlueException;

}
