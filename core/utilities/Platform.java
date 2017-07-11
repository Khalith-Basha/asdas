package core.utilities;

import java.awt.Robot;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.openqa.selenium.internal.BuildInfo;

/**
 * The Platform class contains generic Platform specific functions to handle
 * different Browsers, different Operating Systems, etc.
 */
public class Platform {

	// **************Automation framework release info**************

	/** Automation Framework release version */
	private static final String RELEASE_VERSION = "20170103.82";

	/**
	 * returns Automation Framework release version
	 *
	 * @return version
	 */
	public static String getVersion() {
		return RELEASE_VERSION;
	}

	// **************Selenium & WebDriver related code**************

	/** Global string for WebDriver engine */
	public static final String WEBDRIVER = "WEBDRIVER";

	/** returns boolean depending upon if engine is set to WebDriver */
	private static boolean isWebDriver;

	// TaskManager constants
	/** Windows TaskManager tasklist constant */
	private static final String TASKLIST = "tasklist";

	/** Windows TaskManager taskkill command constant */
	private static final String KILL = "taskkill /F /IM ";

	/**
	 * Returns boolean depending upon if engine is set to WebDriver
	 *
	 * @return true if WebDriver is the TestEngine
	 */
	public static boolean isWebDriver() {
		return isWebDriver;
	}

	/**
	 * Sets the boolean values of the automation test engines
	 *
	 * @param engineName
	 *            the automation tool you are using i.e. WEBDRIVER (Selenium
	 *            version 2), SILKMOBILE
	 */
	public static void setEngine(final String engineName) {
		if (engineName.equals(WEBDRIVER)) {
			isWebDriver = true;
		}
	}

	/**
	 * Gets the name of the automation engine in use
	 *
	 * @return engine name
	 */
	public static String getEngineName() {
		String myEngine = "";
		if (isWebDriver) {
			myEngine = "WebDriver";
		}

		Log.logScriptInfo("Platform.getEngineName() = '" + myEngine + "'");
		return myEngine;
	}

	/**
	 * Gets the version of Selenium Webdriver in use
	 *
	 * @return Selenium Webdriver version
	 */
	public static String getSeleniumVersion() {
		return new BuildInfo().getReleaseLabel();
	}

	// **************Operating system variables and methods**************

	/** Operating System Name */
	public static final String OS_NAME = System.getProperty("os.name");

	/** returns true if Linux OS */
	private static boolean isLinux = OS_NAME.equals("Linux");

	/** Operating System version */
	public static final String OS_VERSION = !isLinux ? System.getProperty("os.version") : getLinuxOSName();

	/** returns true if Windows OS */
	private static boolean isWindows = OS_NAME.startsWith("Windows");

	/** returns true if Mac OS */
	private static boolean isMac = System.getProperty("mrj.version") != null && OS_NAME.contains("OS X");

	/** returns Linux version info */
	private static String linuxVersionInfo = File.separator + "etc" + File.separator + "issue";

	/** return Windows OS version */
	private static final String WIN_XP = "Version 5.1";
	private static final String WIN_7 = "Version 6.1";
	private static final String VISTA = "Version 6.0";

	/**
	 * return true if Mac OS
	 *
	 * @return true if Mac OS
	 */
	public static boolean isMac() {
		return isMac;
	}

	/**
	 * return true if Windows OS
	 *
	 * @return true if Windows OS
	 */
	public static boolean isWindows() {
		return isWindows;
	}

	/**
	 * return true if Linux OS
	 *
	 * @return true if Linux OS
	 */
	public static boolean isLinux() {
		return isLinux;
	}

	/**
	 * Returns the Operating system version as a string
	 *
	 * @return the Operating system version
	 */
	public static String getOSDisplayVersion() {
		String osVersion = "Unknown";

		if (isMac || isLinux()) {
			osVersion = OS_VERSION;
		}

		if (isWinXP()) {
			osVersion = "XP (" + OS_VERSION + ")";
		}
		if (isVista()) {
			osVersion = "Vista (" + OS_VERSION + ")";
		}
		if (isWin7()) {
			osVersion = "7 (" + OS_VERSION + ")";
		}

		return osVersion;
	}

	/**
	 * Gets Linux distributions name and version.
	 *
	 * @return Linux distributions name and version
	 */
	private static String getLinuxOSName() {
		String linuxOSVersion = "";

		if (new File("/etc/lsb-release").exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader("/etc/lsb-release"))) {
				String distribDescription = null;
				String line;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("DISTRIB_DESCRIPTION")) {
						distribDescription = line.replace("DISTRIB_DESCRIPTION=", "").replace("\"", "");
					}
				}
				if (distribDescription != null) {
					linuxOSVersion = distribDescription;
				}

			} catch (final IOException e) {
				return null;
			}
		} else if (new File("/etc/system-release").exists()) {
			linuxOSVersion = getPlatformNameFromFile("/etc/system-release");
		} else if (new File(getFileEndingWith("-release")).exists()) {
			linuxOSVersion = getPlatformNameFromFile(getFileEndingWith("-release"));
		} else if (new File(getFileEndingWith("_version")).exists()) {
			linuxOSVersion = getPlatformNameFromFile(getFileEndingWith("_version"));
		} else if (new File("/etc/issue").exists()) {
			linuxOSVersion = getPlatformNameFromFile("/etc/issue");
		} else if (new File("/proc/version").exists()) {
			linuxOSVersion = getPlatformNameFromFile(new File("/proc/version").getAbsolutePath());
		}

		return linuxOSVersion;
	}

	/**
	 * Gets file path ending with given string.
	 *
	 * @param fileEndingWith
	 * @return path to file
	 */
	private static String getFileEndingWith(final String fileEndingWith) {
		final File[] fileList = new File("/etc/").listFiles(new FilenameFilter() {
			@Override
			public boolean accept(final File dir, final String filename) {
				return filename.endsWith(fileEndingWith);
			}
		});
		if (fileList.length > 0) {
			return fileList[0].getAbsolutePath();
		}

		return "";
	}

	/**
	 * Gets Linux platform name.
	 *
	 * @param filename
	 *            path to file
	 * @return Linux platform name
	 */
	private static String getPlatformNameFromFile(final String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename));) {
			String line;
			String lineToReturn = null;
			int lineNb = 0;
			while ((line = br.readLine()) != null) {
				if (lineNb++ == 0) {
					lineToReturn = line;
				}
				if (line.startsWith("PRETTY_NAME")) {
					return line.substring(13, line.length() - 1);
				}
			}
			return lineToReturn;
		} catch (final IOException e) {
			return null;
		}
	}

	/**
	 * Returns the Operating system name and version
	 *
	 * @return the Operating system name and version as a string
	 */
	public static String getOSNameAndVersion() {
		return OS_NAME + " " + OS_VERSION; // getOSDisplayVersion();
	}

	/**
	 * Determines if the OS Vendor is Red Hat by examining the contents of
	 * /proc/version
	 *
	 * @return true id RED HAT, false otherwise
	 */
	public static boolean isRHEL() {
		if (isLinux) {
			final String[] versionInfo = FileIO.getFileContentsAsListByUTF8(linuxVersionInfo);

			for (final String element : versionInfo) {
				if (element.toUpperCase().contains("RED HAT")) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Determines if the OS Vendor is SLED by examining the contents of
	 * /proc/version
	 *
	 * @return true id SLED, false otherwise
	 */
	public static boolean isSLED() {
		if (isLinux) {
			final String[] versionInfo = FileIO.getFileContentsAsListByUTF8(linuxVersionInfo);

			for (final String element : versionInfo) {
				if (element.toUpperCase().contains("SUSE LINUX")) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Determines if the OS Vendor is Ubuntu by examining the contents of
	 * /proc/version
	 *
	 * @return true id Ubuntu, false otherwise
	 */
	public static boolean isUBUNTU() {
		if (isLinux) {
			final String[] versionInfo = FileIO.getFileContentsAsListByUTF8(linuxVersionInfo);

			for (final String element : versionInfo) {
				if (element.toUpperCase().contains("UBUNTU")) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * determine if the os is WindowXP by examining os version
	 *
	 * @return true if Windows XP
	 */
	public static boolean isWinXP() {
		return isWindows() && OS_VERSION.contains(WIN_XP);
	}

	/**
	 * determine if the OS is Vista by examining OS version
	 *
	 * @return true if OS is Vista
	 */
	public static boolean isVista() {
		return isWindows() && OS_VERSION.contains(VISTA);
	}

	/**
	 * determine if the OS is Win7 by examining OS version
	 *
	 * @return true if OS is Windows 7
	 */
	public static boolean isWin7() {
		return isWindows() && OS_VERSION.contains(WIN_7);
	}

	/**
	 * Returns the appropriate file separator for a specific Operating system
	 * i.e. "\" for windows OS, ":" for Mac OS, etc
	 *
	 * @return the appropriate file separator delimiter for the OS
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}

	/**
	 * Returns the current users home path i.e. "c:\\documents and
	 * settings\\myname" for windows XP, "c:\\users\myname\\" for Windows 7, etc
	 *
	 * @return the path for the current users home folder
	 */
	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	/**
	 * Returns the current Java project path
	 *
	 * @return current java project path as a string
	 */
	public static String getCurrentProjectPath() {
		String myProjectPath = new File(".").getAbsolutePath().replaceAll(".$", "");

		// myProjectPath = myProjectPath.replace("\\", "/");

		if (myProjectPath.endsWith(FileIO.getFileSeparator())) {
			return myProjectPath;
			// myProjectPath = myProjectPath.substring(0, myProjectPath.length()
			// - 1);
		} else {
			return myProjectPath + FileIO.getFileSeparator();
		}

	}

	/**
	 * Returns the full file path corresponding to it's location
	 *
	 * @param resourcePath
	 *            string
	 * @return the resource path
	 */
	public static String getResourceFullPath(final String resourcePath) {
		return new File(ClassLoader.getSystemResource(resourcePath).getPath()).getPath();
	}

	// General methods & variables

	/** Global string for new line separator */
	public static String gsNewline = System.getProperty("line.separator");

	/**
	 * Pauses execution for a specified number of seconds
	 *
	 * @param numberOfSecondsToSleep
	 *            number of seconds to pause
	 */
	public static void sleep(final int numberOfSecondsToSleep) {
		try {
			Thread.sleep(numberOfSecondsToSleep * 1000);
		} catch (final Exception e) {
		}
	}

	// **************Clipboard & SendKey methods***************************

	/**
	 * Sets text to clipboard
	 *
	 * @param s
	 *            String to send to clipboard
	 */
	public static void setClipboard(final String s) {
		// get clipboard
		final Clipboard clip = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();

		// set clipboard contents
		final StringSelection ss = new java.awt.datatransfer.StringSelection(s);
		clip.setContents(ss, ss);
	}

	/**
	 * Gets text from clipboard and returns text as String
	 *
	 * @return String text from clipboard
	 */
	public static String getClipboard() {
		String tmp = "";

		// get content from clipboard into output String
		final Clipboard clip = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard();
		final Transferable t = clip.getContents(null);

		try {
			tmp = (String) t.getTransferData(DataFlavor.stringFlavor);
		} catch (final Exception e) {
			Log.errorHandler("" + e.toString(), e);
		}

		return tmp;
	}

	/**
	 * This method uses the Java Robot class to send actual alpha-numeric
	 * keyboard keystrokes to the active application on the desktop. For
	 * example, sendKeys("My Test 123"); //types in My Test 123 into the active
	 * desktop application.
	 *
	 * @param value
	 *            keys to be typed
	 */
	public static void sendKeys(final String value) {
		try {
			final Robot robot = new Robot();

			// break string into char array
			final char[] c = value.toCharArray();

			for (final char element : c) { // cycle through input chars
				for (int i = 1; i < 128; i++) { // cycle through ASCII chars
					if (element == (char) i) { // compare input char to ascii
												// char if match use keypress
												// method
						// System.out.println("x: " + x + "i:" + i);
						// System.out.println(c[x]+": " + (char)i);
						// Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1); //pause
						// 1 second before each keystroke
						if (i < 91 && i > 64) { // captial letters
							robot.keyPress(KeyEvent.VK_SHIFT);
							robot.keyPress(i);
							robot.keyRelease(KeyEvent.VK_SHIFT);
							robot.keyRelease(i);
						} else if (i < 123 && i > 96) { // lower case letters
							i = i - 32;
							robot.keyPress(i);
							robot.keyRelease(i);
						} else if (i < 58 && i > 47) { // numeric characters
							robot.keyPress(i);
							robot.keyRelease(i);
						} else { // will enter non-alpha-numeric keys but will
									// flag invalid characters such as
									// )(}{+*^@><? as errors
							try {
								robot.keyPress(i);
								robot.keyRelease(i);
							} catch (final Exception e1) {
								Log.errorHandler("Invalid Character: " + element
										+ "Use sendKeys(int iValue) method to send non-alpha numeric keys", e1);
							}
						}
						break;
					}
				}
			}
			Log.logScriptInfo(String.format("Send Keys: \"%s\" to active field in desktop.", value));
		} catch (final Exception e) {
			Log.errorHandler("Error sending keystrokes", e);
		}

	}

	/**
	 * This method uses the Java Robot class to send a single keyboard press of
	 * any single key to the active application on the desktop. For example,
	 * sendKey(KeyEvent.VK_HOME) //presses HOME key
	 *
	 * @param value
	 *            int keycode of keys to be pressed
	 */
	public static void sendKeyPress(final int value) {
		try {
			final Robot robot = new Robot();
			// KeyEvent.VK_ALT = 18
			// KeyEvent.VK_CONTROL = 17
			// KeyEvent.VK_END = 35
			// KeyEvent.VK_HOME = 36
			// To find more KeyEvent constant values uncomment the line below
			// and hover over VK_HOME constant
			// then click the "Constant Field Values" link in the context
			// javadoc help window that appears
			// int i = KeyEvent.VK_HOME;

			robot.keyPress(value);
			Log.logScriptInfo(String.format("Press \"%s\" Key", KeyEvent.getKeyText(value)));

		} catch (final Exception e) {
			Log.errorHandler("Error sending keypress", e);
		}
	}

	/**
	 * This method uses the Java Robot class to release a single keyboard press
	 * of any single key to the active application on the desktop. For example,
	 * sendKey(KeyEvent.VK_HOME) //presses HOME key
	 *
	 * @param value
	 *            int keycode of keys to be pressed i.e.
	 */
	public static void sendKeyRelease(final int value) {
		try {
			final Robot robot = new Robot();
			// KeyEvent.VK_ALT = 18
			// KeyEvent.VK_CONTROL = 17
			// KeyEvent.VK_END = 35
			// KeyEvent.VK_HOME = 36
			// To find more KeyEvent constant values uncomment the line below
			// and hover over VK_HOME constant
			// then click the "Constant Field Values" link in the context
			// javadoc help window that appears
			// int i = KeyEvent.VK_HOME;

			robot.keyRelease(value);
			Log.logScriptInfo(String.format("Release \"%s\" Key", KeyEvent.getKeyText(value)));

		} catch (final Exception e) {
			Log.errorHandler("Error sending key release", e);
		}
	}

	/**
	 * This method uses the Java Robot class to press the Ctrl-C (clipboard Copy
	 * command) to the active systems clipboard.
	 */
	public static void sendKeyClipboardCopy() {
		sendKeyPress(KeyEvent.VK_CONTROL);
		sendKeyPress(KeyEvent.VK_C);
		sleep(Log.AUTOMATION_WAIT_VALUE_2);
		sendKeyRelease(KeyEvent.VK_C);
		sendKeyRelease(KeyEvent.VK_CONTROL);
		sleep(Log.AUTOMATION_WAIT_VALUE_2);
		Log.logScriptInfo("Executed Clipboard Copy via key stroke CTRL-C");
	}

	/**
	 * This method uses the Java Robot class to press the Ctrl-V (clipboard
	 * Paste command) to the active systems clipboard.
	 */
	public static void sendKeyClipboardPaste() {
		sendKeyPress(KeyEvent.VK_CONTROL);
		sendKeyPress(KeyEvent.VK_V);
		sleep(Log.AUTOMATION_WAIT_VALUE_2);
		sendKeyRelease(KeyEvent.VK_CONTROL);
		sendKeyRelease(KeyEvent.VK_V);
		sleep(Log.AUTOMATION_WAIT_VALUE_2);
	}

	/**
	 * This method uses the Java Robot class to press the Ctrl-A (Select All
	 * command)
	 */
	public static void sendKeySelectAll() {
		sendKeyPress(KeyEvent.VK_CONTROL);
		sendKeyPress(KeyEvent.VK_A);

		sendKeyRelease(KeyEvent.VK_CONTROL);
		sendKeyRelease(KeyEvent.VK_A);
	}

	/**
	 * This method uses AutoIt to maximize a specified window
	 *
	 * @param windowCaption
	 *            String caption of window to maximize
	 */
	public static void maximizeWin(final String windowCaption) {
		try {
			// get app file and path from resources folder
			final String exec = Log.AUTOMATION_BASE_PATH + "MaximizeWindow.exe";
			if (!FileIO.dirExists(exec)) {
				FileIO.copyResource("core/tools/autoit/MaximizeWindow.exe", exec);
			}
			Runtime.getRuntime().exec(String.format("%s %s", exec, windowCaption));
			Log.logScriptInfo("Maximize Window: " + windowCaption);
		} catch (final Exception e) {
			Log.errorHandler("Error maximizing window: " + windowCaption, e);
		}

	}

	/**
	 * This method uses AutoIt to minimize a specified window
	 *
	 * @param windowCaption
	 *            String caption of window to minimize
	 */
	public static void minimizeWin(final String windowCaption) {
		try {
			// get exe file and path from resources folder
			final String exec = Log.AUTOMATION_BASE_PATH + "MinimizeWindow.exe";
			if (!FileIO.dirExists(exec)) {
				FileIO.copyResource("core/tools/autoit/MinimizeWindow.exe", exec);
			}
			Runtime.getRuntime().exec(String.format("%s %s", exec, windowCaption));
			Log.logScriptInfo("Minimize Window: " + windowCaption);
		} catch (final Exception e) {
			Log.errorHandler("Error minimizing window: " + windowCaption, e);
		}
	}

	/**
	 * Checks to see if Windows TaskManager task is running
	 *
	 * @param serviceName
	 *            the service to check if running
	 * @param showProcesses
	 *            true to log out a list of running services
	 * @return true if specified process is running
	 * @throws Exception
	 *             raises an exception if an error occurs in the process
	 */
	public static boolean isProcessRunning(final String serviceName, final boolean showProcesses) throws Exception {

		final Process p = Runtime.getRuntime().exec(TASKLIST);
		final BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line;
		while ((line = reader.readLine()) != null) {

			if (showProcesses) {
				Log.logScriptInfo(line);
			}

			if (line.contains(serviceName)) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Ends or kills the specified process in the Windows TaskManager
	 *
	 * @param serviceName
	 *            the service to end or kill
	 * @throws Exception
	 *             raises an exception if an error occurs killing the process
	 */
	public static void killProcess(final String serviceName) throws Exception {

		Runtime.getRuntime().exec(KILL + serviceName);

	}
}
