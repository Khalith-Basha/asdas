
package core.extjswidgets;

/**
 * The TextArea class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSTextArea extends ExtJSTextField {
	/**
	 * Default constructor for TextArea object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSTextArea(String locator) {
		super(locator);
	}

	/**
	 * TextArea constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSTextArea(String locator, String label) {
		super(locator, label);
	}
}
