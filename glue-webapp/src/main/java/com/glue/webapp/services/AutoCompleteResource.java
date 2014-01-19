package com.glue.webapp.services;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.glue.struct.IStream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.search.SearchEngine;

// The Java class will be hosted at the URI path "/autocomplete"
@Path("/autocomplete")
public class AutoCompleteResource {

	@Context
	private SearchEngine<IStream> engine;

	// The Java method will process HTTP GET requests
	@GET
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces(MediaType.APPLICATION_JSON)
	public List<IStream> run(@QueryParam("query") String query) {
		
		// Ask solr
		List<IStream> result = new ArrayList<IStream>();
		try {
			result = engine.searchForAutoComplete(query);
			for (IStream stream : result) {
				System.out.println(stream.getTitle());
				stream.setTitle(stream.getTitle().toLowerCase());
			}
		} catch (InternalServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (result==null)
				return new ArrayList<IStream>();
		}		
		
		return result;
	}
}
