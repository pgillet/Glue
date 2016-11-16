package com.glue.catalog;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import com.glue.catalog.domain.CatalogRepository;
import com.glue.catalog.domain.EventWebsite;
import com.glue.catalog.domain.Frequency;
import com.glue.catalog.security.Manager;
import com.glue.catalog.security.ManagerRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

// @SpringBootApplication
public class SomeClientTest implements CommandLineRunner {

	@Autowired
	private CatalogRepository repository;

	@Autowired
	private ManagerRepository managers;

	/**
	 * For JSON pretty printing.
	 */
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	private Manager manager;

	public static void main(String[] args) {
		SpringApplication.run(SomeClientTest.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		final String username = "pasgille";
		// manager = this.managers.save(new Manager(username, "secr3t",
		// "ROLE_MANAGER"));

		manager = this.managers.findByName(username);

		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(username,
				"doesn't matter", AuthorityUtils.createAuthorityList("ROLE_MANAGER")));

		EventWebsite dummy = repository.save(getDummy());

		System.out.println(toJson(dummy));

		EventWebsite ws = repository.findTopLeastRecentlyVisitedByCrawlFrequency(Frequency.MONTHLY);

		System.out.println(toJson(ws));

		repository.delete(dummy);

		ws = repository.findTopLeastRecentlyVisitedByCrawlFrequency(Frequency.MONTHLY);

		System.out.println(toJson(ws));

		SecurityContextHolder.clearContext();
	}

	private String toJson(EventWebsite eventWebsite) {
		String jsonObj = gson.toJson(eventWebsite);
		return jsonObj;
	}

	private EventWebsite getDummy() {

		EventWebsite retval = new EventWebsite("dummy");

		retval.setManager(manager);

		retval.setActivated(true);
		// retval.setLastVisited(LocalDateTime.now().minusMonths(2));

		return retval;
	}

}
