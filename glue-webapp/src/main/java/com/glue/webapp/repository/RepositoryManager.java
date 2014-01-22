package com.glue.webapp.repository;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.Part;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.IMedia;

public class RepositoryManager {

	static final Logger LOG = LoggerFactory.getLogger(RepositoryManager.class);

	public static final String STREAMS = "/streams";

	private static String Root;

	/**
	 * @return the root
	 */
	public static String getRoot() {
		return Root;
	}

	/**
	 * @param root
	 *            the root to set
	 */
	public static void setRoot(String root) {
		File f = new File(root, STREAMS);
		Root = f.getPath();
	}

	public static boolean createStream(long streamId) {
		File file = new File(Root, Long.toString(streamId));
		return file.mkdirs();
	}

	public static void createMedia(IMedia media, Part mediaPart) throws IOException {

		File streamDirectory = new File(Root, Long.toString(media.getStream().getId()));
		File mediaFile = new File(streamDirectory, Long.toString(media.getId()) + "." + media.getExtension());

		LOG.info("Writing media file part to: " + mediaFile.getPath());
		mediaPart.write(mediaFile.getPath());
	}
}
