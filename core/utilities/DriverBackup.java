package core.utilities;

import org.openqa.selenium.WebDriver;

/**
 * Class contains methods to backup and restore driver. It is needed for cases
 * when several sessions must be used in the same browser. Example: <code>
 * <br>String windowId1 = Browser.getAllWindowIds()[0];
 * <br><b>DriverBackup current_driver = DriverBackup.backup();</b>
 * <br>
 * <br>Browser.loadURL("http://google.com", Log.AUTOMATION_WAIT_VALUE_1);
 * <br>performActions1();
 * <br>
 * <br><b>DriverBackup new_driver = DriverBackup.backup();</b>
 * <br>
 * <br>Browser.loadURL("http://yahoo.com", Log.AUTOMATION_WAIT_VALUE_1);
 * <br>performActions2();
 * <br>
 * <br><b>current_driver.restore();</b>
 * <br><b>Browser.selectWindow(windowId1);</b>
 * <br>
 * <br>restoreActions1();
 * </code> Note: this driver should be closed separately.
 *
 * @author Tony Venditti
 */
public class DriverBackup {
	private WebDriver driver;

	public DriverBackup() {
		driver = SeleniumCore.driver;
	}

	/**
	 * Creates backup of original driver.
	 * 
	 * @return DriverBackup object
	 */
	public static DriverBackup backup() {
		return new DriverBackup();
	}

	/**
	 * Restores original driver.
	 */
	public void restore() {
		SeleniumCore.driver = driver;
	}
}
