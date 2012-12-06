package com.glue.webapp.repository;

import java.io.File;
import java.io.IOException;

import com.glue.struct.IMedia;

public class RepositoryManager {

	private static final String GLUE_ROOT = "C:\\Glue_Stream\\";

	public static boolean createStream(long streamId) {
		File file = new File(GLUE_ROOT + streamId);
		return file.mkdirs();
	}

	public static boolean createMedia(IMedia media) throws IOException {
		File file = new File(GLUE_ROOT + media.getStreamId(), media.getId() + "." + media.getExtension());
		return file.createNewFile();
	}

}
