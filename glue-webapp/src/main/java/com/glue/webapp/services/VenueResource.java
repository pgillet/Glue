package com.glue.webapp.services;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.glue.domain.Venue;
import com.glue.persistence.VenueDAO;

@Path("/venues")
public class VenueResource {
	
    @Inject
    VenueDAO dao;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("autocomplete")
    /**
     * 
     * @param query
     * @return list of venue
     */
    public List<Venue> getVenues(@QueryParam("q") String query) {
    	List<Venue> result = new ArrayList<>();
    	//dao.
    	return result;
    }
	
}
