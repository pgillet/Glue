package com.glue.feed.io;

import java.io.File;
import java.io.FileFilter;

import org.apache.commons.lang.StringUtils;

public class FileExtensionFilter implements FileFilter {

	private String ext;

	public FileExtensionFilter(String ext) {
		this.ext = ext;
	}

	@Override
	public boolean accept(File pathname) {
		return StringUtils.endsWithIgnoreCase(pathname.getName(), ext);
	}

}
