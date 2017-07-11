
package core.extjswidgets;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The ExtJSTabPanel class is a wrapper class to allow centralized control of
 * common Ext Js web widget objects and methods.
 */
public class ExtJSTabPanel extends ExtJSWidget {
	/**
	 * Default constructor for ExtJSTabPanel object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSTabPanel(String locator) {
		super(locator);
	}

	/**
	 * ExtJSTabPanel constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSTabPanel(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Collapses the panel body so that it becomes hidden.
	 *
	 * @throws AutomationException
	 *             if could not collapse panel
	 */
	public void collapse() {
		Log.logScriptInfo(String.format("Collapse %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').collapse()", getId()));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during collapse: %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Expands the panel body so that it becomes visible.
	 *
	 * @throws AutomationException
	 *             if could not expand panel
	 */
	public void expand() {
		Log.logScriptInfo(String.format("Expand %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').expand()", getId()));
		} catch (Exception exception) {
			throw new AutomationException(String.format("Error occurred during expand: %s", exception.getMessage()),
					exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Sets the specified tab as the active tab.
	 *
	 * @param index
	 *            of tab panel to activate. Starting from 1
	 * @throws AutomationException
	 *             if could not set active tab
	 */
	public void setActiveTab(int index) {
		Log.logScriptInfo(String.format("Set active tab #%s %s", index, widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').setActiveTab(%d)", getId(), index - 1));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during setting active tab: %s", exception.getMessage()), exception);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets index of the currently active tab.
	 *
	 * @return index of the currently active tab
	 * @throws AutomationException
	 *             if could not get index of active tab
	 */
	public int getActiveTabIndex() {
		try {
			return ((Long) getJsExecutor().executeScript(String.format(
					"return (function(){var p=Ext.getCmp('%s'); return p.items.indexOf(p.getActiveTab())})()",
					getId()))).intValue() + 1;
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getting index of active tab: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Gets title of the currently active tab.
	 *
	 * @return title of the currently active tab
	 * @throws AutomationException
	 *             if could not get title active tab
	 */
	public String getActiveTabTitle() {
		try {
			return (String) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getActiveTab().title", getId()));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getting title of active tab: %s", exception.getMessage()),
					exception);
		}
	}

	/**
	 * Checks if table is disabled.
	 *
	 * @param index
	 *            of tab panel. Starting from 1
	 * @return true if table is disabled
	 */
	public Boolean isDisable(int index) {
		try {
			return (Boolean) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').items.get(%s).disabled", getId(), index - 1));
		} catch (Exception exception) {
			throw new AutomationException(
					String.format("Error occurred during getting title of active tab: %s", exception.getMessage()),
					exception);
		}
	}
}
