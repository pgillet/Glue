package com.glue.catalog.domain;

public interface CatalogRepositoryCustom {

	/**
	 * Returns an EventWebsite with the given crawling frequency which has not
	 * been visited yet (see {@link #findTopNewByCrawlFrequency(Frequency)}, or
	 * the least recently visited EventWebsite otherwise, i.e. an EventWebsite
	 * which is activated and whose the lastVisited date is before the current
	 * date with 1 hour, day, week, month or year subtracted, according to the
	 * given crawling frequency.
	 * 
	 * @param crawlFrequency
	 * @return
	 */
	EventWebsite findTopLeastRecentlyVisitedByCrawlFrequency(Frequency crawlFrequency);

	/**
	 * Returns a newly created EventWebsite with the given crawling frequency
	 * and ready for scraping, i.e. an EventWebsite which is activated and whose
	 * the lastVisited date is null.
	 * 
	 * @param crawlFrequency
	 * @return
	 */
	EventWebsite findTopNewByCrawlFrequency(Frequency crawlFrequency);

}
