
package core.extjswidgets.utils;

import org.openqa.selenium.JavascriptExecutor;

import core.utilities.Log;
import core.utilities.Platform;
import core.utilities.SeleniumCore;
import core.utilities.Timers;
import core.utilities.Timers.Condition;
import core.utilities.exceptions.AutomationException;

/**
 * Class contains basic methods for processing Extjs Widget and page objects.
 */
public class ExtJSHelper {
	/** Cannot instantiate. */
	protected ExtJSHelper() {
	}

	/**
	 * Waits until AJAX calls are completed using ExtJS API.
	 *
	 * @param waitTime
	 *            time to wait in seconds
	 * @return true if ajax is loaded completely
	 */
	public static boolean waitForAjax(final int waitTime) {
		return Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
				return !((Boolean) ((JavascriptExecutor) SeleniumCore.driver)
						.executeScript("return Ext.Ajax.isLoading();"));
			}
		}, waitTime * 1000);
	}

	/**
	 * Waits until AJAX calls are completed using ExtJS API.
	 *
	 * @return true if ajax is loaded completely
	 */
	public static boolean waitForAjax() {
		return waitForAjax(Log.AUTOMATION_WAIT_VALUE_60);
	}

	/**
	 * Waits until AJAX calls are completed using ExtJS API.
	 *
	 * @param waitTime
	 *            time to wait in seconds
	 * @throws AutomationException
	 *             is ajax is still loading
	 */
	public static void ensureWaitedForAjax(final int waitTime) {
		final boolean isReady = waitForAjax(waitTime);

		if (!isReady) {
			throw new AutomationException("Error performing: Wait for completion of AJAX load");
		}
	}

	/**
	 * Waits until AJAX calls are completed using ExtJS API.
	 *
	 */
	public static void ensureWaitedForAjax() {
		ensureWaitedForAjax(Log.AUTOMATION_WAIT_VALUE_60);
	}

	/**
	 * Verifies presence of Ext Js API on the page.
	 *
	 * @return true if Ext Js exists
	 */
	public static boolean isExtJsExists() {
		String version3 = null;
		String version5 = null;

		try {
			version3 = (String) SeleniumCore.jsExecutor.executeScript("return Ext.version");
			version5 = (String) SeleniumCore.jsExecutor.executeScript("return Ext.versions.ext.version");
		} catch (final Exception e) {
		}

		if (version3 == null && version5 == null) {
			return false;
		}

		return true;
	}

	/**
	 * Verifies presence of Ext Js API on the page.
	 *
	 * @throws AutomationException
	 *             if Ext Js is not present on the page
	 */
	public static void ensureIsExtJsExists() {
		if (!isExtJsExists()) {
			throw new AutomationException("Ext Js not found on the page or Ext Js version is not supported");
		}
	}

	/**
	 * Waits and verifies presence of Ext Js API on the page.
	 *
	 * @param waitTime
	 *            time to wait in seconds
	 * @return true if Ext Js exists
	 */
	public static boolean waitForExtJs(final int waitTime) {
		return Timers.waitFor(new Condition() {
			@Override
			public boolean check() {
				Platform.sleep(Log.AUTOMATION_WAIT_VALUE_1);
				return isExtJsExists();
			}
		}, waitTime * 1000);
	}

	/**
	 * Waits and verifies presence of Ext Js API on the page.
	 *
	 * @return true if Ext Js exists
	 */
	public static boolean waitForExtJs() {
		return waitForExtJs(Log.AUTOMATION_WAIT_VALUE_60);
	}

	/**
	 * Ensures presence of Ext Js API on the page.
	 *
	 * @param waitTime
	 *            time to wait in seconds
	 */
	public static void ensureWaitedForExtJs(final int waitTime) {
		final boolean isReady = waitForExtJs(waitTime);

		if (!isReady) {
			throw new AutomationException("Error performing: Wait for completion of Ext Js load");
		}
	}

	/**
	 * Ensures presence of Ext Js API on the page.
	 */
	public static void ensureWaitedForExtJs() {
		ensureWaitedForExtJs(Log.AUTOMATION_WAIT_VALUE_60);
	}

	/**
	 * Waits and verifies whether page with Ext Js is loaded.
	 *
	 * @param waitTime
	 *            time to wait in seconds
	 * @return true if page with Ext Js is loaded
	 */
	public static boolean waitForPage(final int waitTime) {
		return waitForExtJs(waitTime) && waitForAjax(waitTime);
	}

	/**
	 * Waits and verifies whether page with Ext Js is loaded.
	 *
	 * @return true if page with Ext Js is loaded
	 */
	public static boolean waitForPage() {
		return waitForExtJs() && waitForAjax();
	}

	/**
	 * Waits and verifies whether page with Ext Js is loaded.
	 *
	 * @param waitTime
	 *            time to wait in seconds
	 */
	public static void ensureWaitedForPage(final int waitTime) {
		final boolean isReady = waitForExtJs(waitTime) && waitForAjax(waitTime);

		if (!isReady) {
			throw new AutomationException("Error performing: Wait for loading of page with Ext Js");
		}
	}

	/**
	 * Waits and verifies whether page with Ext Js is loaded.
	 */
	public static void ensureWaitedForPage() {
		ensureWaitedForPage(Log.AUTOMATION_WAIT_VALUE_60);
	}

	/**
	 * Gets version of Ext Js.
	 *
	 * @return version of Ext Js
	 * @throws AutomationException
	 *             if Ext Js is not present on the page or major version
	 *             mismatches
	 */
	public static String getExtJsVersion() {
		String version3 = null;
		String version5 = null;

		try {
			version3 = (String) SeleniumCore.jsExecutor.executeScript("return Ext.version");
			version5 = (String) SeleniumCore.jsExecutor.executeScript("return Ext.versions.ext.version");
		} catch (final Exception e) {
		}

		String extJsVersion = "";
		if ((extJsVersion = version3) == null && (extJsVersion = version5) == null) {
			throw new AutomationException("Ext Js not found or Ext Js major version mismatches");
		}

		return extJsVersion;
	}

	/**
	 * Gets major version of Ext Js.
	 *
	 * @return major version of Ext Js
	 */
	public static int getExtJsMajorVersion() {
		return Integer.parseInt(getExtJsVersion().split("\\.")[0]);
	}
}
