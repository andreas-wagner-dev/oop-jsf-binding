package org.task.app;

import org.task.Db;
import org.task.db.H2Db;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/**
 * Initialize and cleanup Application instances.
 */
@WebListener
public class WebApp implements ServletContextListener {

	private H2Db db;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// initialize
		db = new H2Db("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
		System.out.println("Storing the Db instance in the servlet context at key: " + Db.class.getSimpleName());
		sce.getServletContext().setAttribute(Db.class.getSimpleName(), db);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// cleanup
		db.close();
	}

}
