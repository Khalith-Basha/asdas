
package core.extjswidgets;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.exceptions.AutomationException;

/**
 * The TextField class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSTextField extends ExtJSWidget {
	/**
	 * Default constructor for TextField object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSTextField(String locator) {
		super(locator);
	}

	/**
	 * TextField constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSTextField(String locator, String label) {
		super(locator, label);
	}

	/**
	 * Gets text from text field type widget.
	 *
	 * @return String returns text value of calling object
	 * @throws AutomationException
	 *             if could not get text of text field
	 */
	@Override
	public String getText() {
		try {
			return (String) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getValue();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not get text of %s: %s with locator \"%s\"", className, identifier, locator),
					e);
		}
	}

	/**
	 * Sets text into a text field type widget.
	 *
	 * @param text
	 *            value to type into the text field type widget
	 * @throws AutomationException
	 *             if could not set text into text field
	 */
	public void setText(String text) {
		Log.logScriptInfo(String.format("Enter Text \"%s\" into %s", text, widgetInfo));

		try {
			getJsExecutor()
					.executeScript(String.format("Ext.getCmp('%s').setValue('%s');", getId(), escapeValue(text)));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not set \"%s\" text into %s: %s with locator \"%s\"",
					text, className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Resets the current field value to the originally-loaded value.
	 *
	 * @throws AutomationException
	 *             if could not reset text field value
	 */
	public void reset() {
		Log.logScriptInfo(String.format("Reset text in %s", widgetInfo));

		try {
			getJsExecutor().executeScript(String.format("Ext.getCmp('%s').reset();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not reset %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Clears text field.
	 *
	 * @throws AutomationException
	 *             if could not clear text of text field
	 */
	public void clear() {
		Log.logScriptInfo(String.format("Clear text in %s", widgetInfo));

		try {
			setText("");
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not clear %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Checks whether textfield is readOnly in HTML.
	 *
	 * @return true if field is readOnly in HTML
	 * @throws AutomationException
	 *             if could not check whether text field is read-only
	 */
	public boolean isReadOnly() {
		try {
			return (Boolean) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').readOnly;", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether %s is read-only: %s with locator \"%s\"", className,
							identifier, locator),
					e);
		}
	}
}
