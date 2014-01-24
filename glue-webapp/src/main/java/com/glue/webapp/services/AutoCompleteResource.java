package com.glue.webapp.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.domain.IStream;
import com.glue.webapp.logic.InternalServerException;
import com.glue.webapp.search.SearchEngine;

// The Java class will be hosted at the URI path "/autocomplete"
@Path("/autocomplete")
public class AutoCompleteResource {

	static final Logger LOG = LoggerFactory.getLogger(AutoCompleteResource.class);

	@Context
	private SearchEngine<IStream> engine;

	// The Java method will process HTTP GET requests
	@GET
	// The Java method will produce content identified by the MIME Media
	// type "text/plain"
	@Produces(MediaType.APPLICATION_JSON)
	public List<String> run(@QueryParam("query") String query) {
		
		List<String> results = new ArrayList<>();
		
		if (query.trim().length()>1) {			
			
			// How many word in current query?
			List<String> queryWords = Arrays.asList(StringUtils.split(query));
	
			// Ask solr
			List<IStream> streams = new ArrayList<IStream>();
			try {
				streams = engine.searchForAutoComplete(query);
				query = query.trim();
				results.add(query);
				for (IStream stream : streams) {
					String response = extractResponse(queryWords, stream.getTitle().toLowerCase());
					LOG.debug(response);
					if (!"".equals(response) && response.contains(query)) {
						results.add(rtrim(response.substring(query.length())));
					}
				}
			} catch (InternalServerException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return results;
	}

	private String extractResponse(List<String> queryWords, String title) {		

		// Split response into words and retrieve first word of the query
		List<String> responseWords = Arrays.asList(StringUtils.split(title));
		responseWords.removeAll(Collections.singleton(""));
		int i = 0;
		boolean trouve = false;
		while (i < responseWords.size() && !trouve) {
			trouve = responseWords.get(i).startsWith(queryWords.get(0));
			if (!trouve) {
				i++;
			}
		}

		// Get only the next word
		responseWords = responseWords.subList(i, Math.min(responseWords.size(), i + queryWords.size() + 2));

		for (String string : responseWords) {
			LOG.debug(string);
		}
		
		return StringUtils.join(responseWords, " ").trim();

	}
	
	public static String rtrim(String s) {
        int i = s.length()-1;
        while (i >= 0 && Character.isWhitespace(s.charAt(i))) {
            i--;
        }
        return s.substring(0,i+1);
    }
}