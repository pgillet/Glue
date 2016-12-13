package com.glue.feed.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.glue.bot.Crawler;
import com.glue.bot.EventMapper;
import com.glue.bot.HtmlMapper;
import com.glue.catalog.domain.CatalogRepository;
import com.glue.catalog.domain.EventWebsite;
import com.glue.catalog.domain.Frequency;
import com.glue.catalog.security.Manager;
import com.glue.catalog.security.ManagerRepository;
import com.glue.domain.Event;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Component
public class ScheduledTasks {

	private static final Logger LOG = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	private CatalogRepository repository;

	@Autowired
	private ManagerRepository managers;

	/**
	 * For JSON pretty printing.
	 */
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private Manager manager;

	@Scheduled(fixedRate = 120000)
	public void scrapMonthly() throws Exception {
		// Service account
		final String username = "pasgille";

		manager = this.managers.findByName(username);

		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,
				"doesn't matter", AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

		EventWebsite ws = repository.findTopLeastRecentlyVisitedByCrawlFrequency(Frequency.MONTHLY);

		LOG.info("Crawling " + ws.getUri());
		LOG.debug(toJson(ws));

		HtmlMapper<Event> mappingStrategy = new EventMapper(ws.getEventSelectors(), ws.getEventTemplate());

		Crawler<Event> parser = new Crawler<>(ws.getSiteMap(), mappingStrategy);
		
		EventPersister extractor = new EventPersister(mappingStrategy);
		parser.setExtractor(extractor);

		parser.run();

		SecurityContextHolder.clearContext();
	}

	private String toJson(EventWebsite eventWebsite) {
		String jsonObj = gson.toJson(eventWebsite);
		return jsonObj;
	}

}
