package org.task;

import java.io.Serializable;
import java.io.Writer;
import java.sql.Connection;

import jakarta.faces.component.html.HtmlOutputText;

//--- 1. DOMAIN LAYER: Task Interface und Implementierung (UI of Objects) ---
/**
 * Das Domain-Interface, welches Verantwortung für Business-Logik, Persistenz
 * und Anzeige (UI of Objects) trägt.
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
