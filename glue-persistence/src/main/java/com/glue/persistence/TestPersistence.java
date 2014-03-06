package com.glue.persistence;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.glue.domain.v2.Category;
import com.glue.domain.v2.Event;
import com.glue.domain.v2.Performer;
import com.glue.domain.v2.Venue;

public class TestPersistence {

    /**
     * @param args
     */
    public static void main(String[] args) {
	GluePersistenceService svc = GluePersistenceService.getService();
	EventDAO eventDAO = svc.getEventDAO();
	
	Event event = new Event();
	event.setTitle("Babyshambles @ Bikini");
	event.setDescription("bla bla...");
	
	List<Category> categories = new ArrayList<>();
	Category cat = new Category();
	cat.setName("Music");
	categories.add(cat);
	
	event.setCategories(categories);
	
	List<Performer> performers = new ArrayList<>();
	Performer performer = new Performer();
	performer.setName("The Babyshambles");
	performers.add(performer);
	
	event.setPerformers(performers);
	
	event.setPrice("20 euros");
	
	Calendar cal = Calendar.getInstance();
	Date startTime = cal.getTime();;
	
	event.setStartTime(startTime);
	
	Venue venue = new Venue();
	venue.setName("Le Bikini");
	venue.setAddress("Avenue Foch");

	event.setVenue(venue);

	svc.begin();

	Event persistentEvent = eventDAO.create(event);

	svc.commit();
	svc.close();

	System.out.println("Event ID = " + persistentEvent.getId());

    }

}
