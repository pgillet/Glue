package com.glue.catalog.domain;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface CatalogRepository extends
	MongoRepository<EventWebsite, String> {

}
