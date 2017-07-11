
package core.webwidgets;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * The Slider class is a wrapper class to allow centralized control of common
 * web widget objects and methods.
 */
public class Slider extends WebWidget {
	/**
	 * Slider object constructor.
	 *
	 * @param id
	 *            the locator or slider object identifier
	 */
	public Slider(String id) {
		super(id, "Slider");
	}

	/**
	 * Slider object constructor to override object name with sLabel variable.
	 *
	 * @param id
	 *            the locator or slider object identifier
	 * @param label
	 *            the name to use for this object which will override the
	 *            calling objects name For example, if the calling object name
	 *            is Object1 and you want something more descriptive displayed
	 *            in the result log you can set the sLabel to "SearchAccount"
	 *            and the more descriptive object name "SearchAccount" will be
	 *            used in the log output instead of Object1. This is useful when
	 *            you are using a generic object type
	 */
	public Slider(String id, String label) {
		super(id, "Slider", label);
	}

	/**
	 * Clicks on calling object.
	 */
	@Override
	public void click() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click %s", widgetInfo));
			SeleniumCore.getBrowser().click(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}
}
