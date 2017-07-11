package core.webwidgets;

import core.utilities.Log;
import core.utilities.SeleniumCore;

/**
 * The Link class is a wrapper class to allow centralized control of common web
 * widget objects and methods.
 */
public class Link extends WebWidget {
	/**
	 * Link object constructor.
	 *
	 * @param id
	 *            the locator or link object identifier
	 */
	public Link(String id) {
		super(id, "Link");
	}

	/**
	 * Link object constructor to override object name with sLabel variable.
	 *
	 * @param id
	 *            the locator or link object identifier
	 * @param label
	 *            the name to use for this object which will override the
	 *            calling objects name For example, if the calling object name
	 *            is Object1 and you want something more descriptive displayed
	 *            in the result log you can set the sLabel to "SearchAccount"
	 *            and the more descriptive object name "SearchAccount" will be
	 *            used in the log output instead of Object1. This is useful when
	 *            you are using a generic object type
	 */
	public Link(String id, String label) {
		super(id, "Link", label);
	}

	/**
	 * Clicks on calling object.
	 */
	@Override
	public void click() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click %s", widgetInfo));

			try {
				SeleniumCore.driver.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator, bExactMatch))
						.click();
				return;
			} catch (Exception e) {
			}
		}

		Log.errorHandler(getWidgetNotFoundMessage());
	}

	/**
	 * Double clicks the right mouse button on an object.
	 */
	@Override
	public void doubleClick() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("DoubleClick %s", widgetInfo));

			try {
				SeleniumCore.getBrowser().doubleClick(sLocator);
				return;
			} catch (Exception e) {
			}
		}

		Log.errorHandler(getWidgetNotFoundMessage());
	}
}
