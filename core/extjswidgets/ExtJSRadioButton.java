
package core.extjswidgets;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The RadioButton class is a wrapper class to allow centralized control of
 * common Ext Js web widget objects and methods.
 */
public class ExtJSRadioButton extends ExtJSWidget {
	/**
	 * RadioButton constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSRadioButton(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Default constructor for RadioButton object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSRadioButton(String locator) {
		super(locator);
	}

	/**
	 * Selects radio button.
	 *
	 * @throws AutomationException
	 *             if could not select radio button
	 */
	public void select() {
		Log.logScriptInfo(String.format("Select %s", widgetInfo));

		try {
			getJsExecutor().executeScript(
					String.format("(function(){var rb=Ext.getCmp('%s');rb.setValue(true);})();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not select %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Deselects radio button.
	 *
	 * @throws AutomationException
	 *             if could not deselect radio button
	 */
	public void deselect() {
		Log.logScriptInfo(String.format("Deselect %s", widgetInfo));

		try {
			getJsExecutor().executeScript(
					String.format("(function(){var rb=Ext.getCmp('%s');rb.setValue(false);})();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not deselect %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets whether a toggle-button (checkbox/radio) is selected.
	 *
	 * @return true if the radio button is selected, false otherwise
	 * @throws AutomationException
	 *             if could not check whether radio button is selected
	 */
	public boolean isSelected() {
		try {
			return (Boolean) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').checked;", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether %s is selected: %s with locator \"%s\"", className,
							identifier, locator),
					e);
		}
	}

	/**
	 * Gets text from text field type widget.
	 *
	 * @return String returns text value of calling object
	 * @throws AutomationException
	 *             if could not get text of radio button
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
