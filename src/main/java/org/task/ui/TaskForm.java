package org.task.ui;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tree.Tree;
import org.primefaces.event.NodeCollapseEvent;
import org.primefaces.event.NodeExpandEvent;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;
import org.task.Db;
import org.task.Folder;
import org.task.Task;
import org.task.db.DbFolder;

import jakarta.annotation.PostConstruct;
import jakarta.faces.component.html.HtmlOutputText;
import jakarta.faces.event.ActionEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;

/**
 * Managed bean (controller) for binding UI components to domain objects.
 */
@Named
@ViewScoped
public class TaskForm implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServletContext context;

	// DataSource
	private Db dataSource;

	// 1. Domain Object (State of View)
	private Folder rootFoler;
	private Task selectedTask;

	// 2. UI Component Binding on Target Setter
	private HtmlOutputText taskOutputComponent;
	private Tree folderTeeComponent;

	private TreeNode<?> selectedNode;

	private DataTable taskTableComponent;
	private Folder selectedFolder;

	@PostConstruct
	public void initForm() {
		try {
			dataSource = (Db) context.getAttribute(Db.class.getSimpleName());
			for (Folder folder : DbFolder.roots(dataSource)) {
				rootFoler = folder;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new RuntimeException(e);
		}
	}

	//
	// component binding to object.
	//
	public HtmlOutputText getTaskOutputText() {
		return this.taskOutputComponent;
	}

	public void setTaskOutputText(HtmlOutputText component) {
		this.taskOutputComponent = component;
		if (selectedTask != null) {
			selectedTask.displayTo(component);
		}
	}

	public Tree getFolderTree() {
		return folderTeeComponent;
	}

	public void setFolderTree(Tree component) {
		this.folderTeeComponent = component;
		if (rootFoler != null) {
			rootFoler.displayTo(folderTeeComponent);
		}
	}

	public DataTable getTaskTable() {
		return taskTableComponent;
	}

	public void setTaskTable(DataTable component) {
		this.taskTableComponent = component;
	}

	/**
	 * Complete the task.
	 */
	public void completeAction(ActionEvent event) {
		System.out.println("ACTION: completeAction called.");
		if (selectedTask != null) {
			// 1. Business Logic
			selectedTask = selectedTask.complete();
			// 2. Persistence Logic:
			try (Connection conn = dataSource.getConnection()) {
				selectedTask.persistTo(conn);
			} catch (SQLException e) {
				Logger.getLogger(TaskForm.class.getName()).log(Level.SEVERE, "Database connection error.", e);
			}
			// 3. UI Update Logic
			if (taskOutputComponent != null) {
				selectedTask.displayTo(taskOutputComponent);
			}
		}
	}

	//
	// tree selected node
	//

	public TreeNode<?> getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode<?> selectedNode) {
		this.selectedNode = selectedNode;
	}

	public Folder getSelectedFolder() {
		return selectedFolder;
	}

	public void setSelectedFolder(Folder selectedFolder) {
		this.selectedFolder = selectedFolder;
	}

	//
	// delegate tree events to objects
	//

	public void onNodeSelect(NodeSelectEvent event) {
		System.out.println("EVENT: NodeSelectEvent: " + event.getTreeNode());
		TreeNode<?> treeNode = event.getTreeNode();
		if (treeNode != null && event.getTreeNode().getData() instanceof Folder) {
			selectedFolder = Folder.class.cast(event.getTreeNode().getData());
			selectedFolder.displayTasksTo(taskTableComponent);
		}
	}

	public void onNodeUnselect(NodeUnselectEvent event) {
		System.out.println("EVENT: NodeUnselectEvent: " + event.getTreeNode().getData());
	}

	public void onNodeExpand(NodeExpandEvent event) {
		System.out.println("EVENT: NodeExpandEvent: " + event.getTreeNode().getData());
	}

	public void onNodeCollapse(NodeCollapseEvent event) {
		System.out.println("EVENT: NodeCollapseEvent: " + event.getTreeNode().getData());
	}

	//
	// table selected item
	//

	public Task getSelectedTask() {
		return selectedTask;
	}

	public void setSelectedTask(Task selectedTask) {
		this.selectedTask = selectedTask;
	}

	//
	// delegate table events to objects
	//
	public void onRowSelect(SelectEvent<Task> event) {
		System.out.println("EVENT: onRowUnselect called.");
		this.selectedTask = event.getObject();
	}

	public void onRowUnselect(UnselectEvent<Task> event) {
		System.out.println("EVENT: onRowUnselect called.");
		this.selectedTask = null;
	}

}