
package core.extjswidgets;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import core.extjswidgets.utils.ExtJSHelper;
import core.utilities.Log;
import core.utilities.SeleniumCore;
import core.utilities.Strings;
import core.utilities.Timers;
import core.utilities.Timers.Condition;
import core.utilities.exceptions.AutomationException;

/**
 * The ExtjsWidget class is a wrapper class to allow centralized control of
 * common Ext Js web UI objects.
 */
public abstract class ExtJSWidget {
	protected String locator = "";
	protected String identifier = "";
	protected String className = "";
	protected String widgetInfo = "";

	/**
	 * Initializes widget.
	 *
	 * @param locator
	 *            the locator ID or identifier for the object
	 * @param label
	 *            short description of widget like a name, shouldn't contain
	 *            white spaces or white spaces will be removed
	 */
	public ExtJSWidget(String locator, String label) {
		this.locator = locator;

		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		for (int i = 1; i < stackTrace.length; i++) {
			if (!stackTrace[i].getMethodName().equals("<init>")) {
				String className = stackTrace[i].getClassName();
				if (label == null || label.isEmpty()) {
					label = stackTrace[i].getMethodName();
				}

				identifier = String.format("%s.%s", className.substring(className.lastIndexOf(".") + 1),
						Strings.removeWhiteSpace(label));
				break;
			}
		}

		className = getClass().getSimpleName();

		widgetInfo = Log.AUTOMATION_LOCATOR_PRINT
				? String.format("%s %s with locator \"%s\"", identifier, className, locator)
				: String.format("%s %s", identifier, className);
	}

	/**
	 * Initializes widget.
	 *
	 * @param locator
	 *            the locator ID or identifier for the object
	 */
	public ExtJSWidget(String locator) {
		this(locator, "");
	}

	/**
	 * Clicks on calling object.
	 *
	 * @throws AutomationException
	 *             if could not click on the element
	 */
	public void click() {
		Log.logScriptInfo(String.format("Click %s", widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().click(locator);
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not click on %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Clicks on calling object.
	 *
	 * @throws AutomationException
	 *             if could not click at the element
	 */
	public void clickAt() {
		Log.logScriptInfo(String.format("Click at %s", widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().clickAt(locator, "");
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not click at %s: %s with locator \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Clicks a widget found using exact match for locator string passed in.
	 *
	 * @throws AutomationException
	 *             if could not click exact match element
	 */
	public void clickExactMatch() {
		Log.logScriptInfo(String.format("Click exact match %s", widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().clickExactMatch(locator);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not click exact match for %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Clicks right mouse button on calling object.
	 *
	 * @throws AutomationException
	 *             if could not click right mouse button on the element
	 */
	public void clickRightMouse() {
		Log.logScriptInfo(String.format("Click right mouse button on %s", widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().clickMouseButton(locator);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not click exact match for %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Double clicks the right mouse button on an object.
	 *
	 * @throws AutomationException
	 *             if could not double click on the element
	 */
	public void doubleClick() {
		Log.logScriptInfo(String.format("DoubleClick %s", widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().doubleClick(locator);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not double click %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Drag and Drop specified elements.
	 *
	 * @param toLocator
	 *            - locator to drop object into
	 * @throws AutomationException
	 *             if could not drag from the element to target
	 */
	public void dragAndDrop(String toLocator) {
		Log.logScriptInfo(String.format("Drag from %s to %s", widgetInfo, toLocator));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().dragAndDrop(locator, toLocator);
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not drag from %s: %s with locator of \"%s\" to \"%s\" locator", className,
							identifier, locator, toLocator),
					e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Drag and Drop specified elements.
	 *
	 * @param toObject
	 *            - object to drop object into
	 * @throws AutomationException
	 *             if could not drag from the element to object
	 */
	public void dragAndDrop(ExtJSWidget toObject) {
		Log.logScriptInfo(String.format("Drag from %s to %s", widgetInfo, toObject.widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().dragAndDrop(locator, toObject.locator);
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not drag from %s: %s with locator of \"%s\" to object with locator of \"%s\"",
							className, identifier, locator, toObject.locator),
					e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Sets focus on an object if the element is an input field, move the cursor
	 * to that field.
	 *
	 * @throws AutomationException
	 *             if could not set focus on the element
	 */
	public void focus() {
		ensureWaitedForExistence();

		try {
			Actions action = new Actions(SeleniumCore.driver);
			action.moveToElement(SeleniumCore.driver.findElement(SeleniumCore.getBrowser().convertLocatorToBy(locator)))
					.perform();
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not focus %s: %s with locator of \"%s\"", className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Gets text of widget.
	 *
	 * @return String returns text value of calling object
	 * @throws AutomationException
	 *             if could not get text of the element
	 */
	public String getText() {
		ensureWaitedForExistence();

		try {
			return SeleniumCore.getBrowser().getText(locator);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not get text of %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Returns x coordinate as integer.
	 *
	 * @return x coordinate of calling object
	 */
	public int getX() {
		try {
			return (int) (long) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getEl().getX();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not get X coordinate of %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Returns y coordinate as integer.
	 *
	 * @return y coordinate of calling object
	 */
	public int getY() {
		try {
			return (int) (long) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getEl().getY();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not get Y coordinate of %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Returns width.
	 *
	 * @return width of calling object
	 */
	public int getWidth() {
		try {
			return (int) (long) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getEl().getWidth();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not get width of %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Returns height.
	 *
	 * @return height of calling object
	 */
	public int getHeight() {
		try {
			return (int) (long) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').getEl().getHeight();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not get height of %s: %s with locator of \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Simulates keystroke events on the specified element, as though you typed
	 * the value key-by-key.
	 *
	 * @param keySequence
	 *            keys to type
	 * @throws AutomationException
	 *             if could not type text into the element
	 */
	public void keyPress(String keySequence) {
		Log.logScriptInfo(String.format("Type Text: \"%s\" into %s", keySequence, widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().keyPress(locator, keySequence);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not type text \"%s\" into %s: %s with locator of \"%s\"",
					keySequence, className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Simulates a user pressing and releasing a key in the active window.
	 *
	 * @param arg0
	 *            - key to press
	 * @throws AutomationException
	 *             if could not press key
	 */
	public void keyPressNative(String arg0) {
		try {
			SeleniumCore.getBrowser().keyPressNative(arg0);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not press key \"%s\" natively", arg0), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Mimics typing a key into calling object.
	 *
	 * @param key
	 *            key to type into calling object widget i.e.
	 *            GoogleMainPage.tfSearchText().sendKeys(Keys.BACK_SPACE);
	 * @throws AutomationException
	 *             if could not send keys into the element
	 */
	public void sendKeys(Keys key) {
		Log.logScriptInfo(String.format("Press key: %s into %s", key.name(), widgetInfo));
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().sendKeys(locator, key);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not send keys \"%s\" into %s: %s with locator of \"%s\"",
					key, className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Simulates keystroke events on the specified element, as though you typed
	 * the value key-by-key.
	 *
	 * @param text
	 *            the text to type
	 * @throws AutomationException
	 *             if could not type keys into the element
	 */
	public void typeKeys(String text) {
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().typeKeys(locator, text);
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not type keys \"%s\" into %s: %s with locator of \"%s\"",
					text, className, identifier, locator), e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Simulates a user hovering a mouse over the specified element.
	 *
	 * @throws AutomationException
	 *             if could not process mouse over under the element
	 */
	public void mouseOver() {
		ensureWaitedForExistence();

		try {
			SeleniumCore.getBrowser().mouseOver(locator);
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not process mouse over under the %s: %s with locator of \"%s\"", className,
							identifier, locator),
					e);
		}

		ExtJSHelper.ensureWaitedForAjax();
	}

	/**
	 * Returns true if an object is found, false if object is not found.
	 *
	 * @return true if an object is found, false if object is not found
	 */
	public boolean isExists() {
		return SeleniumCore.getBrowser().isElementPresent(locator);
	}

	/**
	 * Waits for the calling element to appear on the page.
	 *
	 * @param iWait
	 *            amount of time in seconds to wait - use global time out
	 *            variables in Log.giAutomation
	 * @return true if the element is present, false otherwise
	 */
	public boolean waitForExistence(int iWait) {
		return Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				return isExists();
			}
		}, iWait * 1000);
	}

	/**
	 * Waits for the calling element to appear on the page.
	 *
	 * @return true if the element is present, false otherwise
	 */
	public boolean waitForExistence() {
		return waitForExistence(Log.AUTOMATION_WAIT_VALUE_30);
	}

	/**
	 * Waits for the calling element to appear on the page.
	 *
	 * @throws AutomationException
	 *             is could not wait for presence of the element
	 */
	public void ensureWaitedForExistence() {
		if (!waitForExistence()) {
			throw new AutomationException(String.format("Could not wait for presence of the %s: %s with locator \"%s\"",
					className, identifier, locator));
		}
	}

	/**
	 * Waits for the calling element to appear on the page.
	 *
	 * @param iWait
	 *            amount of time in seconds to wait - use global time out
	 *            variables in Log.giAutomation
	 * @throws AutomationException
	 *             is could not wait for presence of the element
	 */
	public void ensureWaitedForExistence(int iWait) {
		if (!waitForExistence(iWait)) {
			throw new AutomationException(String.format("Could not wait for presence of the %s: %s with locator \"%s\"",
					className, identifier, locator));
		}
	}

	/**
	 * Determines if the specified element is visible. This method will fail if
	 * the element is not present.
	 *
	 * @return true if the specified element is visible, false otherwise
	 * @throws AutomationException
	 *             if could not check whether element is visible
	 */
	public boolean isVisible() {
		ensureWaitedForExistence();

		try {
			return (Boolean) getJsExecutor()
					.executeScript(String.format("return Ext.getCmp('%s').isVisible();", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not check whether %s is visible: %s with locator \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * Determines if the specified element is visible within a specified wait
	 * time. This method will fail if the element is not present.
	 *
	 * @param iWait
	 *            maximum time to wait for element to become visible
	 * @return true if the specified element is visible, false otherwise
	 */
	public boolean waitForVisibility(int iWait) {
		return Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				return isVisible();
			}
		}, iWait * 1000);
	}

	/**
	 * Determines if the specified element is visible within wait time. This
	 * method will fail if the element is not present.
	 *
	 * @return true if the specified element is visible, false otherwise
	 */
	public boolean waitForVisibility() {
		return waitForVisibility(Log.AUTOMATION_WAIT_VALUE_30);
	}

	/**
	 * Determines if the specified element is visible within wait time. This
	 * method will fail if the element is not present.
	 *
	 * @throws AutomationException
	 *             is could not wait for visibility of the element
	 */
	public void ensureWaitedForVisibility() {
		if (!waitForVisibility()) {
			throw new AutomationException(String.format(
					"Could not wait for visibility of the %s: %s with locator \"%s\"", className, identifier, locator));
		}
	}

	/**
	 * Determines if the specified element is visible within wait time. This
	 * method will fail if the element is not present.
	 *
	 * @param iWait
	 *            maximum time to wait for element to become visible
	 * @throws AutomationException
	 *             is could not wait for visibility of the element
	 */
	public void ensureWaitedForVisibility(int iWait) {
		if (!waitForVisibility(iWait)) {
			throw new AutomationException(String.format(
					"Could not wait for visibility of the %s: %s with locator \"%s\"", className, identifier, locator));
		}
	}

	/**
	 * This method will return true if the object is enabled and false if it is
	 * not.
	 *
	 * @return true if the object is enabled and false if it is not
	 */
	public boolean isEnabled() {
		return !isDisabled();
	}

	/**
	 * Determines if the specified element is hidden. This method will fail if
	 * the element is not present.
	 *
	 * @return true if the specified element is hidden, false otherwise
	 * @throws AutomationException
	 *             if could not check whether element is hidden
	 */
	public boolean isHidden() {
		ensureWaitedForExistence();

		try {
			return (Boolean) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').hidden;", getId()));
		} catch (Exception e) {
			throw new AutomationException(String.format("Could not check whether %s is hidden: %s with locator \"%s\"",
					className, identifier, locator), e);
		}
	}

	/**
	 * This method will return true if the object is disabled and false if it is
	 * not.
	 *
	 * @return boolean true if the object is disabled and false if it is not
	 * @throws AutomationException
	 *             if could not check whether element is disabled
	 */
	public boolean isDisabled() {
		ensureWaitedForExistence();

		try {
			return (Boolean) getJsExecutor().executeScript(String.format("return Ext.getCmp('%s').disabled;", getId()));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not check whether %s is disabled: %s with locator \"%s\"", className,
							identifier, locator),
					e);
		}
	}

	/**
	 * Returns specified Attribute as String example use: String sHref =
	 * lnkMyObject().getAttribute("href");
	 *
	 * @param attribute
	 *            the name of the object's attribute to return i.e. "href",
	 *            "title", "id", "name"
	 * @return String returns attribute of calling object as a String
	 * @throws AutomationException
	 *             if could not find
	 */
	public String getAttribute(String attribute) {
		ensureWaitedForExistence();

		try {
			return SeleniumCore.getBrowser().getAttribute(String.format("%s~%s", locator, attribute));
		} catch (Exception e) {
			throw new AutomationException(
					String.format("Could not get attribute \"%s\" of %s: %s with locator of \"%s\"", attribute,
							className, identifier, locator));
		}
	}

	/**
	 * Refreshes id of the element.
	 *
	 * @return refreshed id
	 */
	public String getId() {
		String id = getAttribute("id");

		ExtJSHelper.ensureIsExtJsExists();

		Boolean componentExists = (Boolean) getJsExecutor().executeScript(String.format(
				"return (function(){if (Ext.getCmp('%s')===undefined){return false;}else{return true;}})()", id));

		if (!componentExists) {
			int majorVersion = ExtJSHelper.getExtJsMajorVersion();
			if (majorVersion == 3) {
				id = (String) getJsExecutor().executeScript(String.format(
						"return Ext.ComponentMgr.all.find(function(c){if (c.wrap && c.wrap['id']){if (c.wrap['id']=='%s'){return c.id;}}}).id",
						id));
			} else if (majorVersion == 4) {
				id = (String) getJsExecutor().executeScript(String.format(
						"return (function(){var map=Ext.ComponentMgr.all.getValues();var id;var t='%s';for(var i=0;i<map.length;i++){if((map[i]['inputId'] && map[i]['inputId']==t) || map[i]['id']==t){id = map[i].getId();break;}}return id;})()",
						id));
			} else if (majorVersion == 5) {
				id = (String) getJsExecutor().executeScript(String.format(
						"return (function(){var map=Ext.ComponentMgr.getAll();var id;var t='%s';for(var i=0;i<map.length;i++){if((map[i]['inputId'] && map[i]['inputId']==t) || map[i]['id']==t){id = map[i].getId();break;}}return id;})()",
						id));
			}
		}

		return id;
	}

	/**
	 * Gets initialized {@link JavascriptExecutor}. This is shortcut for
	 * {@link SeleniumCore#jsExecutor}.
	 *
	 * @return {@link JavascriptExecutor} object
	 */
	public JavascriptExecutor getJsExecutor() {
		return SeleniumCore.jsExecutor;
	}

	/**
	 * Escapes string value.
	 *
	 * @param value
	 *            string for escape
	 * @return escaped string
	 */
	public static String escapeValue(String value) {
		return value.replace("'", "\\'").replace("\"", "\\\"");
	}
}
