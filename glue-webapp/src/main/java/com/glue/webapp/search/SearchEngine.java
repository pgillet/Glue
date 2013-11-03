package com.glue.webapp.search;

import java.util.Date;
import java.util.List;

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;

public interface SearchEngine {
	
	List<IStream> search(String query, Date start, Date end) throws InternalServerException;

}
