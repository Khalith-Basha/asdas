package core.webwidgets;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

//import com.thoughtworks.selenium.webdriven.JavascriptLibrary;

import core.utilities.Log;
import core.utilities.Platform;
import core.utilities.SeleniumCore;
import core.utilities.Strings;
import core.utilities.exceptions.AutomationException;

/**
 * The WebWidget class is a wrapper class to allow centralized control of common
 * web UI objects such as buttons, text fields, listboxes, etc and methods
 * specific to those types of objects.
 */
public class WebWidget {
	public String sLocator = "";
	public String sIdentifier = "";
	public String sClassName = "";
	public int iIndexValue = 0;
	public String widgetInfo = "";

	public String sObjName = "";
	public String sObjParent = "";
	public static boolean bExactMatch = false;

	/**
	 * WebWidget Constructor method.
	 *
	 * @param id
	 *            the locator ID or identifier for the object
	 * @param classType
	 *            the class type of the object i.e. ListBox, TextField, Button,
	 *            etc
	 */
	public WebWidget(String id, String classType) {
		this(id, classType, "");
	}

	/**
	 * WebWidget Constructor method with added additional constructors to allow
	 * for multiple identifiers.
	 *
	 * @param id
	 *            the locator ID or identifier for the object
	 * @param classType
	 *            the class type of the object i.e. ListBox, TextField, Button,
	 *            etc
	 * @param label
	 *            caption to be used instead of the default one - overrides the
	 *            default label, shouldn't contain white spaces or white spaces
	 *            will be removed
	 */
	public WebWidget(String id, String classType, String label) {
		sLocator = id;
		sClassName = classType;

		try {
			StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
			for (int i = 1; i < stackTrace.length; i++) {
				if (!stackTrace[i].getMethodName().equals("<init>")) {
					String className = stackTrace[i].getClassName();
					if (label == null || label.isEmpty()) {
						label = stackTrace[i].getMethodName();
					}

					sIdentifier = String.format("%s.%s", className.substring(className.lastIndexOf(".") + 1),
							Strings.removeWhiteSpace(label));
					break;
				}
			}

			widgetInfo = Log.AUTOMATION_LOCATOR_PRINT
					? String.format("%s %s with locator \"%s\"", sIdentifier, sClassName, sLocator)
					: String.format("%s %s", sIdentifier, sClassName);
		} catch (Exception e) {
			Log.errorHandler("Error occurred with definition of object", e);
		}
	}

	/**
	 * Gets text from text field type widget.
	 *
	 * @return String returns text value of calling object
	 */
	public String getText() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getText(sLocator);
		}

		Log.errorHandler(getWidgetNotFoundMessage());
		return "";
	}

	/**
	 * Gets text from text field type widget ignores any errors associated with
	 * the getText() action.
	 *
	 * @return String returns text value of calling object
	 */
	public String getTextIgnoreError() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getText(sLocator);
		}
		// Log.logScriptInfo("Could not find " + sClassName + ": " + sIdentifier
		// + " with locator of " + "\"" + sLocator + "\"");
		// Log.errorHandler("Could not find " + sClassName + ": " + sIdentifier
		// + " with locator of " + "\"" + sLocator + "\"");
		return "";
	}

	/**
	 * Clicks on calling object.
	 */
	public void click() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click %s", widgetInfo));
			SeleniumCore.getBrowser().click(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Clicks right mouse button on calling object.
	 */
	public void clickRightMouse() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click right mouse button on %s", widgetInfo));
			SeleniumCore.getBrowser().clickMouseButton(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Clicks a widget found using exact match for sLocator string passed in.
	 */
	public void clickExactMatch() {
		try {
			if (SeleniumCore.getBrowser().exists(sLocator)) {
				Log.logScriptInfo(String.format("Click %s", widgetInfo));
				SeleniumCore.getBrowser().clickExactMatch(sLocator);
			} else {
				Log.errorHandler(getWidgetNotFoundMessage());
			}
		} catch (Exception e) {
			Log.errorHandler(String.format("Could not find exact match for %s: %s with locator of \"%s\"", sClassName,
					sIdentifier, sLocator), e);
		}
	}

	/**
	 * Clicks on calling object.
	 */
	public void clickAt() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click %s", widgetInfo));
			SeleniumCore.getBrowser().clickAt(sLocator, "");
		} else {
			// Log.logScriptInfo("Could not find " + sClassName + ": " +
			// sIdentifier + " with locator of " + "\"" + sLocator + "\"");
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Simulates keystroke events on the specified element, as though you typed
	 * the value key-by-key.
	 *
	 * @param text
	 *            the text to type
	 */
	public void typeKeys(String text) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Type Text \"%s\" into %s", text, widgetInfo));
			SeleniumCore.getBrowser().typeKeys(sLocator, text);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Simulates keystroke events on the specified element, as though you typed
	 * the value key-by-key.
	 *
	 * @param keySequence
	 *            keys to type
	 */
	public void keyPress(String keySequence) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Type Text \"%s\" into %s", keySequence, widgetInfo));
			SeleniumCore.getBrowser().keyPress(sLocator, keySequence);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Double clicks the right mouse button on an object.
	 */
	public void doubleClick() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("DoubleClick %s", widgetInfo));
			SeleniumCore.getBrowser().doubleClick(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Simulates a user hovering a mouse over the specified element.
	 */
	public void hover() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			SeleniumCore.getBrowser().focus(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Sets focus on an object if the element is an input field, move the cursor
	 * to that field.
	 */
	public void focus() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Actions action = new Actions(SeleniumCore.driver);
			action.moveToElement(
					SeleniumCore.driver.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator))).perform();
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Returns true if an object is found, false if object is not found.
	 *
	 * @return true if an object is found, false if object is not found
	 */
	public boolean exists() {
		waitForExistence(Log.AUTOMATION_WAIT_VALUE_60);
		return SeleniumCore.getBrowser().isElementPresent(sLocator);
	}

	/**
	 * Returns true if an object is found and false if it is not found - waits
	 * for a specified amount of time before timing out.
	 *
	 * @param wait
	 *            - specify the amount of time to wait for existence using
	 *            Log.giAutomation time variables.
	 * @return Returns true if an object is found and false if it is not found
	 */
	public boolean exists(int wait) {
		waitForExistence(wait);
		return SeleniumCore.getBrowser().isElementPresent(sLocator);
	}

	/**
	 * Waits for the calling element to appear on the page.
	 *
	 * @param wait
	 *            amount of time in seconds to wait - use global time out
	 *            variables in Log.giAutomation
	 * @return true if the element is present, false otherwise
	 */
	public boolean waitForExistence(int wait) {
		// Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
		boolean isElementPresent = false;
		for (int iteration = 0; iteration < wait; iteration++) {
			try {
				isElementPresent = SeleniumCore.getBrowser().isElementPresent(sLocator);
				if (isElementPresent) {
					break;
				}
			} catch (Exception e) {
			}
			Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
		}

		return isElementPresent;
	}

	/**
	 * Waits for the calling element to be visible on the page.
	 *
	 * @param wait
	 *            time in seconds to wait for the object to be visisble
	 * @return true if the element is visible, false otherwise
	 */
	public boolean waitForVisibility(int wait) {
		Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
		boolean isVisible = false;
		for (int iteration = 0; iteration < wait; iteration++) {
			try {
				isVisible = SeleniumCore.getBrowser().isVisible(sLocator);
				if (isVisible) {
					break;
				}
			} catch (Exception e) {
			}
			Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
		}

		return isVisible;
	}

	/**
	 * Determines if the specified element is visible. This method will fail if
	 * the element is not present.
	 *
	 * @return true if the specified element is visible, false otherwise
	 */
	public boolean isVisible() {
		return SeleniumCore.getBrowser().isVisible(sLocator);
	}

	/**
	 * Determines if the specified element is visible within a specified wait
	 * time. This method will fail if the element is not present.
	 *
	 * @param wait
	 *            maximum time to wait for element to become visible
	 * @return true if the specified element is visible, false otherwise
	 */
	public boolean isVisible(int wait) {
		return SeleniumCore.getBrowser().isVisible(sLocator, wait);
	}

	/**
	 * Simulates a user pressing and releasing a key in the active window.
	 *
	 * @param arg0
	 *            - key to press
	 */
	public void keyPressNative(String arg0) {
		SeleniumCore.getBrowser().keyPressNative(arg0);
	}

	/**
	 * Logs whether element is visible or not depending on the bVisible option
	 * passed.
	 *
	 * @param visible
	 *            the expected boolean value. true to verify object is visible
	 *            and false to verify object is not visible
	 */
	public void verifyVisible(boolean visible) {
		if (visible) {
			verifyVisible();
		} else {
			if (isVisible()) {
				Log.errorHandler(String.format("%s is Visible", widgetInfo));
			} else {
				Log.logScriptInfo(String.format("Verify %s is NOT Visible", widgetInfo));
			}
		}
	}

	/**
	 * Logs whether element is visible.
	 */
	public void verifyVisible() {
		if (isVisible()) {
			Log.logScriptInfo(String.format("Verify %s is Visible", widgetInfo));
		} else {
			Log.errorHandler(String.format("%s is NOT Visible", widgetInfo));
		}
	}

	/**
	 * Determines if the specified element is present. This method will fail if
	 * the element is not present.
	 *
	 * @return true if the specified element is present, false otherwise
	 */
	public boolean isElementPresent() {
		return SeleniumCore.getBrowser().isElementPresent(sLocator);
	}

	/**
	 * Returns true if the element is present. If the element is not found and
	 * bLogError is true an error will be logged, otherwise no error will be
	 * produced only a log entry stating that the object could not be found.
	 *
	 * @param logError
	 *            if set to true, error is logged
	 * @return true if element is present
	 */
	public boolean isElementPresent(boolean logError) {
		boolean found = (SeleniumCore.getBrowser().isElementPresent(sLocator));

		if (!found) {
			if (logError) {
				Log.errorHandler(getWidgetNotFoundMessage());
			} else {
				Log.logScriptInfo(getWidgetNotFoundMessage());
			}
		}

		return found;
	}

	/**
	 * Simulates a user hovering a mouse over the specified element.
	 */
	public void mouseOver() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			SeleniumCore.getBrowser().mouseOver(sLocator);
			// mouseOver not working for most apps! switching to focus()
			// instead...
			// SeleniumCore.getBrowser().mouseOver(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Determines if the specified element is present. This method will fail if
	 * the element is not present.
	 *
	 * @return true if the specified element is present, false otherwise
	 */
	public boolean verifyElementIsPresent() {
		Boolean isPresent = null;

		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Verified object %s is present", widgetInfo));
			isPresent = SeleniumCore.getBrowser().isElementPresent(sLocator);
		} else {
			isPresent = false;
			Log.errorHandler(getWidgetNotFoundMessage());
		}

		return isPresent;
	}

	/**
	 * Sets text into a text field type widget.
	 *
	 * @param text
	 *            value to type into the text field type widget
	 */
	public void setText(String text) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Enter Text \"%s\" into %s", text, widgetInfo));
			SeleniumCore.getBrowser().type(sLocator, text);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Returns x coordinate as integer.
	 *
	 * @return x coordinate of calling object
	 */
	public int getX() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getX(sLocator);
		} else {
			throw new AutomationException(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Returns y coordinate as integer.
	 *
	 * @return y coordinate of calling object
	 */
	public int getY() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getY(sLocator);
		} else {
			throw new AutomationException(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Returns width.
	 *
	 * @return width of calling object
	 */
	public int getWidth() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getWidth(sLocator);
		} else {
			throw new AutomationException(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Returns height.
	 *
	 * @return height of calling object
	 */
	public int getHeight() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			return SeleniumCore.getBrowser().getHeight(sLocator);
		} else {
			throw new AutomationException(getWidgetNotFoundMessage());
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
	 */
	public String getAttribute(String attribute) {
		try {
			if (SeleniumCore.getBrowser().exists(sLocator)) {
				return SeleniumCore.getBrowser().getAttribute(sLocator + "~" + attribute);
			}

			Log.errorHandler(getWidgetNotFoundMessage());
			return "";
		} catch (Exception e) {
			// Log.errorHandler("Could not get " + "\"" + "@" + sAttribute +
			// "\"" + " attribute for object " + sIdentifier);
			return "";
		}
	}

	/**
	 * Returns specified Attribute from hidden element as String example use:
	 * String sHref = lnkMyObject().getAttribute("href");
	 *
	 * @param attribute
	 *            the name of the object's attribute to return i.e. "href",
	 *            "title", "id", "name"
	 * @return String returns attribute of calling object as a String
	 */
	public String getHiddenAttribute(String attribute) {
		try {
			return SeleniumCore.getBrowser().getAttribute(sLocator + "~" + attribute);
		} catch (Exception e) {
			Log.errorHandler(String.format("Could not find attribute \"%s\" from element with locator of \"%s\"",
					attribute, sLocator));
			return "";
		}
	}

//	/**
//	 * Execute event on EXTJS component Field Object.
//	 *
//	 * @param event
//	 *            ExtJS Field identifier
//	 */
//	public void fireEvent(String event) {
//		if (SeleniumCore.getBrowser().exists(sLocator)) {
//			WebElement element = SeleniumCore.driver
//					.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator));
//			JavascriptLibrary javascript = new JavascriptLibrary();
//			javascript.callEmbeddedSelenium(SeleniumCore.driver, "triggerEvent", element, event);
//
//			// Actions test = new Actions(SeleniumCore.driver);
//			// test.moveToElement(SeleniumCore.driver.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator)));
//			// test.click(SeleniumCore.driver.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator))).build().perform();
//		} else {
//			Log.errorHandler(getWidgetNotFoundMessage());
//		}
//	}

	/**
	 * Drag and Drop specified elements.
	 *
	 * @param myLocatorTo
	 *            - locator to drop object into
	 */
	public void dragAndDrop(String myLocatorTo) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Drag from %s to \"%s\"", widgetInfo, myLocatorTo));
			SeleniumCore.getBrowser().dragAndDrop(sLocator, myLocatorTo);
		}
	}

	/**
	 * Drag and Drop specified elements.
	 *
	 * @param objTo
	 *            - object to drop object into
	 */
	public void dragAndDrop(WebWidget objTo) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Drag from %s to %s", widgetInfo, objTo.widgetInfo));
			SeleniumCore.getBrowser().dragAndDrop(sLocator, objTo.sLocator);
		}
	}

	/**
	 * Counts how many nodes match the specified xpath.
	 *
	 * @return int returns the number of nodes that match the specified xpath
	 *         used to count number of rows in table
	 */
	public int countXpaths() {
		return SeleniumCore.getBrowser().getXpathCount(sLocator).intValue();
	}

	/**
	 * This method will return true if the object is disabled and false if it is
	 * not.
	 *
	 * @return boolean true if the object is disabled and false if it is not
	 */
	public boolean isDisabled() {
		boolean isDisabled = false;
		try {
			if (SeleniumCore.getBrowser().exists(sLocator)) {
				String disabled = "";
				// disabled = SeleniumCore.getBrowser().getAttribute(sLocator +
				// "@disabled");
				// it's not correctly working if we put in sLocator short xPath
				// like: new TextField("id=login_field")

				// correct
				final WebElement element = SeleniumCore.driver
						.findElement(SeleniumCore.getBrowser().convertLocatorToBy(sLocator));
				disabled = element.getAttribute("disabled");

				if (disabled != null) {
					isDisabled = disabled.equals("true") || disabled.equals("");
				}
			} else {
				// More consistent to consider an unexisting item as disabled
				isDisabled = true;
				// Log.errorHandler("Could not find " + sClassName + ": " +
				// sIdentifier + " with locator of " + "\"" + sLocator + "\"");

			}
		} catch (Exception e) {
			// As we are checking for null above don't know in which condition
			// we might get here might be better to consider the item as
			// disabled in this case
			isDisabled = false;
			// Log.errorHandler("Could not get " + "\"" + "@" + sAttribute +
			// "\"" + " attribute for object " + sIdentifier);
		}

		return isDisabled;
	}

	/**
	 * This method will return true if the object is enabled and false if it is
	 * not.
	 *
	 * @return true if the object is enabled and false if it is not.
	 */
	public boolean isEnabled() {
		return !isDisabled();
	}

	/**
	 * Mimics typing a key into calling object.
	 *
	 * @param key
	 *            key to type into calling object widget i.e.
	 *            GoogleMainPage.tfSearchText().sendKeys(Keys.BACK_SPACE);
	 */
	public void sendKeys(Keys key) {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Press Key %s into %s", key.name(), widgetInfo));
			SeleniumCore.getBrowser().sendKeys(sLocator, key);
		}
	}

	/**
	 * Clicks on calling object inside ExtJS WebTable.
	 */
	public void clickInsideExtJSWebTable() {
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			Log.logScriptInfo(String.format("Click %s", widgetInfo));
			SeleniumCore.getBrowser().clickInsideExtJSWebTable(sLocator);
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
	}

	/**
	 * Get List of elements by XPath.
	 *
	 * @return list of web elements
	 */
	public List<WebElement> getListOfElements() {
		List<WebElement> webElements = new ArrayList<>();
		if (SeleniumCore.getBrowser().exists(sLocator)) {
			webElements = SeleniumCore.driver.findElements(SeleniumCore.getBrowser().convertLocatorToBy(sLocator));
		} else {
			Log.errorHandler(getWidgetNotFoundMessage());
		}
		return webElements;
	}

	/**
	 * Returns completed error message for not found widget to print into log.
	 *
	 * @return error message for not found widget
	 */
	protected String getWidgetNotFoundMessage() {
		return String.format("Could not find %s: %s with locator of \"%s\"", sClassName, sIdentifier, sLocator);
	}
}
