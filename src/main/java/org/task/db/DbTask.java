package org.task.db;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.primefaces.component.datatable.DataTable;
import org.task.Db;
import org.task.Task;
import org.task.ui.TaskTable;

import jakarta.faces.component.html.HtmlOutputText;

public class DbTask implements Task, Serializable {

	private static final long serialVersionUID = 1L;

	private final DataSource ds;

	private String id;
	private String description;
	private boolean completed;
	private String folderId;

	public DbTask(DataSource ds, ResultSet rs) throws SQLException {
		this(ds, rs.getString("folderid"), rs.getString("description"), rs.getBoolean("completed"),
				rs.getString("folderid"));
	}

	public DbTask(DataSource ds) {
		this(ds, UUID.randomUUID().toString());
	}

	public DbTask(DataSource ds, String id) {
		this.ds = ds;
		this.id = id;
	}

	public DbTask(DataSource ds, String id, String description, boolean completed, String folderId) {
		this.ds = ds;
		this.id = id;
		this.description = description;
		this.completed = completed;
		this.folderId = folderId;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public boolean ident(String id) {
		return this.id.equals(id);
	}

	@Override
	public String toString() {
		return id;
	}

	@Override
	public Task complete() {
		return new DbTask(ds, id, description, completed, folderId);
	}

	@Override
	public void persistTo(Connection connection) {
		String sql = String.format("UPDATE TASK SET DESCRIPTION = %s, COMPLETED = %s WHERE ID = %s", this.description,
				this.completed ? "TRUE" : "FALSE", this.id);
		System.out.println("LOG: Persisting Task: " + sql);
		try (Statement stmt = connection.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			throw new RuntimeException("Persistence error", e);
		}
	}

	@Override
	public String description() {
		return description;
	}

	@Override
	public boolean isCompleted() {
		return completed;
	}

	@Override
	public void displayTo(HtmlOutputText output) {
		if (output != null) {
			output.setValue(this.toString());
		}
	}

	@Override
	public int hashCode() {
		return id != null ? getClass().getSimpleName().hashCode() + id.hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other != null && id != null && other.getClass().isAssignableFrom(getClass())
				&& getClass().isAssignableFrom(other.getClass()) ? Task.class.cast(other).ident(id) : other == this;
	}

	//
	// Factory methods
	//

	public static List<Task> all(DataSource ds, String folderId) {
		List<Task> tasks = new ArrayList<>(0);
		try (PreparedStatement stmt = ds.getConnection()
				.prepareStatement("SELECT id, description, completed, folderid FROM task WHERE folderid = ?;")) {
			stmt.setString(1, folderId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					tasks.add(new DbTask(ds, rs));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return tasks;
	}

	public static void displayToTable(DataSource dataSource, DataTable table, String folderId) {
		table.setValue(new TaskTable(dataSource, folderId));
	}

	public static Task of(Db ds, String id) {
		List<Task> tasks = new ArrayList<>(0);
		try (PreparedStatement stmt = ds.getConnection()
				.prepareStatement("SELECT id, description, completed, folderid FROM task WHERE id = ?;")) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					tasks.add(new DbTask(ds, rs));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (tasks.isEmpty()) {
			// empty task
			return new DbTask(ds);
		}
		return tasks.get(0);
	}

	@Override
	public void print(Writer writer) {
		// convert to JSON string
		StringBuilder json = new StringBuilder();
		json.append("{").append("\"id\":\"").append(id).append("\",").append("\"description\":\"").append(description)
				.append("\",").append("\"completed\":").append(completed).append(",").append("\"folderid\":\"")
				.append(folderId).append("\"").append("}");
		System.out.println(json.toString());
		try {
			writer.write(json.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void prints(PrintWriter writer, String folderId) {
		List<Task> tasks = new ArrayList<>(0);
		try (PreparedStatement stmt = ds.getConnection()
				.prepareStatement("SELECT id, description, completed, folderid FROM task WHERE folderid = ?;")) {
			stmt.setString(1, folderId);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					tasks.add(new DbTask(ds, rs));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// print to JSON string
		StringBuilder json = new StringBuilder();
		json.append("[");
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			Writer sw = new StringWriter();
			task.print(sw);
			json.append(sw.toString());
			if (i < tasks.size() - 1) {
				json.append(",");
			}
		}
		json.append("]");
		System.out.println(json.toString());
		writer.write(json.toString());
	}
}