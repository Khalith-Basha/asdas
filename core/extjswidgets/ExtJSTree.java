
package core.extjswidgets;

import java.util.Arrays;
import java.util.List;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The Tree class is a wrapper class to allow centralized control of common Ext
 * Js web widget objects and methods.
 */
public class ExtJSTree extends ExtJSWidget {
	/**
	 * Default constructor for Tree object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSTree(String locator) {
		super(locator);
	}

	/**
	 * Tree constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSTree(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Collapses all nodes.
	 *
	 * @throws AutomationException
	 *             if could not collapse all nodes of the tree
	 */
	public void collapseAll() {
		Log.logScriptInfo(String.format("Collapse %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').collapseAll();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not collapse all nodes of %s: %s with locator \"%s\"",
					className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Expands all nodes.
	 *
	 * @throws AutomationException
	 *             if could not expand all nodes of the tree
	 */
	public void expandAll() {
		Log.logScriptInfo(String.format("Expand %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').expandAll();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not expand all nodes of %s: %s with locator \"%s\"",
					className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Expands node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be expanded.
	 * @throws AutomationException
	 *             if could not expand node of the tree
	 */
	public void expand(String... nodes) {
		expand(Arrays.asList(nodes));
	}

	/**
	 * Expands node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be expanded.
	 * @throws AutomationException
	 *             if could not expand node of the tree
	 */
	public void expand(List<String> nodes) {
		Log.logScriptInfo(String.format("Expand \"%s\" node of %s", nodes, widgetInfo));

		try {
			String collapseNodeQuery = String.format("(function() {%s node.expand(); })()", getNodeQuery(nodes),
					getId());
			getJsExecutor().executeScript(collapseNodeQuery);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not expand \"%s\" node of %s: %s with locator \"%s\"",
					nodes, className, identifier, locator), e);
		}
	}

	/**
	 * Collapses node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be collapsed.
	 * @throws AutomationException
	 *             if could not collapse node of the tree
	 */
	public void collapse(String... nodes) {
		collapse(Arrays.asList(nodes));
	}

	/**
	 * Collapses node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be collapsed.
	 * @throws AutomationException
	 *             if could not collapse node of the tree
	 */
	public void collapse(List<String> nodes) {
		Log.logScriptInfo(String.format("Collapse \"%s\" node of %s", nodes, widgetInfo));

		try {
			String collapseNodeQuery = String.format("(function() {%s node.collapse(); })()", getNodeQuery(nodes),
					getId());
			getJsExecutor().executeScript(collapseNodeQuery);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not collapse \"%s\" node of %s: %s with locator \"%s\"",
					nodes, className, identifier, locator), e);
		}
	}

	/**
	 * Selects tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be selected.
	 * @throws AutomationException
	 *             if could not select node of the tree
	 */
	public void select(String... nodes) {
		select(Arrays.asList(nodes));
	}

	/**
	 * Selects tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be selected.
	 * @throws AutomationException
	 *             if could not select node of the tree
	 */
	public void select(List<String> nodes) {
		Log.logScriptInfo(String.format("Select \"%s\" node in %s", nodes, widgetInfo));

		try {
			String selectNodeQuery = String.format("(function() {%s" + "var tree = Ext.getCmp('%s');"
					+ "tree.getSelectionModel().select(node);" + "})()", getNodeQuery(nodes), getId());

			getJsExecutor().executeScript(selectNodeQuery);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not select \"%s\" node of %s: %s with locator \"%s\"",
					nodes, className, identifier, locator), e);
		}
	}

	/**
	 * Deselects tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be deselected.
	 * @throws AutomationException
	 *             if could not deselect node of the tree
	 */
	public void deselect(String... nodes) {
		deselect(Arrays.asList(nodes));
	}

	/**
	 * Deselects tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be deselected.
	 * @throws AutomationException
	 *             if could not deselect node of the tree
	 */
	public void deselect(List<String> nodes) {
		Log.logScriptInfo(String.format("Deselect \"%s\" node in %s", nodes, widgetInfo));

		try {
			String actionQuery = "deselect";
			int version = ExtJSHelper.getExtJsMajorVersion();
			if (version == 3) {
				actionQuery = "unselect";
			}

			String deselectNodeQuery = String.format(
					"(function() {%s" + "var tree = Ext.getCmp('%s');" + "tree.getSelectionModel().%s(node);" + "})()",
					getNodeQuery(nodes), getId(), actionQuery);

			getJsExecutor().executeScript(deselectNodeQuery);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not deselect \"%s\" node of %s: %s with locator \"%s\"",
					nodes, className, identifier, locator), e);
		}
	}

	/**
	 * Moves node from current tree to another tree or position in current one.
	 *
	 * @param sourceNodes
	 *            list of nodes texts from root to target node in the current
	 *            tree. If single node name is set, then the first match will be
	 *            selected.
	 * @param destinationTree
	 *            the other or current tree
	 * @param destinationNodes
	 *            list of nodes texts from root to target node in the other or
	 *            current tree. If single node name is set, then the first match
	 *            will be selected.
	 * @throws AutomationException
	 *             if could not move node
	 */
	public void move(List<String> sourceNodes, ExtJSTree destinationTree, List<String> destinationNodes) {
		if (sourceNodes == null || destinationNodes == null || sourceNodes.isEmpty() || destinationNodes.isEmpty()) {
			throw new AutomationException("From-Path and To-Path cannot be null or empty");
		}

		Log.logScriptInfo(String.format("Move \"%s\" node from %s into \"%s\" %s", sourceNodes, widgetInfo,
				destinationNodes, destinationTree.widgetInfo));

		try {
			int version = ExtJSHelper.getExtJsMajorVersion();
			if (version == 3) {
				getJsExecutor().executeScript(String.format(
						"(function() {" + "var sTree = Ext.getCmp('%s');" + "var dTree = Ext.getCmp('%s');" + "%s"
								+ "var sNode = node;" + "%s" + "var dNode = node;" + "dNode.appendChild(sNode);"
								+ "})()",
						getId(), destinationTree.getId(), getNodeQuery(sourceNodes),
						destinationTree.getNodeQuery(destinationNodes)));
			} else {
				getJsExecutor().executeScript(String.format(
						"(function() {" + "%s" + "var sNode = node;" + "%s" + "var dNode = node;"
								+ "dNode.insertChild(0, sNode);" + "})()",
						getNodeQuery(sourceNodes), destinationTree.getNodeQuery(destinationNodes)));
			}
		} catch (Exception e) {
			throw new AutomationException(String.format(
					"Could not move \"%s\" node of %s: %s with locator \"%s\" into \"%s\" %s with locator \"%s\"",
					sourceNodes, className, identifier, locator, destinationNodes, destinationTree.className,
					destinationTree.locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets children of the specified node.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then children for the first match will be
	 *            returned.
	 * @return node children
	 * @throws AutomationException
	 *             if could not get children of the node
	 */
	public List<String> getNodes(String... nodes) {
		return getNodes(Arrays.asList(nodes));
	}

	/**
	 * Gets children of the specified node.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then children for the first match will be
	 *            returned.
	 * @return node children
	 * @throws AutomationException
	 *             if could not get children of the node
	 */
	public List<String> getNodes(List<String> nodes) {
		try {
			int version = ExtJSHelper.getExtJsMajorVersion();
			String textContainer = (version == 3) ? "" : ".data";

			String getNodesQuery = String.format("return (function() {%s" + "var cnildren = node.childNodes;"
					+ "var nodes = new Array();" + "for (var i = 0; i < cnildren.length; i++) {"
					+ "    nodes.push(cnildren[i]%s.text);" + "}" + "return nodes.join('\\n');" + "})()",
					getNodeQuery(nodes), textContainer);

			String children = (String) getJsExecutor().executeScript(getNodesQuery);
			return Arrays.asList(children.split("\\n"));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not get children of \"%s\" node of %s: %s with locator \"%s\"", nodes,
							className, identifier, locator),
					e);
		}
	}

	/**
	 * Returns depth of the node (the root node has a depth of 0).
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then depth for the first match will be returned.
	 * @return depth
	 * @throws AutomationException
	 *             if could not get depth of the node
	 */
	public int getDepth(String... nodes) {
		return getDepth(Arrays.asList(nodes));
	}

	/**
	 * Returns depth of the node (the root node has a depth of 0).
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then depth for the first match will be returned.
	 * @return depth
	 * @throws AutomationException
	 *             if could not get depth of the node
	 */
	public int getDepth(List<String> nodes) {
		try {
			return (int) (long) getJsExecutor().executeScript(
					String.format("return (function() {%s" + "if (node != null) {" + "    return node.getDepth();"
							+ "} else {" + "    return null;" + "}})()", getNodeQuery(nodes)));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not get depth of \"%s\" node in %s: %s with locator \"%s\"", nodes, className,
							identifier, locator),
					e);
		}
	}

	/**
	 * Checks whether node is present in the tree.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is present in the tree
	 */
	public boolean contains(String... nodes) {
		return contains(Arrays.asList(nodes));
	}

	/**
	 * Checks whether node is present in the tree.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is present in the tree
	 */
	public boolean contains(List<String> nodes) {
		try {
			return (Boolean) getJsExecutor().executeScript(
					String.format("return (function() {%s return node != null;})()", getNodeQuery(nodes)));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether \"%s\" node is present in %s: %s with locator \"%s\"", nodes,
							className, identifier, locator),
					e);
		}
	}

	/**
	 * Checks whether node is expanded.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is expanded
	 * @throws AutomationException
	 *             if could not check whether node is expanded
	 */
	public boolean isExpanded(String... nodes) {
		return isExpanded(Arrays.asList(nodes));
	}

	/**
	 * Checks whether node is expanded.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is expanded
	 * @throws AutomationException
	 *             if could not check whether node is expanded
	 */
	public boolean isExpanded(List<String> nodes) {
		try {
			return (Boolean) getJsExecutor().executeScript(
					String.format("return (function() {%s" + "if (node != null) {" + "    return node.isExpanded();"
							+ "} else {" + "    return null;" + "}})()", getNodeQuery(nodes)));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether \"%s\" node is expanded in %s: %s with locator \"%s\"",
							nodes, className, identifier, locator),
					e);
		}
	}

	/**
	 * Checks whether node is selected.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is selected
	 * @throws AutomationException
	 *             if could not check whether node is selected
	 */
	public boolean isSelected(String... nodes) {
		return isSelected(Arrays.asList(nodes));
	}

	/**
	 * Checks whether node is selected.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is selected
	 * @throws AutomationException
	 *             if could not check whether node is selected
	 */
	public boolean isSelected(List<String> nodes) {
		try {
			int version = ExtJSHelper.getExtJsMajorVersion();
			if (version == 3) {
				return (Boolean) getJsExecutor().executeScript(
						String.format("return (function() {%s return node.isSelected();})()", getNodeQuery(nodes)));
			}

			String getSelectedNodesQuery = String.format("return (function() {%s"
					+ "var selectedNodes = Ext.getCmp('%s').getSelectionModel().getSelection();"
					+ "for (var i = 0; i < selectedNodes.length; i++) {" + "    if (selectedNodes[i].id === node.id) {"
					+ "        return true;" + "    }" + "}" + "return false;" + "})()", getNodeQuery(nodes), getId());

			return (Boolean) getJsExecutor().executeScript(getSelectedNodesQuery);
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether \"%s\" node is selected in %s: %s with locator \"%s\"",
							nodes, className, identifier, locator),
					e);
		}
	}

	/**
	 * Gets node ID by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then id for the first match will be returned.
	 * @return node ID
	 * @throws AutomationException
	 *             if could not get node ID
	 */
	protected String getNodeQuery(List<String> nodes) {
		if (nodes == null || nodes.isEmpty()) {
			throw new AutomationException("Path to target node cannot be null or empty");
		}

		int version = ExtJSHelper.getExtJsMajorVersion();
		String textContainer = (version == 3) ? "" : ".data";

		try {
			String rootText = (String) getJsExecutor().executeScript(
					String.format("return Ext.getCmp('%s').getRootNode()%s.text;", getId(), textContainer));

			if (nodes.size() == 1) {
				// Root node can be the required node
				if (rootText != null && rootText.equals(nodes.get(0))) {
					return String.format("var node = Ext.getCmp('%s').getRootNode();\n", getId());
				}

				// First match with specified text can be the required node
				return String.format("var node = Ext.getCmp('%s').getRootNode().findChild('text','%s',true);\n",
						getId(), escapeValue(nodes.get(0)));
			}

			String index = "1";
			if (rootText == null || !nodes.get(0).equals(rootText)) {
				index = "0";
			}

			String nodesLabelsQuery = "";
			for (String node : nodes) {
				nodesLabelsQuery = String.format("%snodesLabels.push('%s');\n", nodesLabelsQuery, escapeValue(node));
			}

			return String.format(
					"var node = Ext.getCmp('%s').getRootNode();" + "var nodesLabels = new Array();" + nodesLabelsQuery +

							"for (var i = %s; i < nodesLabels.length; i++) {"
							+ "    node = node.findChild('text', nodesLabels[i], false);" + "    if (node == null) {"
							+ "        break;" + "    }" + "}\n",
					getId(), index);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not get \"%s\" node ID of %s: %s with locator \"%s\"",
					nodes, className, identifier, locator), e);
		}
	}
}
