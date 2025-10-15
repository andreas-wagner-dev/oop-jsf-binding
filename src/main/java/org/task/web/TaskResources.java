package org.task.web;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS application configuration class.
 */
@ApplicationPath("/api")
public class TaskResources extends Application {
    // defines the components of a JAX-RS application.
}