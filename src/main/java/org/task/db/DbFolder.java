package org.task.db;

import java.io.Serializable;
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
import org.primefaces.component.tree.Tree;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import org.task.Folder;
import org.task.Task;
import org.task.ui.FolderTree;
import org.task.ui.TaskTable;

/**
 * Implementation of the Folder domain object with database persistence.
 */
public class DbFolder implements Folder, Serializable {

	private static final long serialVersionUID = 1L;

	private final DataSource ds;

	private String id;
	private String name;
	private String parentId;

	// UI part
	private TreeNode<Object> treeNode;

	private TaskTable taskTable;

	public DbFolder(DataSource ds, ResultSet rs) throws SQLException {
		this(ds, rs.getString("id"), rs.getString("name"), rs.getString("parentid"));
	}

	public DbFolder(DataSource ds, String name) {
		this(ds, UUID.randomUUID().toString(), name, null);
	}

	public DbFolder(DataSource ds, String name, String parentId) {
		this(ds, UUID.randomUUID().toString(), name, parentId);
	}

	public DbFolder(DataSource ds, String id, String name, String parentId) {
		this.ds = ds;
		this.id = id;
		this.name = name;
		this.parentId = parentId;
	}

	@Override
	public String id() {
		return id;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean ident(String id) {
		return this.id.equals(id);
	}

	@Override
	public Iterable<Folder> children() {
		List<Folder> children = new ArrayList<>();
		try (PreparedStatement stmt = ds.getConnection()
				.prepareStatement("SELECT id, name, parentid FROM folder WHERE parentid = ?;")) {
			stmt.setString(1, id);
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					children.add(new DbFolder(ds, rs));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Laden der  Kinder: " + e.getMessage(), e);
		}
		return children;
	}

	@Override
	public Iterable<Task> tasks() {
		try {
			return DbTask.all(ds, id);
		} catch (Exception e) {
			throw new RuntimeException("Fehler beim Laden der Tasks: " + e.getMessage(), e);
		}
	}

	@Override
	public void persistTo(Connection connection) {
		String sql = String
				.format("UPDATE task SET name = %s, parentId = %s WHERE id = %s", this.name, this.parentId, this.id);
		System.out.println("LOG: Persisting Task: " + sql);
		try (Statement stmt = connection.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			throw new RuntimeException("Persistence error", e);
		}
	}

	@Override
	public void displayTo(Tree tree) {
		// PrimeFaces TreeNode Logic
		if (treeNode == null) {
			treeNode = new DefaultTreeNode<>("root", "root", null);
			treeNode.setExpanded(true);
			treeNode.getChildren().add(new FolderTree(this));
		}
		tree.setValue(treeNode);
	}

	public void displayTo(Connection connection, Tree tree) {
		if (treeNode == null) {
			treeNode = new DefaultTreeNode<>("root", "root", null);
			treeNode.setExpanded(true);
			treeNode.getChildren().add(new FolderTree(this));
		}
		tree.setValue(treeNode);
	}

	@Override
	public void displayTasksTo(DataTable table) {
		if (taskTable == null) {
			taskTable = new TaskTable(ds, id);
		}
		if (table != null) {
			table.setValue(taskTable);
		}
	}

	@Override
	public int hashCode() {
		return id != null ? getClass().getSimpleName().hashCode() + id.hashCode() : super.hashCode();
	}

	@Override
	public boolean equals(Object other) {
		return other != null && id != null && other.getClass().isAssignableFrom(getClass())
				&& getClass().isAssignableFrom(other.getClass()) ? Folder.class.cast(other).ident(id) : other == this;
	}

	//
	// factory methods
	//
	public static List<Folder> roots(DataSource ds) {
		List<Folder> roots = new ArrayList<>(0);
		try (PreparedStatement stmt = ds.getConnection()
				.prepareStatement("SELECT id, name, parentid FROM folder WHERE parentid IS NULL")) {
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					roots.add(new DbFolder(ds, rs));
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return roots;
	}
}
