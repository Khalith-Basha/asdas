package core.webwidgets;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * The CheckBox class is a wrapper class to allow centralized control of common
 * web widget objects and methods.
 */
public class CheckBox extends WebWidget {
	/**
	 * Default constructor for CheckBox object.
	 *
	 * @param id
	 *            the object identifier locator
	 */
	public CheckBox(String id) {
		super(id, "CheckBox");
	}

	/**
	 * CheckBox constructor to override object name with sLabel variable.
	 *
	 * @param id
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public CheckBox(String id, String label) {
		super(id, "CheckBox", label);
	}

	/**
	 * Checks a checkbox.
	 */
	public void check() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Check %s", widgetInfo));
			SeleniumCore.getBrowser().check(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Unchecks a checkbox.
	 */
	public void uncheck() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Uncheck %s", widgetInfo));
			SeleniumCore.getBrowser().uncheck(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Gets whether a toggle-button (checkbox/radio) is checked.
	 *
	 * @return true if the checkbox is checked, false otherwise
	 */
	public boolean ischecked() {
		return SeleniumCore.getBrowser().ischecked(sLocator);
	}

	/**
	 * Gets the (whitespace-trimmed) value of an input field (or anything else
	 * with a value parameter). For check-box/radio elements, the value will be
	 * "on" or "off" depending on whether the element is checked or not.
	 *
	 * @return checkbox object value
	 */
	public String getValue() {
		return SeleniumCore.getBrowser().getValue(sLocator);
	}
}
