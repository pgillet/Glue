package com.glue.catalog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.glue.catalog.domain.EventWebsite;

@Component
@RepositoryEventHandler(EventWebsite.class)
public class SpringDataRestEventHandler {

    private final ManagerRepository managerRepository;

    @Autowired
    public SpringDataRestEventHandler(ManagerRepository managerRepository) {
	this.managerRepository = managerRepository;
    }

    @HandleBeforeCreate
    public void applyUserInformationUsingSecurityContext(EventWebsite website) {

	String name = SecurityContextHolder.getContext().getAuthentication()
		.getName();
	Manager manager = this.managerRepository.findByName(name);
	if (manager == null) {
	    Manager newManager = new Manager();
	    newManager.setName(name);
	    newManager.setRoles(new String[] { "ROLE_MANAGER" });
	    manager = this.managerRepository.save(newManager);
	}
	website.setManager(manager);
    }
}
