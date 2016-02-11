package com.glue.bot.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.glue.bot.SelectorKeys;
import com.glue.chain.Command;
import com.glue.chain.Context;
import com.glue.domain.Event;

public class InitEventCommand extends BaseCommand implements Command {

    public static final Logger LOG = LoggerFactory
	    .getLogger(InitEventCommand.class);

    private String eventTemplateKey = SelectorKeys.EVENT_TEMPLATE_KEY;

    public String getEventTemplateKey() {
	return eventTemplateKey;
    }

    public void setEventTemplateKey(String eventTemplateKey) {
	this.eventTemplateKey = eventTemplateKey;
    }

    @Override
    public boolean execute(Context context) throws Exception {

	LOG.trace("Entering " + this.getClass().getName() + " execute method");

	Event eventTemplate = (Event) context.get(getEventTemplateKey());

	Event event = new Event();
	context.put(getEventKey(), event);

	if (eventTemplate != null) {

	    event.setAllDay(eventTemplate.isAllDay());
	    event.setCategories(eventTemplate.getCategories());
	    event.setCategory(eventTemplate.getCategory());
	    event.setChildren(eventTemplate.getChildren());
	    event.setComments(eventTemplate.getComments());
	    event.setCreated(eventTemplate.getCreated());
	    event.setDescription(eventTemplate.getDescription());
	    event.setFree(eventTemplate.isFree());
	    event.setGoing(eventTemplate.getGoing());
	    // copy.setId(id);
	    // copy.setImages(eventTemplate.getImages());
	    event.setLinks(eventTemplate.getLinks());
	    event.setOccurrences(eventTemplate.getOccurrences());
	    event.setParent(eventTemplate.getParent());
	    event.setPerformers(eventTemplate.getPerformers());
	    event.setPrice(eventTemplate.getPrice());
	    event.setProperties(eventTemplate.getProperties());
	    event.setReference(eventTemplate.isReference());
	    event.setSource(eventTemplate.getSource());
	    event.setStartTime(eventTemplate.getStartTime());
	    event.setStopTime(eventTemplate.getStopTime());
	    event.setSummary(eventTemplate.getSummary());
	    event.setTags(eventTemplate.getTags());
	    event.setTimeZone(eventTemplate.getTimeZone());
	    event.setTitle(eventTemplate.getTitle());
	    event.setUrl(eventTemplate.getUrl());
	    event.setVenue(eventTemplate.getVenue());
	    event.setWithdrawn(eventTemplate.isWithdrawn());
	    event.setWithdrawnNote(eventTemplate.getWithdrawnNote());
	}

	LOG.trace("Exiting " + this.getClass().getName() + " execute method");

	return Command.CONTINUE_PROCESSING;
    }

}
