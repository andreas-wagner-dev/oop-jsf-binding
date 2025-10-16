package org.todo;

import java.io.Closeable;

/**
 * Interface for database access.
 * Extends javax.sql.DataSource and Closeable to provide connection pooling and resource management.
 */
public interface Db extends javax.sql.DataSource, Closeable {

}
