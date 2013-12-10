package com.glue.feed.io;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.DeferredFileOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlueIOUtils {

	static final Logger LOG = LoggerFactory.getLogger(GlueIOUtils.class);

	/**
	 * Positions the given input stream in the ZIP file format to the first ZIP
	 * file entry that satisfies the filter.
	 * 
	 * @param in
	 * @param filter
	 * @return
	 * @throws IOException
	 */
	public static ZipEntry getEntry(ZipInputStream zin, FileFilter filter)
			throws IOException {
		ZipEntry ze;
		do {
			ze = zin.getNextEntry();
		} while (ze != null && !filter.accept(new File(ze.getName())));

		if (LOG.isDebugEnabled()) {
			if (ze != null) {
				LOG.debug("Selected ZIP file entry = " + ze.getName());
			} else {
				LOG.debug("No selected ZIP file entry");
			}
		}

		return ze;
	}

	/**
	 * Retains data from the given input stream in memory until a specified
	 * threshold is reached, and only then commit it to disk. Returns a new
	 * input stream on this deferred data source.
	 * 
	 * TODO: should use a pipe!
	 * 
	 * @param in
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public static InputStream getDeferredInputStream(final InputStream in,
			String name) throws IOException {
		int threshold = 20 * 1024; // 20 Kio
		String prefix = FilenameUtils.getBaseName(name);
		String suffix = FilenameUtils.getExtension(name);

		final DeferredFileOutputStream out = new DeferredFileOutputStream(
				threshold, prefix, suffix, null);

		try {

			long start = System.currentTimeMillis();

			IOUtils.copy(in, out);

			long end = System.currentTimeMillis();
			LOG.info(String.format("Copied input data from %s in %d ms", name,
					end - start));

			// The problem here is that we wait the input data to be fully
			// copied before returning an input stream on it

			InputStream din = null;
			byte[] data = out.getData();
			if (data != null) {
				din = new ByteArrayInputStream(data);
			} else {
				din = new FileInputStream(out.getFile());
			}

			return din;
		} finally {
			IOUtils.closeQuietly(in); // Is this method responsible for closing
										// the given input stream ?
			IOUtils.closeQuietly(out);
		}
	}
}
