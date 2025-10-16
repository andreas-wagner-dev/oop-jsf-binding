package org.todo;

import java.sql.Connection;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tree.Tree;

/**
 * Domain interface for a Folder object.
 * Responsible for business logic, persistence, and UI display.
 */
public interface Folder {

    /**
     * Returns the unique identifier of the folder.
     */
    String id();

    /**
     * Checks if the given id matches this folder's id.
     */
    boolean ident(String id);

    /**
     * Returns the child folders of this folder.
     */
    Iterable<Folder> children();

    /**
     * Returns the tasks contained in this folder.
     */
    Iterable<Task> tasks();

    /**
     * Persists the folder and its contents to the given database connection.
     */
    void persistTo(Connection connection);

    /**
     * Displays this folder in the given PrimeFaces Tree component.
     */
    void displayTo(Tree tree);

    /**
     * Displays the tasks of this folder in the given PrimeFaces DataTable component.
     */
    void displayTasksTo(DataTable taskTable);

}