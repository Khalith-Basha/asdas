package core.webwidgets;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * TextField WebWidget Class. This class contains all methods specific to
 * TextField objects. The TextField class is a wrapper class to allow
 * centralized control of common webwidget objects and methods.
 */
public class TextField extends WebWidget {
	/**
	 * Constructor for text field object.
	 * <p>
	 *
	 * @param id
	 *            value of text field object
	 */
	public TextField(String id) {
		super(id, "TextField");
	}

	/**
	 * Constructor to override object name with sLabel variable.
	 * <p>
	 *
	 * @param id
	 *            value of text field object
	 * @param label
	 *            value to use in log output for text field object
	 */
	public TextField(String id, String label) {
		super(id, "TextField", label);
	}

	/**
	 * Constructor to override object name with sLabel variable.
	 * <p>
	 *
	 * @param id
	 *            value of text field object
	 * @param classType
	 *            object class
	 * @param label
	 *            value to use in log output for text field object
	 */
	public TextField(String id, String classType, String label) {
		super(id, "TextField", label);
	}

	/**
	 * Sets text into a text field type widget.
	 * <p>
	 *
	 * @param text
	 *            value to type into the text field type widget
	 */
	@Override
	public void setText(String text) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Enter Text \"%s\" into %s", text, widgetInfo));
			SeleniumCore.getBrowser().type(sLocator, text);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Clears text in text field type widget.
	 */
	public void clearText() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Cleared Text in %s", widgetInfo));
			SeleniumCore.getBrowser().type(sLocator, "");
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Checks if TextField isEditable.
	 * <p>
	 *
	 * @return true if TextField is editable, false if TextField is not editable
	 */
	public boolean isEditable() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			// Log.logScriptInfo("Enter Text " + "\"" + sText + "\"" + " into "
			// + sIdentifier + " " + sClassName);
			return SeleniumCore.getBrowser().isEditable(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return false;
	}

	/**
	 * Returns the text content from the calling textfield object. Logs an error
	 * if the textfield is not found.
	 * <p>
	 *
	 * @return text value of calling object
	 */
	@Override
	public String getText() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getValue(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Returns text from text field type widget and does not log error if
	 * textfield is not found.
	 * <p>
	 *
	 * @return text value of calling object
	 */
	@Override
	public String getTextIgnoreError() {
		try {
			return SeleniumCore.getBrowser().getText(sLocator);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Gets XpathCount from text field type widget.
	 * <p>
	 *
	 * @return XpathCount value of calling object
	 */
	public Number getXpathCount() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getXpathCount(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return null;
	}

	/**
	 * Gets Attribute from text field type widget attribute can be "href",
	 * "id","value", "style", etc.
	 * <p>
	 *
	 * @return Attribute value of calling object
	 */
	@Override
	public String getAttribute(String attribute) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getAttribute(sLocator + "~" + attribute);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Gets value from text field type widget.
	 *
	 * @return Attribute value of calling object
	 */
	public String getValue() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getValue(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Returns string array of window ids for each page on the system desktop.
	 *
	 * @return array of window ids for each page on the system desktop
	 */
	public String[] getAllWindowIds() {
		String[] windowIds = null;
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			windowIds = SeleniumCore.getBrowser().getAllWindowIds();
			return windowIds;
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return windowIds;
	}

	/**
	 * Returns string array of window names for each page on the system desktop.
	 *
	 * @return array of window names for each page on the system desktop
	 */
	public String[] getAllWindowNames() {
		String[] windowNames = null;
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			windowNames = SeleniumCore.getBrowser().getAllWindowNames();
			return windowNames;
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return windowNames;
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
	 * Simulates keystroke events on the specified element, as though you typed
	 * the value key-by-key.
	 * <p>
	 *
	 * @param text
	 *            the text to type
	 */
	@Override
	public void typeKeys(String text) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Type Text \"%s\" into %s", text, widgetInfo));
			SeleniumCore.getBrowser().typeKeys(sLocator, text);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Returns hidden field attribute value of element.
	 *
	 * @param attribute
	 *            attribute name
	 * @return attribute value
	 */
	public String getHiddenFieldAttribute(String attribute) {
		try {
			return SeleniumCore.getBrowser().getAttribute(sLocator + "~" + attribute);
		} catch (Exception e) {
			Log.errorHandler(String.format("Could not get \"@%s\" attribute for object %s", attribute, sIdentifier));
			return "";
		}
	}
}
