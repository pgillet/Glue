package com.glue.webapp;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.glue.webapp.repository.RepositoryManager;

public class GlueContext implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();

		// Configure the repository manager
		String path = context.getRealPath("/");
		RepositoryManager.setRoot(path);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
