package com.glue.webapp.repository;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.Part;

import com.glue.struct.IMedia;

public class RepositoryManager {

	private static final String GLUE_ROOT = "C:\\Glue_Stream\\";

	public static boolean createStream(long streamId) {
		File file = new File(GLUE_ROOT + streamId);
		return file.mkdirs();
	}

	public static void createMedia(IMedia media, Part mediaPart) throws IOException {
		System.out.println(GLUE_ROOT + media.getStreamId() + File.separator + media.getId() + "."
				+ media.getExtension());
		mediaPart.write(GLUE_ROOT + media.getStreamId() + File.separator + media.getId() + "." + media.getExtension());
	}
}
