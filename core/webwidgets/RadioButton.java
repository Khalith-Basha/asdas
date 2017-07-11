package core.webwidgets;

import org.openqa.selenium.JavascriptExecutor;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * The RadioButton class is a wrapper class to allow centralized control of
 * common radio button web widget objects and methods.
 */
public class RadioButton extends WebWidget {
	/**
	 * RadioButton object constructor.
	 *
	 * @param id
	 *            the locator or RadioButton object identifier
	 */
	public RadioButton(final String id) {
		super(id, "RadioButton");
	}

	/**
	 * RadioButton object constructor to override object name with sLabel
	 * variable.
	 *
	 * @param id
	 *            the locator or Radiobutton object identifier
	 * @param label
	 *            the name to use for this object which will override the
	 *            calling objects name For example, if the calling object name
	 *            is Object1 and you want something more descriptive displayed
	 *            in the result log you can set the sLabel to "SearchAccount"
	 *            and the more descriptive object name "SearchAccount" will be
	 *            used in the log output instead of Object1. This is useful when
	 *            you are using a generic object type
	 */
	public RadioButton(final String id, final String label) {
		super(id, "RadioButton", label);
	}

	/**
	 * Clicks on radio button object.
	 */
	@Override
	public void click() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click %s", widgetInfo));
			// SeleniumCore.getBrowser().click(sLocator);

			((JavascriptExecutor) SeleniumCore.driver).executeScript("arguments[0].click()",
					SeleniumCore.driver.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator)));

		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Selects a specific radio button object.
	 */
	public void select() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Select %s", widgetInfo));
			if (!SeleniumCore.getBrowser().ischecked(sLocator)) {
				SeleniumCore.getBrowser().click(sLocator);
			}
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Gets the (whitespace-trimmed) value of an input field (or anything else
	 * with a value parameter). For check-box/radio elements, the value will be
	 * "on" or "off" depending on whether the element is checked or not.
	 * <p>
	 *
	 * @return value of input field For check-box/radio elements, the value will
	 *         be "on" or "off" depending on whether the element is checked or
	 *         not.
	 */
	public String getValue() {
		return SeleniumCore.getBrowser().getValue(sLocator);
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
	 * Gets whether a toggle-button (checkbox/radio) is selected.
	 *
	 * @return true if the checkbox or radio button is selected, false otherwise
	 */
	public boolean isSelected() {
		return SeleniumCore.getBrowser().ischecked(sLocator);
	}
}
