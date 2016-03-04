package com.glue.catalog.domain;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(collectionResourceRel = "websites", path = "websites")
public interface CatalogRepository extends
	MongoRepository<EventWebsite, String> {

    List<EventWebsite> findByUri(@Param("uri") String uri);

}
