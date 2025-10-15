package org.task.ui;

import java.sql.Connection;
import java.util.Objects;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tree.Tree;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.DefaultTreeNodeChildren;
import org.primefaces.model.TreeNode;
import org.primefaces.model.TreeNodeChildren;
import org.task.Folder;
import org.task.Task;

/**
 * TreeNode implementation for representing Folder domain objects in the UI.
 */
public class FolderTree extends CheckboxTreeNode<Object> implements Folder {

	public static final String TEST42_EXPLORER = "Test-Catalog-Explorer";

	private static final long serialVersionUID = 1L;

	private boolean childrenFetched;
	private String originRowKey;
	private String originParentRowKey;

	private boolean selected;

	private Folder folder;

	public FolderTree(Folder folder) {
		super.setData(folder);
		super.setType("folder");
		this.folder = folder;
		setChildren(new DefaultTreeNodeChildren<>(this));
	}

	//
	//
	//

	@Override
	public String id() {
		return folder.id();
	}

	@Override
	public boolean ident(String id) {
		return folder.ident(id);
	}

	@Override
	public Iterable<Folder> children() {
		return folder.children();
	}

	@Override
	public Iterable<Task> tasks() {
		return folder.tasks();
	}

	@Override
	public void persistTo(Connection connection) {
		folder.persistTo(connection);
	}

	@Override
	public void displayTo(Tree tree) {
		folder.displayTo(tree);
	}

	@Override
	public void displayTasksTo(DataTable taskTable) {
		folder.displayTasksTo(taskTable);
	}

	//
	//
	//

	// The Folder Domain Object create the View Model (TreeNode) for JSF.
	private void ensureChildrenFetched() {
		if (childrenFetched || !isExpanded()) { return; }
		childrenFetched = true;
		try {
			if (Objects.equals(getType(), "root")) {
				for (Folder child : children()) {
					super.getChildren().add(new FolderTree(child));
				}
			} else if (Objects.equals(getType(), "folder")) {
				for (Folder child : children()) {
					super.getChildren().add(new FolderTree(child));
				}
			}
		} catch (Exception e) {
			DefaultTreeNode<Object> errorNode = new DefaultTreeNode<>(
					"default", "Failed to load " + getData() + " data. " + e.getMessage(), null
			);
			errorNode.setSelectable(false);
			// super.getChildren().add(errorNode);
		}
	}

	public boolean isSelectableElement() {
		return true;
	}

	@Override
	public void setSelected(boolean value) {
		// super.setSelected(value);
		this.selected = value;
	}

	@Override
	public void setSelected(boolean value, boolean propagate) {
		// super.setSelected(value, propagate);
		this.selected = value;
	}

	@Override
	public void setSelected(boolean value, boolean propagateDown, boolean propagateUp) {
		// super.setSelected(value, propagateDown, propagateUp);
		this.selected = value;
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public boolean isSelectable() {
		return isSelectableElement();
	}

	@Override
	public void setRowKey(String rowKey) {
		super.setRowKey(rowKey);
		if (originRowKey == null) {
			originRowKey = rowKey;
		}
	}

	@Override
	public void setParent(@SuppressWarnings("rawtypes")
	TreeNode parent) {
		super.setParent(parent);
		if (originParentRowKey != null && parent != null) {
			originParentRowKey = parent.getRowKey();
		}
	}

	@Override
	public TreeNodeChildren<Object> getChildren() {
		ensureChildrenFetched();
		return super.getChildren();
	}

	@Override
	public int getChildCount() {
		ensureChildrenFetched();
		return super.getChildCount();
	}

	@Override
	public boolean isLeaf() {
		boolean isLeaf = false;
		if (!isLeaf) {
			ensureChildrenFetched();
		}
		return isLeaf;
	}

	@Override
	public void setExpanded(boolean expanded) {
		ensureChildrenFetched();
		super.setExpanded(expanded);
	}

	public void reloadChildren() {
		super.getChildren().clear();
		childrenFetched = false;
	}

	public void refreshChildren() {
		boolean expanded = isExpanded();
		setExpanded(true);
		setChildren(new DefaultTreeNodeChildren<>(this));
		childrenFetched = false;
		ensureChildrenFetched();
		setExpanded(expanded);
	}

	public String getOriginRowKey() {
		return originRowKey;
	}

	public void setOriginRowKey(String originRowKey) {
		this.originRowKey = originRowKey;
	}

	public String getOriginParentRowKey() {
		return originParentRowKey;
	}

	public void setOriginParentRowKey(String originParentRowKey) {
		this.originParentRowKey = originParentRowKey;
	}

}