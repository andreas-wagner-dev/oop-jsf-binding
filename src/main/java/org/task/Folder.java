package org.task;

import java.sql.Connection;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tree.Tree;

public interface Folder {

	String id();

	boolean ident(String id);

	Iterable<Folder> children();

	Iterable<Task> tasks();

	void persistTo(Connection connection);

	void displayTo(Tree tree);

	void displayTasksTo(DataTable taskTable);

}