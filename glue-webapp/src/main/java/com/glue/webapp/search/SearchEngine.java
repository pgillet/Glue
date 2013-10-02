package com.glue.webapp.search;

import java.util.List;

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;

public interface SearchEngine {
	
	List<IStream> search(String query) throws InternalServerException;

}
