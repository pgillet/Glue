package com.glue.catalog.security;

import org.springframework.data.repository.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface ManagerRepository extends Repository<Manager, Long> { // MongoRepository
								       // ??

    Manager save(Manager manager);

    Manager findByName(String name);

}
