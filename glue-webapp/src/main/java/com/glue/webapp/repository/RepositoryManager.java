package com.glue.webapp.repository;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.Part;

import com.glue.struct.IMedia;

public class RepositoryManager {

	public static boolean createStream(long streamId, String path) {
		File file = new File(path + File.separator + streamId);
		return file.mkdirs();
	}

	public static void createMedia(IMedia media, Part mediaPart, String path) throws IOException {
		System.out.println(path + File.separator + media.getStreamId() + File.separator + media.getId() + "."
				+ media.getExtension());
		mediaPart.write(path + File.separator + media.getStreamId() + File.separator + media.getId() + "."
				+ media.getExtension());
	}
}
