package org.task.db;

import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.ShardingKeyBuilder;
import java.sql.Statement;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.task.Db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * Implementation of Db for H2 in-memory database using HikariCP connection pool.
 */
public class H2Db implements Db, Serializable {

	private static final long serialVersionUID = 1L;

	private static transient HikariDataSource db;

	private transient javax.sql.DataSource ds;

	/**
	 * Source URL of the DB. e.g. "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1" hold the DB
	 * open.
	 *
	 * @param url e.g. jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
	 */
	public H2Db(String url) {
		this(ds(url, "sa", "sa"));
	}

	public H2Db(String url, String username, String password) {
		this(ds(url, username, password));
	}

	public H2Db(javax.sql.DataSource ds) {
		this.ds = ds;
	}

	@Override
	public void close() {
		if (db != null) {
			db.close();
		}
	}

	public Connection open() {
		try {
			return db.getConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public <R> Supplier<R> open(Function<Connection, R> task) {

		return new Supplier<R>() {

			@Override
			public R get() {
				R result;
				try {
					Connection connection = db.getConnection();
					try {
						connection.setAutoCommit(false);
						result = task.apply(open());
						connection.commit();
					} catch (Exception e) {
						connection.rollback();
						throw e;
					} finally {
						connection.setAutoCommit(true);
						connection.close();
					}
				} catch (Exception e) {
					throw new IllegalStateException(e);
				}
				return result;
			}
		};
	}

	// Erzeugt die H2 DataSource.
	public static synchronized DataSource ds(String url, String username, String password) {
		// Muss org.h2.jdbcx.JdbcDataSource verwenden, da javax.sql.DataSource
		// ein Interface ist und keine statische Instanziierung erlaubt.
		if (db == null) {
			// h2 = new org.h2.jdbcx.JdbcDataSource();
			// h2.setURL(url);
			// h2.setUser(username);
			// h2.setPassword(password);
			HikariConfig config = new HikariConfig();
			config.setDriverClassName("org.h2.Driver");
			config.setJdbcUrl(url);
			config.setUsername(username);
			config.setPassword(password);
			config.setMaximumPoolSize(300);
			config.setMinimumIdle(5);
			config.setIdleTimeout(200000);
			config.setLeakDetectionThreshold(0);
			config.setConnectionTestQuery("SELECT 1");
			db = new HikariDataSource(config);
			initDb(db);
		}
		return db;
	}

	public static void initDb(DataSource dataSource) {
		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			// DDL Schemas of FOLDER and TASK tables
			conn.setAutoCommit(false);
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS folder (" +
							"id VARCHAR(255) PRIMARY KEY, " +
							"name VARCHAR(255) NOT NULL, " +
							"parentid VARCHAR(255), " +
							"CONSTRAINT fkparent FOREIGN KEY (parentid) REFERENCES folder (id) ON DELETE CASCADE" +
							");"
			);
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS task (" +
							"id VARCHAR(255) PRIMARY KEY, " +
							"description VARCHAR(500) NOT NULL, " +
							"completed BOOLEAN NOT NULL DEFAULT FALSE, " +
							"folderid VARCHAR(255) NOT NULL, " +
							"CONSTRAINT fktaskfolder FOREIGN KEY (folderid) REFERENCES folder (id) ON DELETE CASCADE" +
							");"
			);
			conn.commit();
			// Test data records for FOLDER and TASK tables

			// -- INSERT FOLDERS (3-level hierarchy)

			// -- 1. Root Folder (no parentid)
			stmt.execute("INSERT INTO folder (id, name, parentid) VALUES ('f100', 'Gesamtprojekt Organisation', NULL)");
			// -- 2. Subfolders of f100
			stmt.execute("INSERT INTO folder (id, name, parentid) VALUES ('f101', 'Frontend Development', 'f100');");
			stmt.execute("INSERT INTO folder (id, name, parentid) VALUES ('f102', 'Backend Tasks', 'f100');");
			// -- 3. Subfolder of f101 (Deeper nesting)
			stmt.execute("INSERT INTO folder (id, name, parentid) VALUES ('f103', 'Styling und UX', 'f101');");
			stmt.execute("INSERT INTO folder (id, name, parentid) VALUES ('f104', 'Personal und Verwaltung', 'f102');");

			// -- INSERT TASKS

			// -- Tasks in folder 'Frontend Development' (f101)
			stmt.execute(
					"INSERT INTO task (id, description, completed, folderid) VALUES ('t201', 'PrimeFaces Tree Komponente binden', TRUE, 'f101');"
			);
			stmt.execute(
					"INSERT INTO task (id, description, completed, folderid) VALUES ('t202', 'Tree Rendering mit f:event optimieren', FALSE, 'f101');"
			);
			// -- Tasks in folder 'Backend Tasks' (f102)
			stmt.execute(
					"INSERT INTO task (id, description, completed, folderid) VALUES ('t203', 'Datenbank DDL Migration erstellen', TRUE, 'f102');"
			);
			stmt.execute(
					"INSERT INTO task (id, description, completed, folderid) VALUES ('t204', 'Entity Persistence Logik implementieren', FALSE, 'f102');"
			);
			// -- Tasks in folder 'Styling und UX' (f103)
			stmt.execute(
					"INSERT INTO task (id, description, completed, folderid) VALUES ('t205', 'CSS-Anpassungen für Mobile-Ansicht', FALSE, 'f103');"
			);
			// -- Tasks in folder 'Personal und Verwaltung' (f104)
			stmt.execute(
					"INSERT INTO task (id, description, completed, folderid) VALUES ('t206', 'Reisekostenabrechnung einreichen', TRUE, 'f104');"
			);
			conn.commit();
			System.out.println("DB: Schema and initial created.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to initialize Schema for DB.");
		}
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return ds.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return ds.isWrapperFor(iface);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return ds.getConnection();
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		return ds.getConnection(username, password);
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return ds.getParentLogger();
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return ds.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		ds.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		ds.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return ds.getLoginTimeout();
	}

	@Override
	public ConnectionBuilder createConnectionBuilder() throws SQLException {
		return ds.createConnectionBuilder();
	}

	@Override
	public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
		return ds.createShardingKeyBuilder();
	}

}