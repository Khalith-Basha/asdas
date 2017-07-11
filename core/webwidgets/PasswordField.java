package core.webwidgets;

import com.google.common.base.Strings;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * PasswordField WebWidget Class. This class contains all methods specific to
 * PasswordField objects. The PasswordField class is a wrapper class to allow
 * centralized control of common webwidget objects and methods.
 */
public class PasswordField extends TextField {
	/**
	 * Constructor for PasswordField object.
	 * <p>
	 *
	 * @param id
	 *            value of PasswordField object
	 */
	public PasswordField(String id) {
		super(id, "PasswordField");
	}

	/**
	 * Constructor to override object name with sLabel variable.
	 * <p>
	 *
	 * @param id
	 *            value of PasswordField object
	 * @param label
	 *            value to use in log output for PasswordField object
	 */
	public PasswordField(String id, String label) {
		super(id, "PasswordField", label);
	}

	/**
	 * Sets text into a Password field type widget.
	 * <p>
	 *
	 * @param text
	 *            value to type into the password field type widget
	 */
	@Override
	public void setText(String text) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(
					String.format("Enter Password \"%s\" into %s", Strings.repeat("*", text.length()), widgetInfo));

			SeleniumCore.getBrowser().type(sLocator, text);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}
}
