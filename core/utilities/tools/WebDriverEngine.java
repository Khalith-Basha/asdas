
package core.utilities.tools;

import core.utilities.Browser;
import core.utilities.Images;
import core.utilities.Log;
import core.utilities.Platform;

/**
 * This class contains common methods responding to WebDriver engine behavior.
 */
public class WebDriverEngine implements Engine {
	@Override
	public void setEngine() {
		Platform.setEngine(Platform.WEBDRIVER);
		Log.logScriptInfo("Automation Engine: WEBDRIVER version: " + Platform.getSeleniumVersion());
	}

	/**
	 * Closes and stops browser.
	 */
	@Override
	public void stop() {
		Browser.close();
		Browser.stop();
	}

	/**
	 * Takes browser/desktop capture.
	 */
	@Override
	public boolean takeCapture(String fileName, boolean error) {
		if (Log.AUTOMATION_BROWSER_CAPTURE) {
			return Images.doBrowserCapture(fileName, error);
		}

		return Images.doDesktopCapture(fileName, error);
	}

}
