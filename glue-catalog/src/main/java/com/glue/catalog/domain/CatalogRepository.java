package com.glue.catalog.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;

//@RepositoryRestResource(collectionResourceRel = "websites", path = "websites")
@PreAuthorize("hasRole('ROLE_MANAGER')")
public interface CatalogRepository extends
	MongoRepository<EventWebsite, String>, CatalogRepositoryCustom {

    Page<EventWebsite> findByUriLike(@Param("uri") String uri, Pageable pageable);

}
