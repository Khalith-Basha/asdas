
package core.extjswidgets;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The CheckBox class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSCheckBox extends ExtJSWidget {
	/**
	 * CheckBox constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSCheckBox(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Default constructor for CheckBox object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSCheckBox(String locator) {
		super(locator);
	}

	/**
	 * Checks checkbox.
	 *
	 * @throws AutomationException
	 *             if could not check checkbox
	 */
	public void check() {
		Log.logScriptInfo(String.format("Check %s", widgetInfo));

		try {
			getJsExecutor().executeScript(
					String.format("(function(){var chk=Ext.getCmp('%s');chk.setValue(true);})();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Unchecks checkbox.
	 *
	 * @throws AutomationException
	 *             if could not uncheck checkbox
	 */
	public void uncheck() {
		Log.logScriptInfo(String.format("Uncheck %s", widgetInfo));

		try {
			getJsExecutor().executeScript(
					String.format("(function(){var chk=Ext.getCmp('%s');chk.setValue(false);})();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not uncheck %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets whether a toggle-button (checkbox/radio) is checked.
	 *
	 * @return true if the checkbox is checked, false otherwise
	 * @throws AutomationException
	 *             if could not check whether checkbox is checked
	 */
	public boolean isChecked() {
		try {
			return (Boolean) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').checked;", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not check whether %s is checked: %s with locator \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Gets text from text field type widget.
	 *
	 * @return String returns text value of calling object
	 * @throws AutomationException
	 *             if could not get text of checkbox
	 */
	@Override
	public String getText() {
		try {
			return (String) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').boxLabel;", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not get text of %s: %s with locator \"%s\"", className, identifier, locator),
					e);
		}
	}
}
