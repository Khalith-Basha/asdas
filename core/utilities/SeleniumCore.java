package core.utilities;

import java.awt.Robot;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.browserstack.local.Local;

import core.webwidgets.WebWidget;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;

// import com.opera.core.systems.OperaDriver; //pre-2.46
// import org.openqa.selenium.WebDriverBackedSelenium; //library required for Selenium version 2.39 and below

/**
 * The SeleniumCore class contains core selenium commands and functions that are
 * wrapped in other core library classes and should be called from other core
 * libraries.
 */
public class SeleniumCore {

	public static WebDriver driver;
	public static JavascriptExecutor jsExecutor;
	public static Local browserstacklocal;
	public static Proxy proxy;
	private static String defaultBrowser = "*firefox";
	private static String browser_safari = "*safari";
	private static String browser_firefox = "*firefox";
	private static String browser_ie = "*iexplore";
	private static String browser_chrome = "*googlechrome";
	private static String browser_opera = "*opera";
	private static String currentBrowser = "";
	private static String userAgent = "";
	private static String browserVersion;

	public class BrowserName {
		public static final String IE = "Internet Explorer";
		public static final String SAFARI = "Safari";
		public static final String FIREFOX = "Firefox";
		public static final String CHROME = "Google Chrome";
		public static final String OPERA = "Opera";
	}

	/**
	 * returns the current browser name
	 *
	 * @return String Current Browser Name
	 */
	public String getCurrentBrowserName() {
		return currentBrowser;
	}

	/**
	 * sets the browser used by Automation
	 *
	 * @param browserName
	 *            browser name to be used
	 */
	public static void setCurrentBrowser(final String browserName) {
		if (browserName.toLowerCase().contains("safari")) {
			currentBrowser = browser_safari;
		}
		if (browserName.toLowerCase().contains("explorer")) {
			currentBrowser = browser_ie;
		}
		if (browserName.toLowerCase().contains("firefox")) {
			currentBrowser = browser_firefox;
		}
		if (browserName.toLowerCase().contains("chrome")) {
			currentBrowser = browser_chrome;
		}
		if (browserName.toLowerCase().contains("opera")) {
			currentBrowser = browser_opera;
		}
		defaultBrowser = currentBrowser;
	}

	/**
	 * static method used to return an instance of the seleniumHelper class
	 *
	 * @return seleniumHelper instance
	 */
	public static seleniumHelper getBrowser() {
		return new seleniumHelper();
	}

	/**
	 * wraps many Selenium methods with logging
	 * <p>
	 * Please use the getBrowser() instance...
	 */
	public static class seleniumHelper {

		/**
		 * The following group of methods are browser information methods
		 */

		/**
		 * returns a text name for the current browser
		 *
		 * @return String Browser Display Name
		 */
		public String getDisplayName() {
			String myBrowserName = "";
			if (currentBrowser.equals(browser_ie)) {
				myBrowserName = BrowserName.IE;
			}
			if (currentBrowser.equals(browser_safari)) {
				myBrowserName = BrowserName.SAFARI;
			}
			if (currentBrowser.equals(browser_firefox)) {
				myBrowserName = BrowserName.FIREFOX;
			}
			if (currentBrowser.equals(browser_chrome)) {
				myBrowserName = BrowserName.CHROME;
			}
			if (currentBrowser.equals(browser_opera)) {
				myBrowserName = BrowserName.OPERA;
			}
			return myBrowserName;
		}

		/**
		 * returns version number for the current browser as string
		 *
		 * @return String Browser version name
		 */
		public String getDisplayVersion() {

			try {
				if (currentBrowser.equals(browser_ie)) {
					if (userAgent.contains("MSIE ")) {
						browserVersion = userAgent.split("MSIE ")[1].split("; Windows")[0];
					} else {
						browserVersion = userAgent.split(" rv:")[1].split("\\) like Gecko")[0];
					}
				}
				if (currentBrowser.equals(browser_safari)) {
					browserVersion = userAgent.split("Safari/")[1].split(" Safari")[0];
				}
				if (currentBrowser.equals(browser_firefox)) {
					browserVersion = userAgent.split("Firefox/")[1];
				}
				if (currentBrowser.equals(browser_chrome)) {
					browserVersion = userAgent.split("Chrome/")[1].split(" Safari")[0];
				}
				if (currentBrowser.equals(browser_opera)) {
					browserVersion = userAgent.split("Opera/")[1].split(" ")[0];
				}

			} catch (final Exception e) {
			}

			return browserVersion;
		}

		// ******************************************************************************
		// The following methods are browser control methods start / shutdown /
		// loadURL
		// ******************************************************************************

		/**
		 * launches the browser passed in as an argument, starts the Selenium
		 * server if needed
		 *
		 * @param browserName
		 *            browser to start
		 */
		public void start(String browserName) {
			if (browserName.toLowerCase().equals("default")) {
				if (Platform.isWindows()) {
					browserName = Browser.gsMozillaFirefox;
				}
				if (Platform.isMac()) {
					browserName = Browser.gsSafari;
				}
				if (Platform.isLinux()) {
					browserName = Browser.gsMozillaFirefox;
				}
			}

			SeleniumCore.setCurrentBrowser(browserName);

			proxy = getProxy();

			// BrowserStack Support
			if (Log.AUTOMATION_BROWSERSTACK_ENABLE) {
				initializeBrowserStack();
			} else {
				// WebDriver
				if (browserName.contains("chrome")) {
					initializeChromeDriver();
				} else if (browserName.contains("ie")) {
					final DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
					if (proxy != null) {
						capabilities.setCapability(CapabilityType.PROXY, proxy);
					}
					capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
					capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
							true);
					capabilities.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
					System.setProperty("webdriver.ie.driver", Log.AUTOMATION_TEST_PROJECT_PATH + "IEDriverServer.exe");
					driver = new InternetExplorerDriver(capabilities);
				} else if (browserName.contains("safari")) {
					driver = new SafariDriver();
				} else if (browserName.contains("opera")) {
					final DesiredCapabilities capabilities = DesiredCapabilities.operaBlink(); // .opera();
					if (proxy != null) {
						capabilities.setCapability(CapabilityType.PROXY, proxy);
					}
					capabilities.setCapability("opera.binary", Log.AUTOMATION_BROWSER_PATH); // "C:/Program
																								// Files/Opera
																								// x64/opera.exe"
					capabilities.setCapability("opera.arguments", "-nowin -nomail");
					driver = new OperaDriver(capabilities);
				} else {// FireFox is default
					
					//Uncomment for Selenium 3.x
					//System.setProperty("webdriver.gecko.driver",Log.AUTOMATION_TEST_PROJECT_PATH + "geckodriver.exe");
					
					FirefoxProfile profile = null;

					try {
						if (Log.AUTOMATION_BROWSER_PROFILE != null) {
							if (Files.exists(Paths.get(Log.AUTOMATION_BROWSER_PROFILE))) {
								profile = new FirefoxProfile(new File(Log.AUTOMATION_BROWSER_PROFILE));
							} else {
								profile = new ProfilesIni().getProfile(Log.AUTOMATION_BROWSER_PROFILE);
							}

							Log.logScriptInfo(
									"The Firefox Browser has been configured using the following user profile file: "
											+ profile);
						} else {
							profile = new FirefoxProfile();
						}

					} catch (final Exception e) {
						Log.errorHandler(
								"Error loading browser profile file found in AUTOMATION_BROWSER_PROFILE property in the Automation.properties file",
								e);
						profile = new FirefoxProfile();
					}

					profile.setPreference("browser.download.folderList", 2);
					profile.setPreference("browser.download.manager.showWhenStarting", false);
					profile.setPreference("browser.helperApps.neverAsk.saveToDisk",
							"application/octet-stream,application/pdf,text/csv,application/csv,application/vnd.ms-excel,text/plain,application-download,application/x-pdf");
					profile.setPreference("plugin.disable_full_page_plugin_for_types", "application/pdf");
					profile.setPreference("pdfjs.disabled", true);
					profile.setPreference("plugin.scan.plid.all", false);
					// profile.setPreference("security.tls.version.max", 0);
					// profile.setPreference("security.tls.version.min", 0);

					final DesiredCapabilities capabilities = DesiredCapabilities.firefox();
					if (proxy != null) {
						capabilities.setCapability(CapabilityType.PROXY, proxy);
					}

					if (Log.AUTOMATION_BROWSER_PATH != null) {
						String file = Log.AUTOMATION_BROWSER_PATH;

						if (!file.contains("firefox.exe") && !FileIO.getFileExtension(file).equals(".exe")) {
							file = Paths.get(file, "firefox.exe").toString();
						}

						final File firefoxFile = new File(file);
						if (!firefoxFile.isFile()) {
							Log.errorHandler(
									String.format("Could not find Firefox browser in specified location: %s", file));
							return;
						}

						FirefoxBinary binary = new FirefoxBinary(firefoxFile);
						driver = new FirefoxDriver(binary, profile, capabilities);
					} else {
						capabilities.setCapability(FirefoxDriver.PROFILE, profile);
						driver = new FirefoxDriver(capabilities);
					}
				}

				if (driver != null && !browserName.contains("opera")) {
					driver.manage().window().maximize();
				}
			}

			jsExecutor = (JavascriptExecutor) driver;
			userAgent = (String) jsExecutor.executeScript("return navigator.userAgent;");
		}

		/**
		 * launches the default browser, starts the Selenium server if needed
		 */
		public void startBrowser() {
			start(defaultBrowser);
		}

		/**
		 * launches specified page in browser and waits 60 seconds for page to
		 * load
		 *
		 * @param url
		 *            URL to load in the current browser
		 */
		public void loadURL(final String url) {
			loadURL(url, Log.AUTOMATION_WAIT_VALUE_60);
		}

		/**
		 * Launches specified page in new browser window and waits 60 seconds
		 * for page to load.
		 *
		 * @param url
		 *            URL to load in the current browser
		 * @return ID of the new browser window
		 */
		public String loadURLInNewWindow(final String url) {
			return loadURLInNewWindow(url, Log.AUTOMATION_WAIT_VALUE_60);
		}

		/**
		 * launches specified page in browser
		 *
		 * @param url
		 *            URL to load in the current browser
		 * @param timeout
		 *            time to wait for page to load in seconds
		 */
		public void loadURL(final String url, final int timeout) {
			try {
				int count = 0;

				driver.get(url);
				Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);

				while (driver.getTitle().isEmpty() && count < timeout) {
					count++;
					Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
				}
			} catch (final Exception e2) {
				Log.errorHandler("Error Loading URL: " + url, e2);
			}
		}

		/**
		 * Launches specified page in new browser window.
		 *
		 * @param url
		 *            URL to load in the current browser
		 * @param timeout
		 *            time to wait for page to load in seconds
		 * @return ID of the new browser window
		 */
		public String loadURLInNewWindow(final String url, final int timeout) {
			try {
				int count = 0;

				final Set<String> oldWindowHendles = driver.getWindowHandles();

				jsExecutor.executeScript("window.open();");

				final Set<String> newWindowHendles = driver.getWindowHandles();
				newWindowHendles.removeAll(oldWindowHendles);
				final String newWindowHendler = newWindowHendles.iterator().next();

				driver.switchTo().window(newWindowHendler);

				driver.get(url);
				Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);

				while (driver.getTitle().isEmpty() && count < timeout) {
					count++;
					Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
				}

				return newWindowHendler;
			} catch (final Exception e2) {
				Log.errorHandler("Error Loading URL: " + url, e2);
				return null;
			}
		}

		/**
		 * Overloaded version of loadUrl which navigates to specified page in
		 * browser and waits until a specific object appears in browser
		 *
		 * @param url
		 *            URL to load in the current browser
		 * @param timeout
		 *            time to wait for page to load in seconds
		 * @param object
		 *            object to wait for to be displayed in browser
		 */
		public void loadURL(final String url, final int timeout, final WebWidget object) {
			try {
				driver.get(url);
				for (int count = 0; count <= timeout; count++) {
					if (object.isVisible()) {
						break;
					}

					Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
				}
			} catch (final Exception e2) {
				Log.errorHandler("Error Loading URL: " + url, e2);
			}
		}

		/**
		 * Overloaded version of loadUrlInNewWindow which navigates to specified
		 * page in new browser window and waits until a specific object appears
		 * in browser.
		 *
		 * @param url
		 *            URL to load in the current browser
		 * @param timeout
		 *            time to wait for page to load in seconds
		 * @param object
		 *            object to wait for to be displayed in browser
		 * @return ID of the new browser window
		 */
		public String loadURLInNewWindow(final String url, final int timeout, final WebWidget object) {
			try {
				final Set<String> oldWindowHendles = driver.getWindowHandles();

				jsExecutor.executeScript("window.open();");

				final Set<String> newWindowHendles = driver.getWindowHandles();
				newWindowHendles.removeAll(oldWindowHendles);
				final String newWindowHendler = newWindowHendles.iterator().next();

				driver.switchTo().window(newWindowHendler);

				driver.get(url);
				for (int count = 0; count <= timeout; count++) {
					if (object.isVisible()) {
						break;
					}

					Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
				}

				return newWindowHendler;

			} catch (final Exception e2) {
				Log.errorHandler("Error Loading URL: " + url, e2);
				return null;
			}
		}

		/**
		 * retrieves the Url of the current/selected browser window
		 *
		 * @return URL
		 */
		public String getURL() {
			return driver.getCurrentUrl();
		}

		/**
		 * closes the current browser window - does not stop the selenium server
		 * process
		 */
		public void close() {
			try {
				driver.quit();
			} catch (final Exception e) {
			}
		}

		/**
		 * closes the current browser window - does not stop the selenium server
		 * process
		 */
		public void closeWindow() {
			try {
				driver.close();
			} catch (final Exception e) {
			}
		}

		/**
		 * Closes the browser window by name or handle - does not stop the
		 * selenium server process.
		 *
		 * @param id
		 *            - browser window name or handle
		 */
		public void closeWindowById(final String id) {
			try {
				driver.switchTo().window(id).close();
			} catch (final Exception e) {
			}
		}

		/**
		 * Makes the current browser go back using the "back" button
		 */
		public void goBack() {
			driver.navigate().back();
		}

		/**
		 * maximizes the current browser window - may not work with all browsers
		 */
		public void windowMaximize() {
			driver.manage().window().maximize();
		}

		/**
		 * Sets the current browser window to have focus
		 */
		public void windowFocus() {
			jsExecutor.executeScript("window.focus;");
		}

		// **********************************************************************
		// The following methods are browser action methods - click / type /
		// read
		// **********************************************************************

		/**
		 * sets text into a password text field type widget
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param value
		 *            value to type into the password text field type widget
		 */
		public void typePW(final String locator, final String value) {
			driver.findElement(convertLocatorToBy(locator)).clear();
			driver.findElement(convertLocatorToBy(locator)).sendKeys(value);
		}

		// **********************************************************************
		// The following methods are browser action methods - click / type /
		// read
		// **********************************************************************
		
		/**
		 * sets text into a text field type widget
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param value
		 *            value to type into the text field type widget
		 */
		public void type(final String locator, final String value) {
			driver.findElement(convertLocatorToBy(locator)).clear();
			driver.findElement(convertLocatorToBy(locator)).sendKeys(value);
		}

		/**
		 * wrapper for the typePW method
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param value
		 *            value to type into the text field type widget
		 */
		public void setPassword(final String locator, final String value) {
			typePW(locator, value);
		}

		/**
		 * wrapper for the type method
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param value
		 *            value to type into the text field type widget
		 */
		public void setText(final String locator, final String value) {
			driver.findElement(convertLocatorToBy(locator)).clear();
			driver.findElement(convertLocatorToBy(locator)).sendKeys(value);
		}

		/**
		 * wrapper for the sendKeys method. Mimics typing key into calling
		 * object
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param key
		 *            key to type into calling object widget
		 */
		public void sendKeys(final String locator, final Keys key) {
			driver.findElement(convertLocatorToBy(locator)).sendKeys(key);
		}

		/**
		 * Simulates keystroke events on the specified element, as though you
		 * typed the value key-by-key.
		 * <p>
		 *
		 * @param locator
		 *            an element locator
		 * @param value
		 *            the value to type
		 */
		public void typeKeys(final String locator, final String value) {
			driver.findElement(convertLocatorToBy(locator)).clear();
			driver.findElement(convertLocatorToBy(locator)).sendKeys(value);
		}

		/**
		 * convertLocatorToBy(String sLocator,boolean bExactMatch) returns a
		 * WebDriver By datatype from a given string locator value
		 * <p>
		 *
		 * @param locator
		 *            Selenium RC locator to be converted to WebDriver By
		 *            locator type
		 * @param exactMatch
		 *            true is locator must be exact match
		 * @return By
		 */
		public By convertLocatorToBy(final String locator, final boolean exactMatch) {
			final String name = "name=";
			final String id = "id=";
			final String link = "link=";
			final String css = "css=";
			final String xpath = "xpath=";

			// New Locators
			final String partialLink = "partialLinkText=";
			final String exactLink = "exactLink=";
			final String className = "class=";

			if (locator.startsWith(name)) {
				// System.out.println(locator.substring(sName.length()));
				return By.name(locator.substring(name.length()));
			}

			else if (locator.startsWith(id)) {
				// System.out.println(sLocator.substring(sId.length()));
				return By.id(locator.substring(id.length()));
			}

			else if (locator.startsWith(css)) {
				// System.out.println(locator.substring(sCss.length()));
				return By.cssSelector(locator.substring(css.length()));
			}

			else if (locator.startsWith(link)) {
				if (exactMatch) {
					return By.linkText(locator.substring(link.length()));
				}
				return By.partialLinkText(locator.substring(link.length()));
			}

			else if (locator.startsWith(xpath)) {
				// System.out.println(locator.substring(sXpath.length()));
				return By.xpath(locator.substring(xpath.length()));
			}

			else if (locator.startsWith(partialLink)) {
				return By.partialLinkText(locator.substring(partialLink.length()));

			} else if (locator.startsWith(exactLink)) {
				return By.linkText(locator.substring(exactLink.length()));

			}

			else if (locator.startsWith(className)) {
				return By.className(locator.substring(className.length()));
			}

			else {
				// System.out.println(locator);
				return By.xpath(locator);
			}
		}

		/**
		 * convertLocatorToBy(String sLocator,boolean bExactMatch) returns a
		 * WebDriver By datatype from a given string locator partial value
		 * <p>
		 *
		 * @param locator
		 *            Selenium RC locator to be converted to WebDriver By
		 *            locator type
		 * @return By
		 */
		public By convertLocatorToBy(final String locator) {
			return convertLocatorToBy(locator, WebWidget.bExactMatch);
		}

		/**
		 * clicks a widget found using the sLocator string passed in
		 *
		 * @param locator
		 *            string used to locate the widget
		 */
		public void click(final String locator) {
			if (Log.AUTOMATION_TEST_BROWSER.equals("Internet Explorer")) {
				if (browserVersion.contains("8") || browserVersion.contains("9") || browserVersion.contains("10")) {
					ieClick(locator);
				} else if (browserVersion.contains("11")) {
					driver.findElement(convertLocatorToBy(locator)).click();
				}
			} else {
				driver.findElement(convertLocatorToBy(locator)).click();
			}
		}

		/**
		 * clicks on calling object for IE Browsers used in click() method
		 */
		private void ieClick(final String locator) {
			jsExecutor.executeScript("arguments[0].click()", driver.findElement(convertLocatorToBy(locator)));
		}

		/**
		 * clicks inside ExtJsWebTable object
		 *
		 * @param locator
		 *            locator string
		 */
		public void clickInsideExtJSWebTable(final String locator) {
			// works only in WebDriver
			jsExecutor.executeScript("Ext.ComponentQuery.query('grid')[0].getSelectionModel().select(0);",
					driver.findElement(convertLocatorToBy(locator)));
		}

		/**
		 * alternate mouse over method for IE browsers If the normal mouseOver()
		 * does not work in your IE browser try this one
		 *
		 * @param locator
		 *            locator string
		 */
		public void mouseOverforIE(final String locator) {
			Point coordinates;
			Robot robot = null;
			try {
				robot = new Robot();

				coordinates = driver.findElement(convertLocatorToBy(locator)).getLocation();
				robot.mouseMove(coordinates.getX(), coordinates.getY() + 120);

			} catch (final Exception e) {
				Log.errorHandler("Error performing mouseover in method mouseoverforIE()", e);
			}
		}

		/**
		 * clicks a widget found using a sLocator string that is an exact match
		 *
		 * @param locator
		 *            - exact match string used to locate the widget
		 */
		public void clickExactMatch(final String locator) {
			driver.findElement(convertLocatorToBy(locator, true)).click();
		}

		/**
		 * Selects an item in a listbox found using the sLocator string passed
		 * in
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param value
		 *            item to select in the listbox
		 */
		public void select(final String locator, final String value) {
			new Select(driver.findElement(convertLocatorToBy(locator))).selectByValue(value);
		}

		/**
		 * Selects an item using the text value in a listbox found using the
		 * sLocator string passed in
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param text
		 *            item to select in the listbox by the text of the item
		 */
		public void selectByText(final String locator, final String text) {
			new Select(driver.findElement(convertLocatorToBy(locator))).selectByVisibleText(text);
		}

		/**
		 * Selects an item in a listbox found using the sLocator and Label
		 * string values (Option visible string value).
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param label
		 *            item to select in the list box
		 * @throws Exception
		 *             if listbox widget is not found
		 */
		public void selectByLabel(final String locator, final String label) throws Exception {
			try {
				driver.findElement(this.convertLocatorToBy(String.format("%s/option[text()=\"%s\"]", locator, label)))
						.click();
			} catch (final Exception e) {
				throw new Exception("Option not found");
			}
		}

		/**
		 * Selects an item in a listbox found using the sLocator and the partial
		 * text of an option in a list.
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param label
		 *            item to select in the list box
		 * @throws Exception
		 *             if listbox is not found
		 */
		public void selectByPartialLabel(final String locator, final String label) throws Exception {
			try {
				driver.findElement(
						this.convertLocatorToBy(String.format("%s/option[contains(text(), \"%s\")]", locator, label)))
						.click();
			} catch (final Exception e) {
				throw new Exception("Option not found");
			}
		}

		/**
		 * Selects an item in a listbox found using the sLocator and Index value
		 * Note: not available for Selenium RC version 1
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param index
		 *            Index item to select in the list box
		 */
		public void selectByIndex(final String locator, final int index) {
			new Select(driver.findElement(this.convertLocatorToBy(locator))).selectByIndex(index);
		}

		/**
		 * Clicks on a link, button, check box or radio button at a specific
		 * point
		 * <p>
		 *
		 * @param locator0
		 *            an element locator
		 * @param locator1
		 *            specifies the x,y position (i.e. - 10,20) of the mouse
		 *            event relative to the element returned by the locator.
		 */
		public void clickAt(final String locator0, final String locator1) {
			new Actions(driver).moveToElement(driver.findElement(convertLocatorToBy(locator0))).click().build()
					.perform();
			// driver.findElement(convertLocatorToBy(sLocator0)).click();
		}

		/**
		 * clicks the right mouse button on the widget found using the sLocator
		 * string passed in
		 *
		 * @param sLocator
		 *            string used to locate the widget
		 */
		public void clickMouseButton(final String sLocator) {
			new Actions(driver).contextClick(driver.findElement(convertLocatorToBy(sLocator))).click().build()
					.perform();
		}

		/**
		 * clicks the right mouse button on the widget found using the sLocator
		 * string passed in
		 *
		 * @param locator
		 *            string used to locate the widget
		 */
		public void rightClick(final String locator) {
			new Actions(driver).contextClick(driver.findElement(convertLocatorToBy(locator))).perform();
		}

		/**
		 * Double clicks the right mouse button on the widget found using the
		 * sLocator string passed in
		 *
		 * @param locator
		 *            - string used to locate the widget
		 */
		public void doubleClick(final String locator) {
			new Actions(driver).doubleClick(driver.findElement(convertLocatorToBy(locator))).perform();
		}

		/**
		 * Simulates a user hovering a mouse over the specified element.
		 *
		 * @param locator
		 *            element locator
		 */
		public void hover(final String locator) {
			new Actions(driver).moveToElement(driver.findElement(convertLocatorToBy(locator))).perform();
		}

		/**
		 * Simulates a user hovering a mouse over the specified element.
		 *
		 * @param locator
		 *            element locator
		 */
		public void mouseOver(final String locator) {
			new Actions(driver).moveToElement(driver.findElement(convertLocatorToBy(locator))).build().perform();
			Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
			Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
		}

		/**
		 * Move the focus to the specified element; for example, if the element
		 * is an input field, move the cursor to that field.
		 *
		 * @param locator
		 *            element locator
		 */
		public void focus(final String locator) {
			new Actions(driver).moveToElement(driver.findElement(convertLocatorToBy(locator))).perform();
		}

		/**
		 * Simulates a user pressing and releasing a key.
		 *
		 * @param locator
		 *            element locator
		 * @param keySequence
		 *            Either be a string("\" followed by the numeric keycode of
		 *            the key to be pressed, normally the ASCII value of that
		 *            key), or a single character. For example: "w", "\119".
		 */
		public void keyPress(final String locator, final String keySequence) {
			new Actions(driver).sendKeys(driver.findElement(convertLocatorToBy(locator)), keySequence).perform();
		}

		/**
		 * Simulates a user pressing and releasing a key in the active window.
		 *
		 * @param arg0
		 *            - key
		 */
		public void keyPressNative(final String arg0) {
			Platform.sendKeyPress(Integer.valueOf(arg0));
		}

		/**
		 * Simulates a user pressing a key (without releasing it yet).
		 *
		 * @param locator
		 *            element locator
		 * @param keySequence
		 *            Either be a string("\" followed by the numeric keycode of
		 *            the key to be pressed, normally the ASCII value of that
		 *            key), or a single character. For example: "w", "\119".
		 */
		public void keyDown(final String locator, final String keySequence) {
			new Actions(driver).sendKeys(driver.findElement(convertLocatorToBy(locator)), keySequence).perform();
		}

		/**
		 * Simulates a user releasing a key.
		 *
		 * @param locator
		 *            element locator
		 * @param keySequence
		 *            Either be a string("\" followed by the numeric keycode of
		 *            the key to be pressed, normally the ASCII value of that
		 *            key), or a single character. For example: "w", "\119".
		 */
		public void keyUp(final String locator, final String keySequence) {
			new Actions(driver).sendKeys(driver.findElement(convertLocatorToBy(locator)), keySequence).perform();
		}

		/**
		 * Submit the specified form. This is particularly useful for forms
		 * without submit buttons, e.g. single-input "Search" forms.
		 *
		 * @param locator
		 *            an element locator for the form you want to submit
		 */
		public void submit(final String locator) {
			driver.findElement(convertLocatorToBy(locator)).submit();
		}

		/**
		 * Checks a checkbox found using the sLocator string passed in
		 *
		 * @param locator
		 *            string used to locate the widget
		 */
		public void check(final String locator) {
			final WebElement element = driver.findElement(convertLocatorToBy(locator));
			if (!element.isSelected()) {
				element.click();
			}
		}

		/**
		 * Unchecks a checkbox found using the sLocator string passed in
		 *
		 * @param locator
		 *            string used to locate the widget
		 */
		public void uncheck(final String locator) {
			final WebElement element = driver.findElement(convertLocatorToBy(locator));
			if (element.isSelected()) {
				element.click();
			}
		}

		/**
		 * Gets whether a toggle-button (checkbox/radio) is checked. Fails if
		 * the specified element doesn't exist or isn't a toggle-button.
		 *
		 * @param locator
		 *            an element locator pointing to a checkbox or radio button
		 * @return true if the checkbox is checked, false otherwise
		 */
		public boolean ischecked(final String locator) {
			return driver.findElement(convertLocatorToBy(locator)).isSelected();
		}

		/**
		 * Gets whether a element is selected. Fails if the specified element
		 * doesn't exist or isn't selectable.
		 *
		 * @param locator
		 *            an element locator pointing to a selectable item
		 * @return true if the element is selected, false otherwise
		 */
		public boolean isSelected(final String locator) {
			return driver.findElement(convertLocatorToBy(locator)).isSelected();
		}

		// **********************************************************
		// validation methods - exists / ready / etc
		// **********************************************************

		/**
		 * Returns true/false if a widget is found using the sLocator string
		 * passed in
		 *
		 * @param locator
		 *            - string used to locate the widget
		 * @return true if object exists
		 */
		public boolean exists(final String locator) {
			waitForElementPresent(Log.AUTOMATION_WAIT_VALUE_10, locator);
			return isElementPresent(locator);
		}

		/**
		 * Returns true/false if the current browser's ready state is "complete"
		 * <p>
		 * may not work with certain objects may not work as expected on Google
		 * Chrome
		 *
		 * @param timeOut
		 *            time to wait
		 * @return boolean true/false if page is ready
		 */
		public boolean isReady(final int timeOut) {
			try {
				final ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {
					@Override
					public Boolean apply(final WebDriver driver) {
						return ((JavascriptExecutor) driver).executeScript("return document.readyState;")
								.equals("complete");
					}
				};

				final Wait<WebDriver> wait = new WebDriverWait(driver, timeOut);

				try {
					wait.until(expectation);
					return true;
				} catch (final Exception error) {
					throw error;
				}
			} catch (final Exception e) {
				// System.out.println("window.document.readyState="
				// +browser.getEval("window.document.readyState"));
				return false;
			}
		}

		/**
		 * Returns true/false if the current browser's ready state is "complete"
		 * <p>
		 * may not work with certain objects may not work as expected on Google
		 * Chrome
		 *
		 * @return boolean true/false if page is ready
		 */
		public boolean isReady() {
			return isReady(Log.AUTOMATION_WAIT_VALUE_60 * 2);
		}

		/**
		 * Waits for the current page frame to finish loading
		 * <p>
		 * may not work with certain objects
		 *
		 * @param frameName
		 *            the name of the frame
		 * @return boolean true/false if page is ready (beyond the timeout)
		 */
		public boolean waitForFrame(final String frameName) {
			boolean foundFrame = false;

			for (int i = 0; i < Log.AUTOMATION_WAIT_VALUE_60; i++) {
				try {
					driver.switchTo().frame(frameName);

					foundFrame = true;
					break;
				} catch (final Exception e) {
					Platform.sleep(Log.AUTOMATION_WAIT_VALUE_2);
				}
			}
			return foundFrame;
		}

		/**
		 * Returns a true/false if the widget/element is found on the page
		 *
		 * @param locator
		 *            used to locate the element of the current page
		 * @return true/false if the element is found
		 */
		public boolean isElementPresent(final String locator) {
			try {
				driver.findElement(convertLocatorToBy(locator));
				return true;
			} catch (final Exception e) {
				return false;
			}
		}

		/**
		 * Waits for a specified time for object to become present and returns
		 * true is the object is present or false if the object is not present
		 * <p>
		 *
		 * @param locator
		 *            the locator as string of element to wait for
		 * @param timeOut
		 *            time to wait in seconds
		 * @return true if element is present
		 */
		public boolean isElementPresent(final String locator, final long timeOut) {
			// wait for element to be present
			final ExpectedCondition<WebElement> elementPresentCondition = ExpectedConditions
					.presenceOfElementLocated(convertLocatorToBy(locator));
			final WebDriverWait wait = new WebDriverWait(driver, timeOut);
			wait.until(elementPresentCondition);

			// now check for element and return true if found, false if not
			// present
			try {
				driver.findElement(convertLocatorToBy(locator));
				return true;
			} catch (final Exception e) {
				return false;
			}
		}

		/**
		 * Returns a string containing the current browser page body text or
		 * page source
		 *
		 * @return String of the current browser body text or page source
		 */
		public String getBodyText() {
			return driver.getPageSource();
		}

		/**
		 * Returns a true/false if the text is found on the page
		 *
		 * @param text
		 *            string to search for on the page
		 * @return true/false if the text is found
		 */
		public boolean isTextPresent(final String text) {
			try {
				return driver.getPageSource().contains(text);
			} catch (final Exception e) {
				return false;
			}
		}

		/**
		 * Determines if the specified element is visible. This method will fail
		 * if the element is not present.
		 *
		 * @param locator
		 *            an element locator
		 * @return true if the specified element is visible, false otherwise
		 */
		public boolean isVisible(final String locator) {
			try {
				return driver.findElement(convertLocatorToBy(locator)).isDisplayed();
			} catch (final Exception ex) {
				return false;
			}
		}

		/**
		 * Waits for the specified element to be visible.
		 *
		 * @param locator
		 *            Element String locator
		 * @param timeOut
		 *            integer containing number of seconds to wait
		 * @return true if the specified element is visible
		 */
		public boolean isVisible(final String locator, final int timeOut) {
			try {
				final By byLocator = this.convertLocatorToBy(locator);

				final ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {

					@Override
					public Boolean apply(final WebDriver input) {
						if (driver.findElements(byLocator).size() > 0) {
							return input.findElement(byLocator).isDisplayed();
						}

						return false;
					}
				};

				final Wait<WebDriver> wait = new WebDriverWait(driver, timeOut);

				try {
					wait.until(expectation);
					return true;

				} catch (final Exception error) {
					throw error;
				}
			} catch (final Exception ex) {
				return false;
			}
		}

		/**
		 * Determines if the specified element is enabled. This method will
		 * return false is element is not present.
		 *
		 * @param locator
		 *            an element locator
		 * @return true if the specified element is enabled, false otherwise
		 */
		public boolean isElementEnabled(final String locator) {
			try {
				return driver.findElement(this.convertLocatorToBy(locator)).isEnabled();
			} catch (final Exception ex) {
				return false;
			}

		}

		/**
		 * Determines if the specified element is editable. This method will
		 * fail if the element is not present.
		 * <p>
		 *
		 * @param locator
		 *            an element locator
		 * @return true if the specified element is editable, false otherwise
		 */
		public boolean isEditable(final String locator) {
			try {
				return driver.findElement(convertLocatorToBy(locator)).isEnabled();
			} catch (final Exception ex) {
				return false;
			}

		}

		/**
		 * Assert the condition is true
		 *
		 * @param message
		 *            description of comparison
		 * @param condition
		 *            comparison resulting in a boolean
		 */
		public void assertTrue(final String message, final boolean condition) {
			Log.verify(true, condition, message);
		}

		/**
		 * Like assertTrue, but won't end the execution, assert the condition is
		 * true
		 *
		 * @param condition
		 *            comparison resulting in a boolean
		 * @return true/false
		 */
		public boolean verifyTrue(final boolean condition) {
			return Log.verify(true, condition, "");
		}

		/**
		 * Asserts that two string arrays have identical string contents
		 *
		 * @param text1
		 *            expected string content
		 * @param text2
		 *            actual string content
		 * @return true if strings match, false otherwise
		 */
		public boolean verifyEquals(final String text1, final String text2) {
			return Log.verify(text1, text2, true);
		}

		/**
		 * Waits for specified seconds to find and return a web element.
		 *
		 * @param byLocator
		 *            of web element
		 * @param timeOut
		 *            amount of time in seconds to wait for an object before
		 *            timing out and throwing an error
		 * @throws TimeoutException
		 *             if element not found
		 * @return Web Element
		 */
		public WebElement getElement(final By byLocator, final int timeOut) throws TimeoutException {
			new WebDriverWait(driver, timeOut).until(ExpectedConditions.presenceOfElementLocated(byLocator));

			return driver.findElement(byLocator);
		}

		/**
		 * Gets the (whitespace-trimmed) value of an input field (or anything
		 * else with a value parameter). For check-box/radio elements, the value
		 * will be "on" or "off" depending on whether the element is checked or
		 * not.
		 * <p>
		 *
		 * @param locator
		 *            an element locator
		 * @return value
		 */
		public String getValue(final String locator) {
			return driver.findElement(convertLocatorToBy(locator)).getAttribute("value");
		}

		/**
		 * Gets the text of an element. This works for any element that contains
		 * text.
		 * <p>
		 *
		 * @param locator
		 *            an element locator
		 * @return text string containing the content of the text field
		 */
		public String getText(final String locator) {
			return driver.findElement(convertLocatorToBy(locator)).getText();
		}

		/**
		 * Gets option value (value attribute) for selected option in the
		 * specified selected element.
		 *
		 * @param locator
		 *            object locator identifier
		 * @return the selected option value in the specified select drop-down
		 */
		public String getSelectedValue(final String locator) {
			return driver.findElement(convertLocatorToBy(locator)).getAttribute("value");
		}

		/**
		 * Gets the text of selected item in a listbox found using the sLocator
		 * string passed in
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @return selected listbox item text
		 */
		public String getSelectedItemText(final String locator) {
			return new Select(driver.findElement(convertLocatorToBy(locator))).getFirstSelectedOption().getText();
		}

		/**
		 * Gets text of the multiple selected items in a multi-select listbox
		 * object
		 *
		 * @param locator
		 *            identifier or locator for the ListBox object to get items
		 *            from
		 * @return a string list of selected items from the specified
		 *         multi-select listbox.
		 */
		public ArrayList<String> getAllSelectedOptions(final String locator) {
			final ArrayList<String> selOptionsList = new ArrayList<>();
			final List<WebElement> optList = new Select(driver.findElement(convertLocatorToBy(locator)))
					.getAllSelectedOptions();

			for (final WebElement anOptList : optList) {
				selOptionsList.add(anOptList.getText());
			}

			return selOptionsList;
		}

		/**
		 * Drag and Drop specified elements.
		 *
		 * @param locatorFrom
		 *            - locator to grab object from
		 * @param locatorTo
		 *            - locator to drop object into
		 */
		public void dragAndDrop(final String locatorFrom, final String locatorTo) {
			final WebElement fromItem = driver.findElement(convertLocatorToBy(locatorFrom));
			final WebElement toItem = driver.findElement(convertLocatorToBy(locatorTo));
			new Actions(driver).dragAndDrop(fromItem, toItem).build().perform();
		}

		/**
		 * Selects a frame within the current window. (You may invoke this
		 * command multiple times to select nested frames.) To select the parent
		 * frame, use "relative=parent" as a locator; to select the top frame,
		 * use "relative=top". You can also select a frame by its 0-based index
		 * number; select the first frame with "index=0", or the third frame
		 * with "index=2". You may also use a DOM expression to identify the
		 * frame you want directly, like this:
		 * dom=frames["main"].frames["subframe"]
		 * <p>
		 *
		 * @param locator
		 *            an element locator identifying a frame or iframe
		 */
		public void selectFrame(final String locator) {
			driver.switchTo().frame(locator);
		}

		/**
		 * Selects a popup window using a window locator; once a popup window
		 * has been selected, all commands go to that window. To select the main
		 * window again, use null as the target.
		 *
		 * @param windowID
		 *            the JavaScript window ID of the window to select
		 */

		public void selectWindow(final String windowID) {
			driver.switchTo().window(windowID);
		}

		/**
		 * Waits for a popup window to appear and load up.
		 * <p>
		 *
		 * @param windowID
		 *            the JavaScript window "name" of the window that will
		 *            appear (not the text of the title bar) If unspecified, or
		 *            specified as "null", this command will wait for the first
		 *            non-top window to appear (don't rely on this if you are
		 *            working with multiple popups simultaneously).
		 * @param timeout
		 *            a timeout in milliseconds, after which the action will
		 *            return with an error. If this value is not specified, the
		 *            default Selenium timeout will be used. See the
		 *            setTimeout() command
		 */
		public void waitForPopUp(final String windowID, final String timeout) {
			for (int iteration = 0; iteration < Integer.valueOf(timeout); iteration++) {

				try {
					for (final String handle : driver.getWindowHandles()) {
						driver.switchTo().window(handle);
					}
				} catch (final Exception e) {
				}
				Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
			}
		}

		/**
		 * Waits for a specified time for object to become visible.
		 * <p>
		 *
		 * @param locator
		 *            the locator as string for method to wait for visibility of
		 * @param timeout
		 *            time to wait in seconds
		 */
		public void waitUntilVisible(final String locator, final long timeout) {
			final ExpectedCondition<WebElement> elementVisibleCondition = ExpectedConditions
					.visibilityOfElementLocated(convertLocatorToBy(locator));

			final WebDriverWait wait = new WebDriverWait(driver, timeout);

			if (wait.until(elementVisibleCondition) == null) {
				wait.until(angularHasFinishedProcessing());
			}
		}

		/**
		 * Verifies that the specified element is somewhere on the page. extend
		 * the super method to wait in a cycle for 60 seconds
		 * <p>
		 *
		 * @param locator
		 *            element locator
		 * @param timeout
		 *            maximum time to wait for object to appear
		 * @return true if the element is present, false otherwise
		 */
		public boolean waitForElementPresent(final int timeout, final String locator) {
			// Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);

			// for(int iteration = 0; iteration < iWait; iteration++)
			// {
			// try{
			// driver.findElement(convertLocatorToBy(sLocator));
			// return true;
			// }
			// catch (Exception e){//System.out.println("waiting..." +
			// iteration);
			// }
			//
			// Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
			// }
			waitUntilVisible(locator, timeout);

			return false;
		}

		/**
		 * Prints all links found with default locators
		 */
		public void printAllLinks() {
			final List<WebElement> link = driver.findElements(By.tagName("a"));
			for (final WebElement ele : link) {
				System.out.println(ele.getText());
			}
		}

		/**
		 * Gets the Xpath Count
		 *
		 * @param arg0
		 *            locator for element
		 * @return xpath count as Number type
		 */
		public Number getXpathCount(final String arg0) {
			return driver.findElements(convertLocatorToBy(arg0)).size();
		}

		/**
		 * Gets X coordinate of the object from the top left-hand corner.
		 *
		 * @param locator
		 *            Locator value
		 * @return int x coordinate
		 */
		public int getX(final String locator) {
			return driver.findElement(this.convertLocatorToBy(locator)).getLocation().getX();
		}

		/**
		 * Gets Y coordinate of the object from the top left-hand corner.
		 *
		 * @param locator
		 *            Locator value
		 * @return int y coordinate
		 */
		public int getY(final String locator) {
			return driver.findElement(this.convertLocatorToBy(locator)).getLocation().getY();
		}

		/**
		 * Gets width of the object.
		 *
		 * @param locator
		 *            Locator value
		 * @return int width
		 */
		public int getWidth(final String locator) {
			return driver.findElement(this.convertLocatorToBy(locator)).getSize().getWidth();
		}

		/**
		 * Gets height of the object.
		 *
		 * @param locator
		 *            Locator value
		 * @return int height
		 */
		public int getHeight(final String locator) {
			return driver.findElement(this.convertLocatorToBy(locator)).getSize().getHeight();
		}

		/**
		 * Gets the object Attributes
		 *
		 * @param arg0
		 *            locator with attribute info
		 * @return String containing requested object attribute
		 */
		public String getAttribute(final String arg0) {
			return driver.findElement(convertLocatorToBy(arg0.split("~")[0])).getAttribute(arg0.split("~")[1]); // requires
																												// 2
																												// args
																												// for
																												// WebDriver
																												// TBD
		}

		/**
		 * Returns an attribute value for the specified attribute name of a
		 * given object
		 *
		 * @param locator
		 *            Locator value
		 * @param attribute
		 *            Attribute name
		 * @return Attribute value
		 */
		public String getAttribute(final String locator, final String attribute) {
			return driver.findElement(this.convertLocatorToBy(locator)).getAttribute(attribute);
		}

		/**
		 * Returns All Window Ids in a String array
		 *
		 * @return All Window Ids in a String array
		 */
		public String[] getAllWindowIds() {
			String ls[];
			int x = 0;

			final Set<String> handles = driver.getWindowHandles();
			ls = new String[handles.size()];
			for (final String handle : handles) {
				ls[x] = handle;
				x++;
			}
			return ls;

		}

		/**
		 * Gets Window Id
		 *
		 * @return String Window Handle
		 */
		public String getWindowId() {
			return driver.getWindowHandle();
		}

		/**
		 * Gets Window Title
		 *
		 * @return String Window Title
		 */
		public String getTitle() {
			return driver.getTitle();
		}

		/**
		 * Returns all Window Names in a String array
		 *
		 * @return all Window Names in a String array
		 */
		public String[] getAllWindowNames() {
			String ls[];
			int x = 0;

			final Set<String> handles = driver.getWindowHandles();
			ls = new String[handles.size()];
			for (final String handle : handles) {
				ls[x] = driver.switchTo().window(handle).getTitle();
				x++;
			}
			return ls;
		}

		/**
		 * Returns all Window Titles in a String array
		 *
		 * @return all window titles as a string array
		 */
		public String[] getAllWindowTitles() {
			String ls[];
			int x = 0;

			final Set<String> handles = driver.getWindowHandles();
			ls = new String[handles.size()];
			for (final String handle : handles) {
				ls[x] = driver.switchTo().window(handle).getTitle();
				x++;
			}
			return ls;
		}

		/**
		 * Returns an Alert as string
		 *
		 * @return an Alert as string
		 */
		public String getAlert() {
			return driver.switchTo().alert().getText();
		}

		/**
		 * Refresh the page
		 */
		public void refresh() {
			driver.navigate().refresh();
		}

		/**
		 * Deletes all Cookies
		 */
		public void deleteCookie() {
			driver.manage().deleteAllCookies();
		}

		/**
		 * deletes All Visible Cookies
		 */
		public void deleteAllVisibleCookies() {
			driver.manage().deleteAllCookies();
		}

		/**
		 * Gets cookies as string
		 *
		 * @return cookie
		 */
		public String getCookie() {
			return driver.manage().getCookies().toString();
		}

		/**
		 * Get the alert confirmation text
		 *
		 * @return the alert confirmation text
		 */
		public String getConfirmation() {
			return driver.switchTo().alert().getText();
		}

		/**
		 * Accept confirmation alert
		 */
		public void chooseOkOnNextConfirmation() {
			driver.switchTo().alert().accept();
		}

		/**
		 * Choose Cancel on confirmation alert
		 */
		public void chooseCancelOnNextConfirmation() {
			driver.switchTo().alert().dismiss();
		}

		/**
		 * DeSelects an item in a listbox found using the sLocator and Text
		 * Label string values (Option visible string value)
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param label
		 *            text item to deselect in the list box
		 * @throws Exception
		 *             if option is not found
		 */
		public void deSelectByLabel(final String locator, final String label) throws Exception {
			new Select(driver.findElement(this.convertLocatorToBy(locator))).deselectByVisibleText(label);
		}

		/**
		 * Deselects an item in a listbox found using the sLocator and the
		 * partial text of an option in a list
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param label
		 *            partial text of item to deselect in the list box
		 * @throws Exception
		 *             if item is not found
		 */
		public void deSelectByPartialLabel(final String locator, final String label) throws Exception {
			final Select listbox = new Select(driver.findElement(this.convertLocatorToBy(locator)));
			final List<WebElement> options = listbox.getOptions();
			boolean found = false;

			for (final WebElement option : options) {
				if (option.getText().contains(label.trim())) {
					found = true;
					final String sOption = option.getText();
					listbox.deselectByVisibleText(sOption);
					break;
				}
			}

			if (!found) {
				throw new Exception("List option not found");
			}
		}

		/**
		 * DeSelects an item in a listbox found using the sLocator and value of
		 * list option
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param value
		 *            Value to deselect in the list box
		 * @throws Exception
		 *             if option is not found
		 */
		public void deSelectByValue(final String locator, final String value) throws Exception {
			new Select(driver.findElement(this.convertLocatorToBy(locator))).deselectByValue(value);
		}

		/**
		 * DeSelects an item in a listbox found using the sLocator and value of
		 * list option
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the widget
		 * @param index
		 *            value to deselect in the list box
		 * @throws Exception
		 *             if option is not found
		 */
		public void deSelectByIndex(final String locator, final int index) throws Exception {
			new Select(driver.findElement(this.convertLocatorToBy(locator))).deselectByIndex(index);
		}

		/**
		 * DeSelects all items in a listbox found using the sLocator
		 * <p>
		 *
		 * @param locator
		 *            string used to locate the list widget
		 * @throws Exception
		 *             if list object is not found
		 */
		public void deSelectAll(final String locator) throws Exception {
			new Select(driver.findElement(this.convertLocatorToBy(locator))).deselectAll();
		}

		/**
		 * Returns the name of a specified table
		 *
		 * @param locator
		 *            value of the table object
		 * @return name of table
		 */
		public String getTableName(final String locator) {
			try {
				final WebElement table = this.getElement(this.convertLocatorToBy(locator),
						Log.AUTOMATION_WAIT_VALUE_10);
				return table.getText();
			} catch (final Exception e) {
				Log.errorHandler("getTableName() failed", e);
				return "";
			}
		}

		/**
		 * Returns the count of rows in a specified table
		 *
		 * @param locator
		 *            value of the table object
		 * @return number of rows
		 */
		public int getTableRowCount(final String locator) {
			try {
				final WebElement table = this.getElement(this.convertLocatorToBy(locator),
						Log.AUTOMATION_WAIT_VALUE_10);

				// Now get all the TR elements from the table
				return table.findElements(By.tagName("tr")).size();
			} catch (final TimeoutException e) {
				Log.errorHandler("getTableRowCount() failed. Unable to locate: " + locator + " - " + e.getMessage());
				return 0;
			} catch (final Exception e) {
				Log.errorHandler("getTableRowCount() failed: " + e.getMessage());
				return 0;
			}
		}

		/**
		 * Returns column data data for a specific column number as a list array
		 *
		 * @param locator
		 *            locator for table object
		 * @param columnIndex
		 *            column number starting with 1
		 * @return list of column data
		 */
		public List<String> getTableColumnData(final String locator, final int columnIndex) {
			boolean found = false;
			final List<String> cellData = new ArrayList<>();

			try {
				final WebElement table = this.getElement(this.convertLocatorToBy(locator),
						Log.AUTOMATION_WAIT_VALUE_10);

				// Now get all the TR elements from the table
				final List<WebElement> rows = table.findElements(By.tagName("tr"));
				if (rows.size() > 0) {
					// And iterate over rows, getting the cell
					for (final WebElement row : rows) {
						final List<WebElement> cells = row.findElements(By.tagName("td"));

						if (cells.size() > 0) {
							cellData.add(cells.get(columnIndex).getText());
							found = true;
						}
					}
				}

				if (!found) {
					throw new Exception("Column: " + columnIndex + " not found");
				}

			} catch (final Exception e) {
				Log.errorHandler("getTableColumnData() failed: " + e);
			}
			return cellData;
		}

		/**
		 * Searches table rows on specified column to acquire link for matching
		 * item name
		 *
		 * @param locator
		 *            String Locator value of the table object
		 * @param columnDataMatch
		 *            name of the link to find
		 * @param columnOnly
		 *            column location containing item name and link
		 * @return String locator for link
		 */
		public String getTableColumnLink(String locator, final String columnDataMatch, final int columnOnly) {
			int index = -1;

			try {
				final List<String> cellData = this.getTableColumnData(locator, columnOnly);

				for (int iteration = 0; iteration < cellData.size(); iteration++) {

					if (cellData.get(iteration).equalsIgnoreCase(columnDataMatch)) {
						index = iteration;
						break;
					}
				}

				if (index > -1) { // make the link identifier
					// sLocator = byLocator.toString().substring(10);
					locator = locator.substring(10);
					if (index == 0) {
						return locator.concat(String.format("/tbody/tr/td[%s]/a", columnOnly));
					}

					return locator.concat(String.format("/tbody/tr[%s]/td[%s]/a", index, columnOnly));
				}

				throw new Exception("No matching column data for: " + columnDataMatch);

			} catch (final Exception e) {
				Log.errorHandler("getTableColumnLink() failed: " + e);
				return null;
			}
		}

		/**
		 * Returns the cell value of a given column and row in a given table
		 * object
		 *
		 * @param locator
		 *            locator for table object
		 * @param columnIndex
		 *            column number starting with 1
		 * @param rowIndex
		 *            row number starting with 1
		 * @return cell value as String
		 */
		public String getTableCellValue(final String locator, final int columnIndex, final int rowIndex) {
			int rowNum = 0;

			try {
				final WebElement table = this.getElement(this.convertLocatorToBy(locator),
						Log.AUTOMATION_WAIT_VALUE_10);

				// Now get all the TR elements from the table
				final List<WebElement> rows = table.findElements(By.tagName("tr"));

				if (rows.size() > 0) {
					// And iterate over rows, getting the cell
					for (final WebElement row : rows) {
						final List<WebElement> cells = row.findElements(By.tagName("td"));
						rowNum++;

						if (cells.size() > 0) {
							if (rowIndex == rowNum) {
								return cells.get(columnIndex).getText();
							}
						}
					}
				}
			} catch (final Exception e) {
				Log.errorHandler("getTableCellValue() failed: " + e);
			}
			return "";
		}

		/**
		 * Apply Escape key to active web element
		 */
		public static void keyboardEscape() {
			driver.switchTo().activeElement().sendKeys(Keys.ESCAPE);
		}

		/**
		 * Apply Tab key to active web element
		 */
		public static void keyboardTab() {
			driver.switchTo().activeElement().sendKeys(Keys.TAB);
		}

		/**
		 * Send Escape key to active element but element not re-focused.
		 * Alternative to keyboardEscape().
		 */
		public static void keyboardSendEscapeKey() {
			new Actions(driver).sendKeys(Keys.ESCAPE).perform();
		}

		/**
		 * Send Tab key to active element but element not re-focused.
		 * Alternative to keyboardTab().
		 */
		public static void keyboardSendTabKey() {
			new Actions(driver).sendKeys(Keys.TAB).perform();
		}

		private ExpectedCondition<Boolean> angularHasFinishedProcessing() {
			return new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(final WebDriver driver) {
					return Boolean.valueOf(jsExecutor
							.executeScript(
									"return (function(){var el=document.querySelector('*[ng-app],*[data-ng-app],*[ng-controller]');if(!el)return "
											+ "true;if(!window.angular){return true}return"
											+ "(window.angular!=null)&&(angular.element(el).injector()!=null)&&(angular.element(el).injector().get('$http').pendingRequests.length===0)})()")
							.toString());
				}
			};
		}

		/**
		 * Initializes chrome driver with default and user defined options.
		 */
		private static void initializeChromeDriver() {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();

			if (proxy != null) {
				capabilities.setCapability("proxy", proxy);
			}

			ChromeOptions options = new ChromeOptions();
			options.addArguments("silent=true");
			options.addArguments("verbose=false");
			options.addArguments("start-maximized");

			final String chromeCommandLine = Log.AUTOMATION_CHROME_COMMANDLINE;
			if (!Strings.isEmpty(chromeCommandLine)) {
				final String[] args = chromeCommandLine.trim().replace("--", "").replace("  ", " ").split(" ");
				options.addArguments(args);
			}

			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			System.setProperty("webdriver.chrome.driver", Log.AUTOMATION_TEST_PROJECT_PATH + "chromedriver.exe");
			driver = new ChromeDriver(capabilities);
		}

		/**
		 * Initializes BrowserStack with default and user defined options.
		 */
		private static void initializeBrowserStack() {

			DesiredCapabilities capability = null;
			String browser = null;

			// BrowserStack
			switch (Log.AUTOMATION_TEST_BROWSER) {
			case BrowserName.CHROME:
				capability = DesiredCapabilities.chrome();
				browser = "Chrome";
				break;
			case BrowserName.IE:
				capability = DesiredCapabilities.internetExplorer();
				browser = "IE";
				break;
			case BrowserName.OPERA:
				capability = DesiredCapabilities.operaBlink();
				browser = "Opera";
				break;
			case BrowserName.SAFARI:
				capability = DesiredCapabilities.safari();
				browser = "Safari";
				break;
			default:
				capability = DesiredCapabilities.firefox();
				browser = "Firefox";
			}

			capability.setCapability("browser", browser);
			capability.setCapability("browser_version", Log.AUTOMATION_BROWSERSTACK_BROWSER_VERSION);
			capability.setCapability("os", Log.AUTOMATION_BROWSERSTACK_OS);
			capability.setCapability("os_version", Log.AUTOMATION_BROWSERSTACK_OS_VERSION);
			capability.setCapability("resolution", Log.AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION);

			// required BrowserStack settings for executing local or internal
			// Visiant applications
			capability.setCapability("acceptSslCerts", "true");

			// enable BrowserStack local testing
			capability.setCapability("browserstack.local", "true");

			Log.logBanner("Automation Running in AUTOMATION_BROWSERSTACK_ENABLE Environment");

			Log.logScriptInfo("BrowserStack Browser: " + browser);
			Log.logScriptInfo("BrowserStack Browser Version: " + Log.AUTOMATION_BROWSERSTACK_BROWSER_VERSION);
			Log.logScriptInfo("BrowserStack Operating System: " + Log.AUTOMATION_BROWSERSTACK_OS);
			Log.logScriptInfo("BrowserStack OS Version: " + Log.AUTOMATION_BROWSERSTACK_OS_VERSION);
			Log.logScriptInfo("BrowserStack Client Resolution: " + Log.AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION);
			Log.logScriptInfo("==============================================================================",
					Log.LOGTYPE_SIMPLE);

			// load BrowserStackLocal with options
			browserstacklocal = new Local();
			final HashMap<String, String> options = new HashMap<>();
			options.put("key", Log.AUTOMATION_BROWSERSTACK_LICENSE_KEY);
			// options.put("only", "localhost,80,0");
			options.put("f", Log.AUTOMATION_TEST_DATA_PATH);
			// options.put("proxyHost", "127.0.0.1");
			// options.put("proxyPort", "8118");
			// options.put("xyz", "qwerty");

			try {
				browserstacklocal.start(options);
			} catch (final Exception e1) {
				Log.errorHandler("Error starting BrowserStackLocal", e1);
			}

			try {
				driver = new RemoteWebDriver(
						new URL(String.format("https://%s:%s@hub-cloud.browserstack.com/wd/hub",
								Log.AUTOMATION_BROWSERSTACK_LICENSE_USER, Log.AUTOMATION_BROWSERSTACK_LICENSE_KEY)),
						capability);
			} catch (final Exception e) {
				Log.errorHandler("Error Loading BrowserStack RemoteWebDriver", e);
			}
		}

		/**
		 * Gets proxy with Bandwidth Limit.
		 *
		 * @return {@link Proxy} object
		 */
		private static Proxy getProxy() {
			if (Log.AUTOMATION_BANDWIDTH_LIMIT) {

				BrowserMobProxy proxy = new BrowserMobProxyServer();

				proxy.setReadBandwidthLimit(Log.AUTOMATION_BANDWIDTH_LIMIT_READ);
				proxy.setWriteBandwidthLimit(Log.AUTOMATION_BANDWIDTH_LIMIT_WRITE);
				proxy.start();

				return ClientUtil.createSeleniumProxy(proxy);
			}

			return null;
		}
	} // end of inner seleniumHelper class

} // end of main SeleniumCore class
