package core.webwidgets;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * The StaticText class is a wrapper class to allow centralized control of
 * common web widget objects and methods.
 */
public class StaticText extends WebWidget {
	/**
	 * StaticText object constructor.
	 *
	 * @param id
	 *            the locator or static text object identifier
	 */
	public StaticText(String id) {
		super(id, "TextField");
	}

	/**
	 * StaticText object constructor to override object name with sLabel
	 * variable.
	 *
	 * @param id
	 *            the locator or statictext object identifier
	 * @param label
	 *            the name to use for this object which will override the
	 *            calling objects name For example, if the calling object name
	 *            is Object1 and you want something more descriptive displayed
	 *            in the result log you can set the sLabel to "SearchAccount"
	 *            and the more descriptive object name "SearchAccount" will be
	 *            used in the log output instead of Object1. This is useful when
	 *            you are using a generic object type
	 */
	public StaticText(String id, String label) {
		super(id, "TextField", label);
	}

	/**
	 * Returns the text content from the calling static text object. Logs an
	 * error if the static text is not found.
	 * <p>
	 *
	 * @return text value of calling object
	 */
	@Override
	public String getText() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getText(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Silent click on text field. for text field selecting purposes.
	 */
	@Override
	public void click() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			SeleniumCore.getBrowser().click(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Determines if the specified Text is visible. This method will fail if the
	 * Text is not present.
	 *
	 * @return true if the specified Text is visible, false otherwise
	 */
	public boolean isTextPresent() {
		Boolean isTextPresent = null;
		if (SeleniumCore.getBrowser().isTextPresent(sLocator)) {
			isTextPresent = true;
		} else {
			isTextPresent = false;
			Log.errorHandler(getWidgetNotFoundMessage());
		}

		return isTextPresent;
	}

	/**
	 * Determines if the specified Text is visible. This method will log the
	 * details if the Text is not present.
	 *
	 * @return true if the specified Text is visible, false otherwise
	 */
	public boolean isTextPresentIgnoreError() {
		Boolean isTextPresent = null;
		if (SeleniumCore.getBrowser().isTextPresent(sLocator)) {
			isTextPresent = true;
		} else {
			isTextPresent = false;
			Log.logScriptInfo(getWidgetNotFoundMessage());
		}

		return isTextPresent;
	}
}
