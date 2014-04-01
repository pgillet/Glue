package com.glue.content;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class GlueContext implements ServletContextListener {

    static final Logger LOG = LoggerFactory.getLogger(GlueContext.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
	ServletContext context = sce.getServletContext();

	// Create the repository root folder
	Path rootDir = Paths.get(System.getProperty("user.home"), ".glue",
		"repository");

	try {
	    Files.createDirectories(rootDir);
	} catch (IOException e) {
	    LOG.info(e.getMessage());
	}

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
