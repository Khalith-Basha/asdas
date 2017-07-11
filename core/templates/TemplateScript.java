package core.templates;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import core.utilities.Browser;
import core.utilities.FileIO;
import core.utilities.Log;
import core.utilities.Platform;
import core.utilities.scripting.BaseTestScript;

/**
 * Standard Visiant Selenium Test Script Template file.
 * <p>
 * Place your description of what the test script tests here.
 */
public class TemplateScript extends BaseTestScript{
	/**
	 * Initializes the automation for testing.
	 *
	 * @throws Exception
	 *             if an error occurs during the setup portion of the script
	 */
	@BeforeClass
	public static void setUp() throws Exception {
		// script name is auto-generated via this code so DO NOT modify this
		// item
		Log.AUTOMATION_SCRIPT_NAME = Thread.currentThread().getStackTrace()[1].getClassName();

		// TODO: change script description
		Log.AUTOMATION_SCRIPT_DESCRIPTION = "Add test description here"; // update
																			// test
																			// description
		// TODO: change script author
		Log.AUTOMATION_SCRIPT_AUTHOR = "Tony Venditti"; // update script author

		// script test area uses the project path info and is auto-generated via
		// this code so DO NOT modify this item
		Log.AUTOMATION_SCRIPT_TEST_AREA = FileIO.getParentPath(Platform.getCurrentProjectPath()
				+ Log.AUTOMATION_SCRIPT_NAME.replace(".", Platform.getFileSeparator()));
		// Reads in Automation.properties values, Logs script header
		// information, initializes script metrics and starts Selenium server
		Log.initialize();

		// TODO: change setup description
		Log.logBanner("Setup Description goes here");

		// TODO: replace if needed with your setup actions
		Browser.start(); // start browser
		Browser.loadURL("about:blank", Log.AUTOMATION_WAIT_VALUE_5);
	}

	/**
	 * Short test description goes here.
	 * <p>
	 * A Test case description goes here. A test case in this Java-based
	 * Selenium framework is denoted by the use of the Log.startTestCase("Test
	 * case description") method. This script template contains a sample Test
	 * section for Visiant Selenium scripts. In order to work in the
	 * JUnit-Eclipse IDE the test method below must contain the word "test" at
	 * the beginning of the method name: for example, "testMyFirstScript()"
	 */
	@Test
	public void testTemplateScriptExample() {
		try { // good practice to wrap your test case in a try/catch block

			// TODO: place your test data here
			String sLink = "About";

			// TODO: enter some test case code here. Example test code below
			// load test web page
			Browser.loadURL("http://www.google.com", Log.AUTOMATION_WAIT_VALUE_5);

			// TODO: verify test results here. Example verification code below.
			if (Browser.linkExists(sLink)) {
				Log.logScriptInfo("Verified Link: " + "\"" + sLink + "\"" + " is present");
			} else {
				Log.errorHandler("Link: " + "\"" + sLink + "\"" + " does NOT exist");
			}
		} catch (Exception e) { // catch all error handler
			Log.errorHandler("Error occurred in TemplateScript.", e);
		}
	}

}
