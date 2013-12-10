package com.glue.feed.io;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class GlueIOUtils {

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

		return ze;
	}

}
