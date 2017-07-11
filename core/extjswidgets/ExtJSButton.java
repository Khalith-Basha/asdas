
package core.extjswidgets;

import core.utilities.exceptions.AutomationException;

/**
 * The Button class is a wrapper class to allow centralized control of common
 * Ext Js web widget objects and methods.
 */
public class ExtJSButton extends ExtJSWidget {
	/**
	 * Default constructor for Button object.
	 *
	 * @param locator
	 *            the object identifier locator
	 */
	public ExtJSButton(String locator) {
		super(locator);
	}

	/**
	 * Button constructor to override object name with label variable.
	 *
	 * @param locator
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public ExtJSButton(String locator, String label) {
		super(locator, label);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws AutomationException
	 *             if could not get text of the button
	 */
	@Override
	public String getText() {
		try {
			return (String) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').getText();", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not get text of %s: %s with locator \"%s\"", className, identifier, locator),
					e);
		}
	}
}
