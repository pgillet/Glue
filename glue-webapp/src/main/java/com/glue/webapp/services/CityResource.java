package com.glue.webapp.services;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.glue.domain.City;
import com.glue.persistence.CityDAO;

@Path("/cities")
public class CityResource {

    @Inject
    CityDAO dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    /**
     * 
     * @param query
     * @return list of cities
     */
    public List<City> getCities(@QueryParam("q") String query) {
	return dao.find(query, 10);
    }

}
