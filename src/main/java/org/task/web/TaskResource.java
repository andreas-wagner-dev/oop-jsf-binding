package org.task.web;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.task.Db;
import org.task.Task;
import org.task.db.DbTask;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

/**
 * Defines the URL path that serve tasks as resource.
 */
@Path("/tasks")
public class TaskResource {

	@Inject
	private ServletContext server;

	// javax.sql.DataSource
	private Db dataSource;

	@PostConstruct
	public void init() {
		try {
			dataSource = (Db) server.getAttribute(Db.class.getSimpleName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GET
	@Path("/{id}")
	public void task(@PathParam("id") String id, @Context HttpServletResponse response) throws IOException {
		// object print/write to the response directly.
		response.setContentType(MediaType.APPLICATION_JSON);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		//
		Task taskOfId = DbTask.of(dataSource, id);
		// Null-Safe - ask returned object check given id
		if (taskOfId.ident(id)) {
			response.setStatus(200);
			// ask task print themselves as JSON
			taskOfId.print(response.getWriter());
			response.getWriter().close();
		} else {
			// not found
			response.setStatus(404);
		}
	}
}