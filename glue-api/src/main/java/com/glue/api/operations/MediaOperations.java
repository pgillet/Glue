package com.glue.api.operations;

import java.io.File;

import com.glue.domain.Link;
import com.glue.exceptions.GlueException;

public interface MediaOperations {

	Link createMedia(Link media, File file) throws GlueException;

}
