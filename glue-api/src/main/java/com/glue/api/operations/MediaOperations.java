package com.glue.api.operations;

import java.io.File;

import com.glue.exceptions.GlueException;
import com.glue.struct.IMedia;
import com.glue.struct.IStream;

public interface MediaOperations {

	IMedia createMedia(IStream stream, String caption, String extension, String mimeType, Double latitude,
			Double longitude, Long startDate, File file) throws GlueException;

	IMedia createMedia(IMedia media, File file) throws GlueException;

}
