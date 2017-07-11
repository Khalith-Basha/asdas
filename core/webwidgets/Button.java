package core.webwidgets;

/**
 * The Button class is a wrapper class to allow centralized control of common
 * web widget objects and methods.
 */
public class Button extends WebWidget {
	/**
	 * Default constructor for Button object.
	 *
	 * @param id
	 *            the object identifier locator
	 */
	public Button(String id) {
		super(id, "Button");
	}

	/**
	 * Button constructor to override object name with sLabel variable.
	 *
	 * @param id
	 *            locator ID object identifier
	 * @param label
	 *            the label to use in replace of the default label
	 */
	public Button(String id, String label) {
		super(id, "Button", label);
	}
}
