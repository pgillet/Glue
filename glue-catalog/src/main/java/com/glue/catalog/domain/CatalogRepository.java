package com.glue.catalog.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

//@RepositoryRestResource(collectionResourceRel = "websites", path = "websites")
public interface CatalogRepository extends
	MongoRepository<EventWebsite, String> {

    Page<EventWebsite> findByUriLike(@Param("uri") String uri, Pageable pageable);

}
