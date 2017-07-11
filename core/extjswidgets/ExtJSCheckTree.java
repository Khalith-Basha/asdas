
package core.extjswidgets;

import java.util.Arrays;
import java.util.List;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The CheckTree class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSCheckTree extends ExtJSTree {
	/**
	 * Default constructor for CheckTree object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSCheckTree(String locator) {
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
	public ExtJSCheckTree(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Checks tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be checked.
	 * @throws AutomationException
	 *             if could not check node of the tree
	 */
	public void check(String... nodes) {
		check(Arrays.asList(nodes), true);
	}

	/**
	 * Checks tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be checked.
	 * @throws AutomationException
	 *             if could not check node of the tree
	 */
	public void check(List<String> nodes) {
		check(nodes, true);
	}

	/**
	 * Unchecks tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be unchecked.
	 * @throws AutomationException
	 *             if could not uncheck node of the tree
	 */
	public void uncheck(String... nodes) {
		check(Arrays.asList(nodes), false);
	}

	/**
	 * Unchecks tree node by specified text.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be unchecked.
	 * @throws AutomationException
	 *             if could not uncheck node of the tree
	 */
	public void uncheck(List<String> nodes) {
		check(nodes, false);
	}

	/**
	 * Checks/unchecks tree node.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then the first match will be unchecked.
	 * @param check
	 *            set to true to check node, to false to uncheck node
	 * @throws AutomationException
	 *             if could not check/uncheck node of the tree
	 */
	private void check(List<String> nodes, boolean check) {
		String action = "Check";
		if (!check) {
			action = "Uncheck";
		}

		Log.logScriptInfo(String.format("%s \"%s\" node in %s", action, nodes, widgetInfo));

		try {
			int version = ExtJSHelper.getExtJsMajorVersion();
			if (version == 3) {
				getJsExecutor().executeScript(String.format("(function() {" + "%s" + "node.getUI().toggleCheck(%s);"
						+ "node.fireEvent('checkchange', node, %2$s);" + "})()", getNodeQuery(nodes), check));
			} else {
				getJsExecutor().executeScript(String.format(
						"(function() {" + "%s" + "var tree = Ext.getCmp('%s');" + "node.set('checked', %s);"
								+ "tree.fireEvent('checkchange', node, %3$s);" + "})()",
						getNodeQuery(nodes), getId(), check));
			}
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not %s \"%s\" node of %s: %s with locator \"%s\"",
					action.toLowerCase(), nodes, className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Checks whether node is checked.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is checked
	 * @throws AutomationException
	 *             if could not check whether node is checked
	 */
	public boolean isChecked(List<String> nodes) {
		try {
			int version = ExtJSHelper.getExtJsMajorVersion();
			if (version == 3) {
				return (Boolean) getJsExecutor().executeScript(String
						.format("return (function() {%s return node.attributes.checked; })()", getNodeQuery(nodes)));
			}

			return (boolean) getJsExecutor().executeScript(
					String.format("return (function() {%s return node.data.checked; })()", getNodeQuery(nodes)));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether \"%s\" node is checked in %s: %s with locator \"%s\"", nodes,
							className, identifier, locator),
					e);
		}
	}

	/**
	 * Checks whether node is checked.
	 *
	 * @param nodes
	 *            list of nodes texts from root to target node. If single node
	 *            name is set, then condition for the first match will be
	 *            returned.
	 * @return true if node is checked
	 * @throws AutomationException
	 *             if could not check whether node is checked
	 */
	public boolean isChecked(String... nodes) {
		return isChecked(Arrays.asList(nodes));
	}
}
