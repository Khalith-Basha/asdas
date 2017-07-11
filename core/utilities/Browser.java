package core.utilities;

import java.util.List;

import core.utilities.Timers.Condition;
import core.utilities.exceptions.AutomationException;
import core.utilities.tools.WebDriverEngine;
import core.webwidgets.WebWidget;

/**
 * The Browser Class contains wrapper methods for general web browser functions.
 */
public class Browser {

	// **************Browser variables and methods**************

	/**
	 * Returns the name of the web browser as string
	 *
	 * @return returns web browser name as String
	 */
	public static String getWebBrowserName() {
		return SeleniumCore.getBrowser().getDisplayName();
	}

	/**
	 * Returns true if current browser if Firefox
	 *
	 * @return true if the current browser is Firefox
	 */
	public static boolean isFirefox() {
		return getWebBrowserName().equalsIgnoreCase(gsMozillaFirefox);
	}

	/**
	 * Returns true if current browser if InternetExplorer
	 *
	 * @return true if the current browser is Internet Explorer
	 */
	public static boolean isInternetExplorer() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer);
	}

	/**
	 * Returns true if current browser if InternetExplorer6
	 *
	 * @return true if the current browser is Internet Explorer 6
	 */
	public static boolean isInternetExplorer6() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer6);
	}

	/**
	 * Returns true if current browser if InternetExplorer7
	 *
	 * @return true if the current browser is Internet Explorer 7
	 */
	public static boolean isInternetExplorer7() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer7);
	}

	/**
	 * Returns true if current browser if InternetExplorer8
	 *
	 * @return true if the current browser is Internet Explorer 8
	 */
	public static boolean isInternetExplorer8() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer8);
	}

	/**
	 * Returns true if current browser if InternetExplorer9
	 *
	 * @return true if the current browser is Internet Explorer 9
	 */
	public static boolean isInternetExplorer9() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer9);

	}

	/**
	 * Returns true if current browser if InternetExplorer10
	 *
	 * @return true if the current browser is Internet Explorer 10
	 */
	public static boolean isInternetExplorer10() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer10);

	}

	/**
	 * Returns true if current browser if InternetExplorer11
	 *
	 * @return true if the current browser is Internet Explorer 11
	 */
	public static boolean isInternetExplorer11() {
		return getWebBrowserName().equalsIgnoreCase(gsInternetExplorer11);

	}

	/** Global string for Mozilla Firefox Browser */
	public static String gsMozillaFirefox = "Firefox";

	/** Global string for Microsoft Internet Explorer Browser */
	public static String gsInternetExplorer = "Internet Explorer";

	/** Global string for Microsoft Internet Explorer process name */
	public static String gsIExplore = "iexplore.exe";

	/** Global string for Microsoft Internet 6.0 Explorer Browser */
	public static String gsInternetExplorer6 = "Internet Explorer 6.0";

	/** Global string for Microsoft Internet 7.0 Explorer Browser */
	public static String gsInternetExplorer7 = "Internet Explorer 7.0";

	/** Global string for Microsoft Internet 8.0 Explorer Browser */
	public static String gsInternetExplorer8 = "Internet Explorer 8.0";

	/** Global string for Microsoft Internet 9.0 Explorer Browser */
	public static String gsInternetExplorer9 = "Internet Explorer 9.0";

	/** Global string for Microsoft Internet 10.0 Explorer Browser */
	public static String gsInternetExplorer10 = "Internet Explorer 10.0";

	/** Global string for Microsoft Internet 11.0 Explorer Browser */
	public static String gsInternetExplorer11 = "Internet Explorer 11.0";

	/** Global string for Safari Browser */
	public static String gsSafari = "Safari";

	/** Global string for Chrome Browser */
	public static String gsGoogleChrome = "Google Chrome";

	/** Global string for Opera Browser */
	public static String gsOpera = "Opera";

	/**
	 * Launches the default browser.
	 */
	public static void start() {
		Log.setEngine(new WebDriverEngine());

		SeleniumCore.getBrowser().startBrowser();
		Platform.sleep(Log.AUTOMATION_WAIT_VALUE_5);
		Log.logScriptInfo(String.format("Start Browser \"%s\" Version \"%s\" using WEBDRIVER",
				SeleniumCore.getBrowser().getDisplayName(), SeleniumCore.getBrowser().getDisplayVersion()));
	}

	/**
	 * Loads specified page/URL in browser.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 */
	public static void loadURL(String urlToLoad) {
		Log.logScriptInfo(String.format("Open page: \"%s\"", urlToLoad));
		SeleniumCore.getBrowser().loadURL(urlToLoad);
	}

	/**
	 * Loads specified page/URL in new browser window.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 * @return ID of the new browser window
	 */
	public static String loadURLInNewWindow(String urlToLoad) {
		Log.logScriptInfo(String.format("Open page: \"%s\"", urlToLoad));
		return SeleniumCore.getBrowser().loadURLInNewWindow(urlToLoad);
	}

	/**
	 * Loads specified page/URL in browser within a specified time.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 * @param wait
	 *            time in seconds to wait for page to load
	 */
	public static void loadURL(String urlToLoad, int wait) {
		Log.logScriptInfo(String.format("Open page: \"%s\"", urlToLoad));
		SeleniumCore.getBrowser().loadURL(urlToLoad, wait);
	}

	/**
	 * Loads specified page/URL in new browser window within a specified time.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 * @param wait
	 *            time in seconds to wait for page to load
	 * @return ID of the new browser window
	 */
	public static String loadURLInNewWindow(String urlToLoad, int wait) {
		Log.logScriptInfo(String.format("Open page: \"%s\"", urlToLoad));
		return SeleniumCore.getBrowser().loadURLInNewWindow(urlToLoad, wait);
	}

	/**
	 * Overloaded version of loadUrl which navigates to specified page in
	 * browser and waits until a specific object appears in browser.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 * @param wait
	 *            time to wait for page to load in seconds
	 * @param object
	 *            object to wait for on browser page after loading the URL
	 */
	public static void loadURL(String urlToLoad, int wait, WebWidget object) {
		Log.logScriptInfo(String.format("Open page: \"%s\"", urlToLoad));
		SeleniumCore.getBrowser().loadURL(urlToLoad, wait, object);
	}

	/**
	 * Overloaded version of loadURLInNewWindow which navigates to specified
	 * page in browser and waits until a specific object appears in browser.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 * @param wait
	 *            time to wait for page to load in seconds
	 * @param object
	 *            object to wait for on browser page after loading the URL
	 * @return ID of the new browser window
	 */
	public static String loadURLInNewWindow(String urlToLoad, int wait, WebWidget object) {
		Log.logScriptInfo(String.format("Open page: \"%s\"", urlToLoad));
		return SeleniumCore.getBrowser().loadURLInNewWindow(urlToLoad, wait, object);
	}

	/**
	 * Retrieves the URL of current/selected browser.
	 *
	 * @return URL as string
	 */
	public static String getURL() {
		return SeleniumCore.getBrowser().getURL();
	}

	/**
	 * Opens specified page/URL in browser.
	 *
	 * @param urlToLoad
	 *            URL to load in the current browser
	 */
	public static void open(String urlToLoad) {
		loadURL(urlToLoad);
	}

	/**
	 * Closes the current browser window - does not stop the selenium server
	 * process.
	 */
	public static void close() {
		Log.logScriptInfo("Close browser");
		stop();
	}

	/**
	 * closes the current browser window - does not stop the selenium server
	 * process.
	 */
	public static void closeWindow() {
		Log.logScriptInfo("Close browser window");
		SeleniumCore.getBrowser().closeWindow();
	}

	/**
	 * Closes the browser window by Id - does not stop the selenium server
	 * process.
	 *
	 * @param id
	 *            window id
	 */
	public static void closeWindowById(String id) {
		Log.logScriptInfo("Close browser window");
		SeleniumCore.getBrowser().closeWindowById(id);
	}

	/**
	 * Stop Selenium test.
	 */
	public static void stop() {
		SeleniumCore.getBrowser().close();
	}

	/**
	 * Makes the current browser go back using the "back" button.
	 */
	public static void goBack() {
		Log.logScriptInfo("Click Browser Back button");
		SeleniumCore.getBrowser().goBack();
	}

	/**
	 * Maximizes the current browser window - may not work with all browsers.
	 */
	public static void maximize() {
		SeleniumCore.getBrowser().windowMaximize();
	}

	/**
	 * Returns the browser window caption or title.
	 *
	 * @return the browser window caption or title
	 */
	public static String getTitle() {
		return SeleniumCore.getBrowser().getTitle();
	}

	/**
	 * Sets the current browser window to have focus.
	 */
	public static void focus() {
		SeleniumCore.getBrowser().windowFocus();
	}

	/**
	 * Checks if link exists on page.
	 *
	 * @param linkCaption
	 *            - The text caption of the link you want to check the existence
	 *            of
	 * @return true if link exists, false is link does not exist
	 */
	public static boolean linkExists(final String linkCaption) {
		return linkExists(linkCaption, Log.AUTOMATION_WAIT_VALUE_30);
	}

	/**
	 * Checks if link exists on page and waits a specified time for the link to
	 * appear.
	 *
	 * @param linkCaption
	 *            - The text caption of the link you want to check the existence
	 *            of
	 * @param wait
	 *            time out for that link to appear
	 * @return true if link exists, false is link does not exist
	 */
	public static boolean linkExists(final String linkCaption, int wait) {
		return Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				return SeleniumCore.getBrowser().isElementPresent(String.format("link=%s", linkCaption));
			}
		}, wait * 1000);
	}

	/**
	 * Checks if specified text content exists on page.
	 *
	 * @param textContent
	 *            The text content you want to check the existence of
	 * @return true if text exists, false if text does not exist
	 */
	public static boolean textExists(String textContent) {
		return textExists(textContent, Log.AUTOMATION_WAIT_VALUE_30);
	}

	/**
	 * Checks if specified text content exists on page within a specified time.
	 *
	 * @param textContent
	 *            - The text content you want to check the existence of
	 * @param wait
	 *            time out for the text to appear
	 * @return true if text exists, false if text does not exist
	 */
	public static boolean textExists(final String textContent, int wait) {
		return Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				return SeleniumCore.getBrowser().isTextPresent(textContent);
			}
		}, wait * 1000);
	}

	/**
	 * Checks and logs if specified text content exists on page within a
	 * specified time.
	 * <p>
	 *
	 * @param textContent
	 *            The text content you want to check and log the existence of
	 * @param wait
	 *            time out for the text to appear
	 * @return true if text is present on UI, false if text is not present or
	 *         exception occurs.
	 */
	public static boolean validateTextExists(final String textContent, int wait) {
		try {
			if (textExists(textContent, wait)) {
				Log.logScriptInfo(String.format("\"%s\" is present", textContent));
				return true;
			}

			Log.errorHandler(String.format("\"%s\" does not exist", textContent));
		} catch (Exception e) {
			Log.errorHandler(String.format("Error validating Text \"%s\"", textContent), e);
		}

		return false;
	}

	/**
	 * Checks and logs if specified text content exists on page.
	 * <p>
	 *
	 * @param textContent The text content you want to check and log the existence of
	 * @return true if text exists and false if it does not 
	 */
	public static boolean validateTextExists(final String textContent) {
		return validateTextExists(textContent,Log.AUTOMATION_WAIT_VALUE_5);
	}


	/**
	 * Checks and logs if specified text content exists on page within a
	 * specified time and checks if text is expected to appear.
	 *
	 * @param textContent
	 *            - The text content you want to check and log the existence of
	 * @param wait
	 *            time out for the text to appear
	 * @param expected
	 *            true if text is supposed to appear and false is text is not
	 *            supposed to appear
	 */
	public static void validateTextExists(String textContent, int wait, boolean expected) {
		try {
			if (expected) {
				if (textExists(textContent, wait)) {
					Log.logScriptInfo(String.format("\"%s\" is present, as expected", textContent));
				} else {
					Log.errorHandler(String.format("\"%s\" does not exist", textContent));
				}
			} else {
				if (!textExists(textContent, wait)) {
					Log.logScriptInfo(String.format("\"%s\" is not present, as expected", textContent));
				} else {
					Log.errorHandler(String.format("\"%s\" is present in error", textContent));
				}
			}
		} catch (Exception e) {
			Log.errorHandler(String.format("Error validating Text \"%s\"", textContent), e);
		}
	}

	/**
	 * Checks if specified list of text items exists or does not exist on page
	 * within a specified time, and logs the result.
	 *
	 * @param textContent
	 *            list containing text content you want to validate the
	 *            existence of, and log result List may include 1 or more
	 *            values, for example ("Customer Added."), ("Account is
	 *            required.", "Date is required.")
	 * @param wait
	 *            Wait Time for validating the text
	 * @param expected
	 *            true if text is expected to be present on UI; false if text is
	 *            not expected to be present on UI
	 */
	public static void validateTextExists(List<String> textContent, int wait, boolean expected) {
		try {
			if (textContent == null || textContent.isEmpty()) {
				throw new AutomationException(
						"The list passed into validateTextExists(List<String>, int, boolean) is null or empty.");
			}

			for (String aLsTextContent : textContent) {
				validateTextExists(aLsTextContent, wait, expected);
			}
		} catch (Exception e) {
			Log.errorHandler("Error validating Text in validateTextExists(List<String>, int, boolean): ", e);
		}
	}

	/**
	 * Checks and logs if specified link exists on the page within a specified
	 * time.
	 *
	 * @param linkCaption
	 *            The link you want to check and log the existence of
	 * @param wait
	 *            Wait Time for validating the text
	 * @param exists
	 *            true if link is expected to be present on page, false is link
	 *            is not expected to appear on the page.
	 */
	public static void validateLinkExists(String linkCaption, int wait, boolean exists) {
		try {
			if (exists) {
				if (linkExists(linkCaption, wait)) {
					Log.logScriptInfo(String.format("Link \"%s\" is present, as expected", linkCaption));
				} else {
					Log.errorHandler(String.format("Link \"%s\" does not exist", linkCaption));
				}
			} else {
				if (!linkExists(linkCaption, wait)) {
					Log.logScriptInfo(String.format("Link \"%s\" is not present, as expected", linkCaption));
				} else {
					Log.errorHandler(String.format("Link \"%s\" is present in error", linkCaption));
				}
			}
		} catch (Exception e) {
			Log.errorHandler(String.format("Error validating Link \"%s\"", linkCaption), e);
		}
	}

	/**
	 * Checks and logs if specified link content exists on page within a
	 * specified time.
	 *
	 * @param linkCaption
	 *            The link you want to check and log the existence of
	 * @param wait
	 *            Wait Time for validating the link
	 */
	public static void validateLinkExists(String linkCaption, int wait) {
		validateLinkExists(linkCaption, wait, true);
	}

	/**
	 * Handles Message boxes.
	 * <p>
	 * This method accepts unused sCaption parameter. Use
	 * {@link #handleMessageBox(String)}.
	 *
	 * @param caption
	 *            The text caption within the message box
	 * @param action
	 *            The action or button to click in the message box. i.e. "OK",
	 *            "Cancel", "Yes", "No", etc.
	 * @return true if message box exists, false is messagebox does not exist
	 */
	@Deprecated
	public static boolean handleMessageBox(String caption, String action) {
		return handleMessageBox(action);
	}

	/**
	 * Handles Message boxes.
	 * <p>
	 * This method accepts unused sCaption parameter.
	 *
	 * @param action
	 *            The action or button to click in the message box. i.e. "OK",
	 *            "Cancel", "Yes", "No", etc.
	 * @return true if message box exists, false is messagebox does not exist
	 */
	public static boolean handleMessageBox(String action) {
		try {
			if (action.equalsIgnoreCase("OK") || action.equalsIgnoreCase("YES") || action.equalsIgnoreCase("SAVE")
					|| action.equalsIgnoreCase("OPEN")) {
				SeleniumCore.getBrowser().chooseOkOnNextConfirmation();
			} else {
				SeleniumCore.getBrowser().chooseCancelOnNextConfirmation();
			}
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Selects a frame within the current window. (You may invoke this command
	 * multiple times to select nested frames.) To select the parent frame, use
	 * "relative=parent" as a locator; to select the top frame, use
	 * "relative=top". You can also select a frame by its 0-based index number;
	 * select the first frame with "index=0", or the third frame with "index=2".
	 * You may also use a DOM expression to identify the frame you want
	 * directly, like this: dom=frames["main"].frames["subframe"]
	 * <p>
	 *
	 * @param myLocator
	 *            an element locator identifying a frame or iframe
	 */
	public static void selectFrame(String myLocator) {
		SeleniumCore.getBrowser().selectFrame(myLocator);
	}

	/**
	 * Gets current browser window ID.
	 *
	 * @return returns ID of the current browser window
	 */
	public static String getWindowId() {
		return SeleniumCore.getBrowser().getWindowId();
	}

	/**
	 * Gets all window IDs.
	 *
	 * @return String[] - returns IDs for all windows
	 */
	public static String[] getAllWindowIds() {
		return SeleniumCore.getBrowser().getAllWindowIds();
	}

	/**
	 * Get all Windows Names.
	 *
	 * @return String[] Returns all Window Names
	 */
	public static String[] getAllWindowNames() {
		return SeleniumCore.getBrowser().getAllWindowNames();
	}

	/**
	 * Selects the Window using the window ID.
	 *
	 * @param windowID
	 *            ID of window to select
	 */
	public static void selectWindow(String windowID) {
		SeleniumCore.getBrowser().selectWindow(windowID);
	}

	/**
	 * Selects the specified popup window.
	 *
	 * @param windowID
	 *            the JavaScript window "name" of the window that will appear
	 *            (not the text of the title bar) If unspecified, or specified
	 *            as "null", this command will wait for the first non-top window
	 *            to appear (don't rely on this if you are working with multiple
	 *            popups simultaneously).
	 * @param timeout
	 *            a timeout in milliseconds, after which the action will return
	 *            with an error. If this value is not specified, the default
	 *            Selenium timeout will be used. See the setTimeout() command
	 */
	public static void waitForPopUp(String windowID, String timeout) {
		SeleniumCore.getBrowser().waitForPopUp(windowID, timeout);
	}

	/**
	 * Waits for the current page to finish loading.
	 *
	 * @param frameName
	 *            the page to wait for
	 * @return true if frame has finished loading, false otherwise
	 */
	public static boolean waitForFrame(String frameName) {
		return SeleniumCore.getBrowser().waitForFrame(frameName);
	}

	/**
	 * Get get All Window Titles.
	 *
	 * @return string array containing all of the current window titles
	 */
	public static String[] getAllWindowTitles() {
		return SeleniumCore.getBrowser().getAllWindowTitles();
	}

	/**
	 * Get Alerts.
	 *
	 * @return Alert name
	 */
	public static String getAlert() {
		return SeleniumCore.getBrowser().getAlert();
	}

	/**
	 * Refresh the web page.
	 */
	public static void refresh() {
		SeleniumCore.getBrowser().refresh();
	}

	/**
	 * Delete cookie.
	 */
	public static void deleteCookie() {
		SeleniumCore.getBrowser().deleteCookie();
	}

	/**
	 * Delete All Visible Cookies.
	 */
	public static void deleteAllVisibleCookies() {
		SeleniumCore.getBrowser().deleteAllVisibleCookies();
	}

	/**
	 * Get cookie.
	 *
	 * @return cookie as string
	 */
	public static String getCookie() {
		return SeleniumCore.getBrowser().getCookie();
	}

	/**
	 * Get Confirmation.
	 *
	 * @return confirmation as string
	 */
	public static String getConfirmation() {
		return SeleniumCore.getBrowser().getConfirmation();
	}

	/**
	 * Choose OK on Next Confirmation.
	 */
	public static void chooseOkOnNextConfirmation() {
		SeleniumCore.getBrowser().chooseOkOnNextConfirmation();
	}

	/**
	 * Choose Cancel on Next Confirmation.
	 */
	public static void chooseCancelOnNextConfirmation() {
		SeleniumCore.getBrowser().chooseCancelOnNextConfirmation();
	}

	/**
	 * Gets entire text content of active web page and returns as string.
	 *
	 * @return String - returns all web page text content as string
	 */
	public static String getPageText() {
		return SeleniumCore.getBrowser().getBodyText();
	}

	/**
	 * Clicks on dynamically generated object based on specified caption.
	 *
	 * @param caption
	 *            caption of a dynamically created object
	 * @return true if click passed successfully
	 */
	public static boolean clickPasses(String caption) {
		try {
			SeleniumCore.getBrowser().click(caption);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	/**
	 * Clicks on dynamically generated object based on specified caption.
	 *
	 * @param caption
	 *            caption of a dynamically created object
	 */
	public static void click(String caption) {
		if (clickPasses("link=" + caption)
				|| (!caption.contains("[contains") && clickPasses("//a[contains(text() '" + caption + "')]"))
				|| clickPasses("//a[text()='" + caption + "']") || clickPasses(caption)) {
			Log.logScriptInfo(String.format("Click \"%s\" object", caption));
		} else {
			Log.errorHandler(String.format("Error occurred locating dynamic object: \"%s\"", caption));
		}
	}

	/**
	 * Dynamically sets text of specified Html Text Field TestObject.
	 *
	 * @param id
	 *            html TextField TestObject identifier
	 * @param text
	 *            text to enter into text field.
	 */
	public static void setText(String id, String text) {
		try {
			Log.logScriptInfo(String.format("Enter Text \"%s\" into \"%s\" TextField", text, id));
			SeleniumCore.getBrowser().setText(id, text);
		} catch (Exception e) {
			Log.errorHandler(String.format("Error occurred locating dynamic TextField: \"%s\"", id), e);
		}
	}

	/**
	 * Waits for browser to be in ready state.
	 *
	 * @param wait
	 *            max amount of time, in seconds, to wait for ready state
	 */
	public static void waitForReady(int wait) {
		waitForReady(wait, "Browser is ready");
	}

	/**
	 * Waits up to 60 seconds (default) for browser to be ready.
	 */
	public static void waitForReady() {
		waitForReady(Log.AUTOMATION_WAIT_VALUE_60);
	}

	/**
	 * Waits for browser to be in ready state.
	 *
	 * @param wait
	 *            max amount of time, in seconds, to wait for ready state
	 * @param desc
	 *            log description info for wait
	 */
	public static void waitForReady(int wait, String desc) {
		boolean isReady = Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				return SeleniumCore.getBrowser().isReady();
			}
		}, wait * 1000);

		if (isReady) {
			Log.logScriptInfo(desc);
		} else {
			Log.errorHandler("Error occurred in waiting for readiness of browser");
		}
	}

	/**
	 * Switches control to overlay frame for Webdriver.
	 *
	 * @param overlayContainerFrame
	 *            frame to switch control to
	 * @throws Exception
	 *             error
	 */
	public static void switchToFrame(String overlayContainerFrame) throws Exception {
		Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
		SeleniumCore.driver.switchTo().frame(overlayContainerFrame);
		Platform.sleep(Log.AUTOMATION_WAIT_VALUE_5);
	}

	/**
	 * Switches control to Webpage form overlay frame for Webdriver.
	 *
	 * @throws Exception
	 *             error
	 */
	public static void switchToDefaultContent() throws Exception {
		Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
		SeleniumCore.driver.switchTo().defaultContent();
		Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
	}
}
