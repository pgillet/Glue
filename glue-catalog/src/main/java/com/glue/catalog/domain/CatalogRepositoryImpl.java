package com.glue.catalog.domain;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;

public class CatalogRepositoryImpl implements CatalogRepositoryCustom {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public EventWebsite findTopLeastRecentlyVisitedByCrawlFrequency(Frequency crawlFrequency) {

		EventWebsite retval = findTopNewByCrawlFrequency(crawlFrequency);
		
		if (retval == null) {

			LocalDateTime d = LocalDateTime.now();

			switch (crawlFrequency) {

			case HOURLY:
				d = d.minusHours(1);
				break;

			case DAILY:
				d = d.minusDays(1);
				break;

			case WEEKLY:
				d = d.minusWeeks(1);
				break;

			case MONTHLY:
				d = d.minusMonths(1);
				break;

			case YEARLY:
				d = d.minusYears(1);
				break;
			}

			Sort sort = new Sort("lastVisited");

			retval = mongoTemplate.findOne(query(where("activated").is(Boolean.TRUE).and("crawlFrequency")
					.is(crawlFrequency).and("lastVisited").lt(d)).with(sort), EventWebsite.class);
		}

		return retval;
	}

	@Override
	public EventWebsite findTopNewByCrawlFrequency(Frequency crawlFrequency) {
		EventWebsite retval = mongoTemplate.findOne(query(where("activated").is(Boolean.TRUE).and("crawlFrequency")
				.is(crawlFrequency).and("lastVisited").exists(false)), EventWebsite.class);

		return retval;
	}

}
