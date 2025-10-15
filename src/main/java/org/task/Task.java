package org.task;

import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;

import jakarta.faces.component.html.HtmlOutputText;

//--- 1. DOMAIN LAYER: Task Interface and Implementation (UI of Objects) ---
/**
 * The domain interface, which is responsible for business logic, persistence,
 * and display (UI of Objects).
 */
public interface Task extends Serializable {
    String id();

    String description();

    boolean ident(String id);

    boolean isCompleted();

    Task complete();

    void persistTo(Connection connection);

    void displayTo(HtmlOutputText output);

    void print(Writer writer);

}
