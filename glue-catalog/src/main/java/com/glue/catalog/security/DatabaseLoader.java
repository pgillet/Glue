package com.glue.catalog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;

import com.glue.catalog.domain.CatalogRepository;

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final ManagerRepository managers;

    @Autowired
    public DatabaseLoader(CatalogRepository employeeRepository,
	    ManagerRepository managerRepository) {
	this.managers = managerRepository;
    }

    public static void main(String[] args) {
	SpringApplication.run(DatabaseLoader.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {

	final String username = "pasgille";

	try {
	    Manager manager = this.managers.save(new Manager(username, "secr3t",
	    	"ROLE_MANAGER"));
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
