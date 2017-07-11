
package core.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;

import org.testng.Assert;

import core.utilities.exceptions.AutomationException;
import core.utilities.exceptions.SetupException;
import core.utilities.report.AutomationReport;
import core.utilities.tools.Engine;

/**
 * The Log class Performs script initialization, termination and logging
 * operations among many other functions.
 * <p>
 * The log output is written simultaneously to a text file, the system console
 * and to an HTML file. The HTML result log displays results hierarchically with
 * passes in green and fails in red for easy identification. In order to
 * implement the automation logging simply import the Log class in your test
 * script and add the Log.initialize() and Log.terminate() methods to your
 * scripts setup and cleanup sections. Also, all test cases in your test section
 * must be prefaced by the method Log.startTestcase(). All error handling should
 * be done by the method Log.errorHandler().
 * <p>
 * The Automation Log class has an associated properties file called
 * automation.properties. Place the automation.properties file in your client
 * system's home directory. The home directory is usually the c:\Users\yourname\
 * folder. You can find out what your home directory is by executing the
 * following java command: <BR>
 * System.out.printn(Platform.getUserHome()); <BR>
 * Edit the properties in the automation.properties file to represent your local
 * system and automation settings. If you do not have this file, the Log class
 * will use default variables.
 */
public class Log {

	// Global Automation & System variables

	/** Automation test engine. i.e WebDriver, SilkMobile or Silk4J */
	private static Engine engine = null;

	/** Automation base path alternate for Mac OS X clean home directories... */
	public static String AUTOMATION_ALT_BASE_PATH = Platform.getUserHome() + Platform.getFileSeparator() + "Visiant"
			+ Platform.getFileSeparator() + "automation" + Platform.getFileSeparator();

	/** Automation properties file name */
	public static String AUTOMATION_PROPERTIES = "automation.properties";

	/** Automation properties file name */
	public static String AUTOMATION_SUITE_STATS = "suitestats.properties";

	/** Automation base path */
	public static String AUTOMATION_BASE_PATH = Platform.getUserHome() + Platform.getFileSeparator();

	/** Automation properties file name */
	public static String AUTOMATION_DEFAULT_AUTOMATION_PROPERTIES_FILE = AUTOMATION_BASE_PATH + AUTOMATION_PROPERTIES;

	/** Automation suite statistic property file name */
	public static String AUTOMATION_DEFAULT_SUITE_STAT_FILE = AUTOMATION_BASE_PATH + AUTOMATION_SUITE_STATS;

	/** Global string for automation directory */
	public static String AUTOMATION_TEST_PROJECT_PATH = Platform.getCurrentProjectPath();

	public static String AUTOMATION_PROPERTIES_PATH = AUTOMATION_TEST_PROJECT_PATH + AUTOMATION_PROPERTIES;

	/** Global string for automation result directory */
	public static String AUTOMATION_TEST_RESULTS_PATH = Platform.getCurrentProjectPath() + "results"
			+ Platform.getFileSeparator();

	/** Global string for automation result image suffix */
	public static String AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX = ".jpg";

	/** Global string for automation Test Data Spreadsheet suffix */
	public static String AUTOMATION_SPREADSHEET_SUFFIX = ".xls";

	/** Global string for old Excel .xls file extension */
	public static String AUTOMATION_EXCEL_XLS_EXT = ".xls";

	/** Global string for Google Excel .xlsx file extension */
	public static String AUTOMATION_EXCEL_XLSX_EXT = ".xlsx";

	/** Global string for XML Spreadsheet 2003 .xml file extension */
	public static String AUTOMATION_EXCEL_XML_EXT = ".xml";

	/** Global string for Google Excel .xlsx file extension */
	public static String AUTOMATION_CSV_EXT = ".csv";

	/** Global string for automation support documents path */
	public static String AUTOMATION_TEST_DATA_PATH = Platform.isLinux() ? AUTOMATION_TEST_PROJECT_PATH
			: "c:" + Platform.getFileSeparator();

	/** Global strings for Browser Profile file */
	public static String AUTOMATION_BROWSER_PROFILE;

	/** Global string for additional Chrome command line arguments */
	public static String AUTOMATION_CHROME_COMMANDLINE;

	/** Global strings for Browser Path */
	public static String AUTOMATION_BROWSER_PATH;

	/** Global string for remote server name or IP address */
	public static String AUTOMATION_SCM_REMOTE_SERVER_NAME;

	/** Global string for remote server user name */
	public static String AUTOMATION_SCM_REMOTE_SERVER_USERNAME;

	/** Global string for remote server password */
	public static String AUTOMATION_SCM_REMOTE_SERVER_PASSWORD;

	/** Global string for remote server port */
	public static String AUTOMATION_SCM_REMOTE_SERVER_PORT;

	// Global script FTP server info

	/** Global string for FTP server name or IP address */
	public static String AUTOMATION_FTP_SERVER_NAME;

	/** Global string for FTP server user name */
	public static String AUTOMATION_FTP_SERVER_USERNAME;

	/** Global string for FTP server password */
	public static String AUTOMATION_FTP_SERVER_PASSWORD;

	/** Global string for FTP server port */
	public static String AUTOMATION_FTP_SERVER_PORTAL;

	// Global script Database server info

	/** Global string for database system */
	public static String AUTOMATION_DATABASE_SERVER_SYSTEM;

	/** Global string for database server name or IP address */
	public static String AUTOMATION_DATABASE_SERVER_NAME;

	/** Global string for database name or instance */
	public static String AUTOMATION_DATABASE_SERVER_INSTANCE_NAME;

	/** Global string for database server user name */
	public static String AUTOMATION_DATABASE_SERVER_USERNAME;

	/** Global string for database server password */
	public static String AUTOMATION_DATABASE_SERVER_PASSWORD;

	/** Global string for database server port */
	public static String AUTOMATION_DATABASE_SERVER_PORT;

	/** Global string for test browser */
	public static String AUTOMATION_TEST_BROWSER; // = "Internet Explorer 6.0";

	// Global time-out variables

	/** Global Time-out variable - 1 Second */
	public static int AUTOMATION_WAIT_VALUE_1; // = 1;

	/** Global Time-out variable - 2 Seconds */
	public static int AUTOMATION_WAIT_VALUE_2; // = 2;

	/** Global Time-out variable - 5 Seconds */
	public static int AUTOMATION_WAIT_VALUE_5; // = 5;

	/** Global Time-out variable - 10 Seconds */
	public static int AUTOMATION_WAIT_VALUE_10; // = 10;

	/** Global Time-out variable - 15 Seconds */
	public static int AUTOMATION_WAIT_VALUE_15; // = 15;

	/** Global Time-out variable - 30 Seconds */
	public static int AUTOMATION_WAIT_VALUE_30; // = 30;

	/** Global Time-out variable - 60 Seconds */
	public static int AUTOMATION_WAIT_VALUE_60; // = 60;

	// Log Type formats

	/**
	 * Global default Pass-Fail, action description, time, elapsed time output
	 * Log type
	 */
	public static final int LOGTYPE_TIME_PASS_FAIL = 1;

	/** Global Script output Log type */
	public static final int LOGTYPE_SCRIPT_OUTPUT = 2;

	/** Global Simple output Log type */
	public static final int LOGTYPE_SIMPLE = 3;

	/** Global No output Log type */
	public static final int LOGTYPE_NONE = 4;

	/** Global Error output Log type */
	public static final int LOGTYPE_ERROR_OUTPUT = 5;

	/** Global Error output Log type */
	public static final int LOGTYPE_DEBUG_INFO = 6;

	/** Global log type - used to add html information in result html log */
	public static final int LOGTYPE_HTML = 7;

	/** Global Fail info output log type */
	public static final int LOGTYPE_FAIL = 8;

	/** Global console output log type */
	public static final int LOGTYPE_CONSOLE = 0;

	/** Global command trace back info output log type */
	public static final int LOGTYPE_APPEND_INVOCATION = 9;

	// global Default Log Type

	public static String AUTOMATION_REPORT_TYPE = "TXT,XML,JUNIT,HTML";

	/** Global default Log type */
	public static int AUTOMATION_LOG_TYPE = LOGTYPE_TIME_PASS_FAIL;

	// global Error Handling Options

	/**
	 * Global boolean to capture browser window. If variable is false then
	 * entire screen (primary desktop) will be captured.
	 */
	public static boolean AUTOMATION_BROWSER_CAPTURE = false;

	/** Global default Image capture output option */
	public static boolean AUTOMATION_IMAGE_CAPTURE = false;

	/** Global default Error Handling Stack Trace output Option */
	public static boolean AUTOMATION_STACK_TRACE = true;

	/** Global default Error Handling Image capture output option */
	public static boolean AUTOMATION_ERROR_IMAGE_CAPTURE = true;

	/** Global default Close browsers upon test script completion option */
	public static boolean AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION = true;

	/** Global default For automatically open html report in default browser */
	public static boolean AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION = false;

	// Global testcase logging information

	/** Global int for tracking number of test cases executed */
	public static int AUTOMATION_SCRIPT_COUNTER;

	/** Global boolean checking for failed script */
	public static boolean AUTOMATION_SCRIPT_FAILED = false;

	/** Global int for tracking number of failed scripts */
	public static int AUTOMATION_SCRIPT_FAILED_COUNTER;

	/** Global int for tracking number of passed scripts */
	public static int AUTOMATION_SCRIPT_PASSED_COUNTER;

	/** Global int for tracking number of test cases executed */
	public static int AUTOMATION_TESTCASE_COUNTER;

	/**
	 * Global int for tracking number of failures flagged in the entire script
	 */
	public static int AUTOMATION_ERROR_COUNTER;

	/** Global boolean checking for failed test case */
	public static boolean AUTOMATION_TESTCASE_FAILED = false;

	/** Global int for tracking number of failed test cases */
	public static int AUTOMATION_TESTCASE_FAILED_COUNTER;

	/** Global int for tracking number of test actions */
	public static int AUTOMATION_TEST_ACTION_COUNTER;

	/** Global string for Test case header */
	public static String AUTOMATION_TESTCASE_HEADER = "* Testcase - Start - ";

	/** Global string for Script header */
	public static String AUTOMATION_SCRIPT_HEADER = "* Script Name:           ";

	/** Global string for Script header */
	public static String AUTOMATION_SCRIPT_FOOTER = "* Elapsed Time:";

	/** Global string for current date and time */
	public static String AUTOMATION_CURRENT_DATE_TIME;

	/** Global string for Total Script header */
	public static String AUTOMATION_TOTAL_SCRIPTS_EXECUTED = "* Total Scripts Executed:";

	/** Global string for Total Scripts (suite) Elapsed time */
	public static String AUTOMATION_TOTAL_ELAPSED_TIME = "* Total Elapsed Time:";

	/** Global string for Total Script header */
	public static String AUTOMATION_TESTCASES_EXECUTED = "* Testcases Executed:";

	/** Global string for Total Script header */
	public static String AUTOMATION_TESTCASE_FOOTER = "* Testcases Executed:";

	// Global script header information

	/** Global string for script author */
	public static String AUTOMATION_SCRIPT_AUTHOR;

	/** Global string for script description */
	public static String AUTOMATION_SCRIPT_DESCRIPTION;

	/** Global string for script test area */
	public static String AUTOMATION_SCRIPT_TEST_AREA;

	/** Global string for script name */
	public static String AUTOMATION_SCRIPT_NAME;

	/** Global long for script start time */
	public static long AUTOMATION_SCRIPT_START_TIME = 0;

	/** Global string for line separator */
	public static String gsNewline = System.getProperty("line.separator");

	/** Global string for automation result file */
	public static String AUTOMATION_RESULT_FILE;

	/** Global boolean for tracking if suite or script is running */
	public static boolean isSuite = false;

	/** Global string for tracking if suite or script is running */
	public static String AUTOMATION_RESULT_VIEWER_APP;

	/** Global string for image marker in log output. */
	public static String AUTOMATION_IMAGE_MARKER = "Image Saved to: ";

	/** Global string for FAIL item in pass/fail portion of log output. */
	public static String AUTOMATION_FAIL_MARKER = "FAIL - ";

	/** Global string for ERROR item in pass/fail portion of log output. */
	public static String AUTOMATION_ERROR_MARKER = "ERROR - ";

	/** Global string for PASS item in pass/fail portion of log output. */
	public static String AUTOMATION_PASS_MARKER = "PASS - ";

	/** Global string for passed test case footer */
	public static String AUTOMATION_TESTCASE_PASS_FOOTER = "Testcase - " + AUTOMATION_PASS_MARKER;

	/** Global string for failed test case footer */
	public static String AUTOMATION_TESTCASE_FAIL_FOOTER = "Testcase - " + AUTOMATION_FAIL_MARKER;

	/** Global string for Debug info in log output. */
	public static String AUTOMATION_DEBUG_MARKER = "DEBUG - ";

	/** Global string for Warning info in log output. */
	public static String AUTOMATION_WARNING_MARKER = "WARNING - ";

	/** Global string for Abort signature in log output. */
	public static String AUTOMATION_ABORT_MARKER = "**** ABORTING ****";

	/** Global int for Test Level. */
	public static int AUTOMATION_TEST_LEVEL;

	/** Global boolean to display automation properties */
	public static boolean AUTOMATION_DISPLAY_AUTOMATION_PROPERTIES = false;

	/** Delete all files in results folder before start script */
	public static boolean AUTOMATION_CLEAR_RESULTS = false;

	protected static int iCurrentScriptTestCaseNumber;
	protected static int numScriptsRun = 0;

	public static String AUTOMATION_SCRIPT_UID = DateTime.genDateBasedRandVal();

	/** Global boolean to display widgets locators */
	public static boolean AUTOMATION_LOCATOR_PRINT = false;

	/** generate JUnit Fails upon error */
	public static boolean AUTOMATION_JUNIT_FAIL_ENABLE = true;
	
	/** optionally copies results files to alternate result location */
	public static String AUTOMATION_COPY_RESULTS_TO;

	/**
	 * Global boolean to update test case data in runtime before starting of
	 * each test case. Number of test cases must be static and known before
	 * execution. 
	 */
	public static boolean AUTOMATION_RUNTIME_UPDATE_DATA = false;

	// ------------------------------------------------------------------------------
	// BrowserStack settings
	// ------------------------------------------------------------------------------
	/** Global Setting to enable BrowserStack */
	public static boolean AUTOMATION_BROWSERSTACK_ENABLE = false;

	/** BrowserStack license key */
	public static String AUTOMATION_BROWSERSTACK_LICENSE_KEY = "";

	/** BrowserStack license user */
	public static String AUTOMATION_BROWSERSTACK_LICENSE_USER = "";

	/** BrowserStack Operating System */
	public static String AUTOMATION_BROWSERSTACK_OS = "Windows";

	/** BrowserStack Operating System */
	public static String AUTOMATION_BROWSERSTACK_OS_VERSION = "7";

	/** Screen resolution for BrowserStack */
	public static String AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION = "1024x768";

	/** BrowserStack Browser Version */
	public static String AUTOMATION_BROWSERSTACK_BROWSER_VERSION = "46.0";

	/**
	 * global Java System properties list retrieved from Java System properties
	 * Used for getting global automation settings from Silk Central Parameters
	 */
	public static Properties JAVA_SYSTEM_PROPERTIES = System.getProperties();

	/**
	 * Report object for collect and write results of testing in html and xml
	 * report.
	 */
	private static AutomationReport report = null;

	/** Global Setting to enable BandWidth limit */
	public static boolean AUTOMATION_BANDWIDTH_LIMIT = false;
	/**
	 * The maximum bandwidth to consume when reading server responses, in bytes
	 * per second
	 */
	public static int AUTOMATION_BANDWIDTH_LIMIT_READ = 10000;
	/**
	 * The maximum bandwidth to consume when sending requests to servers, in
	 * bytes per second
	 */
	public static int AUTOMATION_BANDWIDTH_LIMIT_WRITE = 10000;

	/** JVM options for automation */
	public static final String EXECUTION_TOOL = "executiontool";
	public static final String SCTM_TEST_RESULTS_DIR = "#sctm_test_results_dir";
	public static final String AUTOMATION_AUTO_PATH = "AUTOMATION_TEST_PROJECT_PATH";

	/**properties files*/
	public static String AUTOMATION_PROPERTIES_FILE = "";
	public static String APPLICATION_PROPERTIES_FILE = "";
	
	/**Suite result file name*/
	public static String AUTOMATION_SUITE_RESULT_FILENAME = "";
	
	
	/**
	 * Constructor for Log class logging without script header variables
	 */
	public Log() {
	}

	/**
	 * Constructor for Log class Logging with script header variables
	 * <p>
	 *
	 * @param author
	 *            script author
	 * @param scriptDescription
	 *            script description
	 * @param testArea
	 *            script test functional area
	 */
	public Log(final String author, final String scriptDescription, final String testArea) {
		// assign script variables
		AUTOMATION_SCRIPT_AUTHOR = author;
		AUTOMATION_SCRIPT_DESCRIPTION = scriptDescription;
		AUTOMATION_SCRIPT_TEST_AREA = testArea;
	}

	public static void initialize() {

		// initialize automation global variables
		try {
			autoSetup(true);
			Log.startTestCase("configuration and test setup");
		} catch (final Exception e) {
			errorHandler("error occurred at script initialization", e);
		}

	}

	/**
	 * Executes script cleanup and test metric gathering functions upon script
	 * termination
	 */
	public static void terminate() {
		try {
			if (report.isTestCaseStarted()) {
				report.finishTestCase();
			}

			// if option is true then close test browsers otherwise leave
			// browsers up
			if (engine != null && AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION) {
				engine.stop();
				engine = null;
			}

			report.finishScript();

			// Get test metrics and report results
			autoCleanup(true);
		} catch (final Exception e) {
			errorHandler("error occurred at script termination", e);
		}

		if (AUTOMATION_BROWSERSTACK_ENABLE) {
			try {
				SeleniumCore.browserstacklocal.stop();
			} catch (final Exception e) {
				errorHandler("Error quitting browserstacklocal", e);
			}
		}
		
		
		

	}

	/**
	 * Returns script name
	 *
	 * @return script name
	 */
	public static String getScriptName() {
		return AUTOMATION_SCRIPT_NAME;
	}

	/**
	 * Sets the script name
	 *
	 * @param name
	 *            script name
	 */
	public static void setScriptName(final String name) {
		AUTOMATION_SCRIPT_NAME = name;
	}

	/**
	 * Sets up, initializes automation environment and logs script header info
	 * <P>
	 *
	 * @param showHeaderInfo
	 *            true displays script header information, false does not
	 *            display script header information
	 */
	public static void autoSetup(final boolean showHeaderInfo) {
		// Setup and initialize automation environment
		
		// get script name
		if (getScriptName() == null) {
			try {
				setScriptName(AUTOMATION_SCRIPT_NAME);
			} catch (final Exception e) // in case the calling script doesn't
										// have a matching script definition in
										// the resources directory
										// for
			// some reason, just bail
			{
				// noop
			}
		}

		// set script counter
		AUTOMATION_SCRIPT_COUNTER = 1;

		// load global automation variables
		loadAutomationPropertySettings();

		// clear result folder
		if (!isSuite && AUTOMATION_CLEAR_RESULTS) {
			Platform.sleep(AUTOMATION_WAIT_VALUE_1);
			FileIO.clearDirectory(Paths.get(AUTOMATION_TEST_RESULTS_PATH));
			Platform.sleep(AUTOMATION_WAIT_VALUE_1);
			FileIO.dirExists(Paths.get(AUTOMATION_TEST_RESULTS_PATH));
		}

		// create unique result file based on script name and date
		if (AUTOMATION_SCRIPT_NAME != null) {
			AUTOMATION_RESULT_FILE = String.format("%s%s_%s", AUTOMATION_TEST_RESULTS_PATH, AUTOMATION_SCRIPT_NAME,
					AUTOMATION_SCRIPT_UID);
		} else {
			AUTOMATION_RESULT_FILE = String.format("%sNONAME_%s", AUTOMATION_TEST_RESULTS_PATH, AUTOMATION_SCRIPT_UID);
		}

		// assign current date and time to global variable
		AUTOMATION_CURRENT_DATE_TIME = DateFormat.getDateInstance().format(new Date()) + " "
				+ DateFormat.getTimeInstance().format(new Date());

		// set the current browser
		SeleniumCore.setCurrentBrowser(AUTOMATION_TEST_BROWSER);

		// Write Script Header info to result log
		if (showHeaderInfo) {
			if (report == null) {
				report = new AutomationReport(AUTOMATION_RESULT_FILE);
			}

			report.startScript(AUTOMATION_SCRIPT_NAME);

			generateHeaderInfo("Script");
		}

		// Start script clock
		setStartTime();

		// Clear AUTOMATION_SCRIPT_PASSED_COUNTER
		AUTOMATION_SCRIPT_PASSED_COUNTER = 0;

		// Clear AUTOMATION_SCRIPT_FAILED_COUNTER
		AUTOMATION_SCRIPT_FAILED_COUNTER = 0;

		// Clear testcase counter
		AUTOMATION_TESTCASE_COUNTER = 0;

		// Clear error counter
		AUTOMATION_ERROR_COUNTER = 0;

		// Clear failed testcase counter
		AUTOMATION_TESTCASE_FAILED_COUNTER = 0;

		// Set testcase failed boolean to false
		AUTOMATION_TESTCASE_FAILED = false;

		// Set script failed boolean to false
		AUTOMATION_SCRIPT_FAILED = false;

		// Clear test action counter
		AUTOMATION_TEST_ACTION_COUNTER = 0;
	}

	/**
	 * Generates header info.
	 *
	 * @param testEssence
	 *            - 'Script' or 'Suite' string.
	 */
	private static void generateHeaderInfo(final String testEssence) {
		final String myPlatformDisplayName = SeleniumCore.getBrowser().getDisplayName();
	
		if (!testEssence.equals("Script")) {
			AUTOMATION_SCRIPT_NAME = " " + AUTOMATION_SCRIPT_NAME;
			AUTOMATION_SCRIPT_AUTHOR = " " + AUTOMATION_SCRIPT_AUTHOR;
			AUTOMATION_SCRIPT_DESCRIPTION = " " + AUTOMATION_SCRIPT_DESCRIPTION;
			AUTOMATION_SCRIPT_TEST_AREA = " " + AUTOMATION_SCRIPT_TEST_AREA;
		}

		logScriptInfo("******************************************************************************", LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* %s Name:                     %s", testEssence, AUTOMATION_SCRIPT_NAME),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* %s Author:                   %s", testEssence, AUTOMATION_SCRIPT_AUTHOR),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* %s Description:              %s", testEssence, AUTOMATION_SCRIPT_DESCRIPTION),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* %s Test Area:                %s", testEssence, AUTOMATION_SCRIPT_TEST_AREA),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Result Folder:                   %s", AUTOMATION_TEST_RESULTS_PATH),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Test Data Folder:                %s", AUTOMATION_TEST_DATA_PATH),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Automation Properties File:      %s", AUTOMATION_PROPERTIES_FILE),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Test Client:                     %s", getLocalClientName()), LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Test Client OS:                  %s", Platform.getOSNameAndVersion()),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Core Automation:                 %s", Platform.getVersion()), LOGTYPE_SIMPLE);
		if (!myPlatformDisplayName.isEmpty()) {
			logScriptInfo(String.format("* Test Platform:	           %s", myPlatformDisplayName), LOGTYPE_SIMPLE);
		}
		logScriptInfo(String.format("* Java Version:                    %s", System.getProperty("java.version")),
				LOGTYPE_SIMPLE);
		logScriptInfo(String.format("* Start Date and Time:             %s", AUTOMATION_CURRENT_DATE_TIME),
				LOGTYPE_SIMPLE);
		logScriptInfo("******************************************************************************", LOGTYPE_SIMPLE);
	}

	/**
	 * Logs script information to multiple output sources. Must explicitly state
	 * which log format type to use. Writes info to log in any of a number of
	 * specified formats
	 * <p>
	 *
	 * @param log
	 *            Log message
	 * @param type
	 *            type of log information (i.e. 1=PASS/FAIL, 2=SCRIPT OUTPUT,
	 *            etc.)
	 */
	public static void logScriptInfo(final String log, final int type) {
		// Overloaded to explicitly state which log format type to use. Writes
		// info to log in any of a number of specified formats
		final DateFormat tmFormat = DateFormat.getTimeInstance();
		String s = "";

		switch (type) {
		case LOGTYPE_APPEND_INVOCATION: // Verbose - append invocation
										// information
			s = String.format("%s - %s - %s%s", tmFormat.format(new Date()),
					DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME), AUTOMATION_PASS_MARKER, log); // format
			AUTOMATION_TEST_ACTION_COUNTER++; // add one to test action counter
			if (report != null) {
				report.logStep(s);
			}
			break;
		case LOGTYPE_TIME_PASS_FAIL: // Verbose - PASS-FAIL format output
			s = String.format("%s - %s - %s%s", tmFormat.format(new Date()),
					DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME), AUTOMATION_PASS_MARKER, log); // format
			AUTOMATION_TEST_ACTION_COUNTER++; // add one to test action counter
			if (report != null) {
				report.logStep(s);
			}
			break;
		case LOGTYPE_SCRIPT_OUTPUT: // Verbose - Manual script format output
			s = "[ ] - " + log; // format
			AUTOMATION_TEST_ACTION_COUNTER++; // add one to test action counter
			if (report != null) {
				report.logStep(s);
			}
			break;
		case LOGTYPE_SIMPLE: // Verbose - Log only what is entered
			s = log;
			if (report != null) {
				report.logStep(s);
			}
			break;
		case LOGTYPE_NONE: // Do NOT log actions info to log file
			break;
		case LOGTYPE_ERROR_OUTPUT:
			if (!log.contains(AUTOMATION_FAIL_MARKER)) {
				s = String.format("%s - %s - %s%s", tmFormat.format(new Date()),
						DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME), AUTOMATION_ERROR_MARKER, log); // format
			} else {
				s = log;
			}

			System.err.println(s);
			if (report != null) {
				report.logStep(s);
			}
			break;
		case LOGTYPE_DEBUG_INFO:
			s = String.format("%s - %s - %s%s", tmFormat.format(new Date()),
					DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME), AUTOMATION_DEBUG_MARKER, log); // format
			if (report != null) {
				report.logStep(s);
			}
			break;
		// Real time log implementation items
		case LOGTYPE_HTML:
			s = log;
			if (report != null) {
				report.logStep(s);
			}
			break;
		case LOGTYPE_FAIL:
			s = String.format("%s - %s - %s%s", tmFormat.format(new Date()),
					DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME), AUTOMATION_FAIL_MARKER, log); // format
			AUTOMATION_TEST_ACTION_COUNTER++; // add one to test action counter
			if (report != null) {
				report.logStep(s);
				report.handleError();
			}
			break;
		case LOGTYPE_CONSOLE:
			s = log;
			break;
		default:
			s = log;
			if (report != null) {
				report.logStep(s);
			}
			break;
		}

		// abort script, set fail vars
		if (s.endsWith(AUTOMATION_ABORT_MARKER)) {
			if (AUTOMATION_TESTCASE_COUNTER > 0) {
				AUTOMATION_TESTCASE_FAILED = true;
			}
			if (AUTOMATION_SCRIPT_COUNTER > 0) {
				AUTOMATION_SCRIPT_FAILED = true;
			}
		}

		// write results to text file (AUTOMATION_RESULT_FILE) and to console
		if (type != LOGTYPE_NONE && type != LOGTYPE_ERROR_OUTPUT) {
			if (type != LOGTYPE_HTML) {
				System.out.println(s); // console
			}

			if (type == LOGTYPE_APPEND_INVOCATION) {
				s = s + getInvocationInfo();
			}
		}
	}

	/**
	 * logs a snapshot of the desktop to the HTML result log and saves the image
	 * as a file in the designated result directory
	 * <p>
	 *
	 * @param description
	 *            description for generated snapshot.
	 * @param error
	 *            - whether to generate snapshot for error or not.
	 * @return image filename
	 */
	public static String logScreenCapture(final String description, final boolean error) {
		String fileName = null;

		try {
			// To avoid previous image file form being over written, use
			// DateTime.getFormattedDateTime(new Date().getTime(),
			// "MMddHHmmssSSS") not genDateBasedRandVal().
			fileName = AUTOMATION_TEST_RESULTS_PATH + AUTOMATION_SCRIPT_NAME + "_"
					+ DateTime.getFormattedDateTime(new Date().getTime(), "MMddHHmmssSSS")
					+ AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX;

			// add description in log for captured image
			if (!error) {
				if (description != null) {
					logScriptInfo("Image Description: " + description);
				}
			}

			if (engine != null) {
				if (!Images.captureScreen(fileName, error)) {
					fileName = null;

					throw new AutomationException("Error in capturing of desktop/browser/device image");
				}
			}

			logScriptInfo(AUTOMATION_IMAGE_MARKER + fileName, error ? LOGTYPE_ERROR_OUTPUT : LOGTYPE_TIME_PASS_FAIL);

		} catch (final Exception e) {
			// DO NOT use errorHandler method here or will fall in infinite
			// invoking, because this method is invoked in errorHandler method.
			System.err.println("Exception Log.logScreenCapture(TestObject, String, boolean): " + e.getMessage());
		}

		return fileName;
	}

	/**
	 * log screen snapshot with description. used for normal purpose.
	 *
	 * @param description
	 *            of screen snapshot.
	 */
	public static void logScreenCapture(final String description) {
		logScreenCapture(description, false);
	}

	/**
	 * log screen snapshot without description
	 */
	public static void logScreenCapture() {
		logScreenCapture(null, false);
	}

	/**
	 * Logs script information to multiple output sources. Overloaded to
	 * simplify to single parameter. Forces use of global Log Type
	 * AUTOMATION_LOG_TYPE parameter.
	 *
	 * @param log
	 *            log message
	 */
	public static void logScriptInfo(final String log) {
		// Overloaded to simplify to single parameter. Forces use of global Log
		// Type AUTOMATION_LOG_TYPE parameter for Log formatting.
		logScriptInfo(log, AUTOMATION_LOG_TYPE);
	}

	/**
	 * Logs debug script information to multiple output sources. Forces use of
	 * global Log Type AUTOMATION_LOG_TYPE parameter set to #6 (equals debug
	 * info).
	 *
	 * @param log
	 *            log message
	 */
	public static void logDebugInfo(final String log) {
		// Forces use of Debug Log Type AUTOMATION_LOG_TYPE parameter for
		// Logging of debug info.
		if (AUTOMATION_LOG_TYPE == 6) {
			logScriptInfo(log, 6);
		}
	}

	/**
	 * Sets script clock start time - by default this is started automatically
	 * at script startup when the autoSetup() function is used. The function
	 * sets a global variable called AUTOMATION_SCRIPT_START_TIME which is then
	 * used as an argument to the getElapsedTime(AUTOMATION_SCRIPT_START_TIME)
	 * function which is called in the logScriptInfo() function
	 */
	public static void setStartTime() {
		AUTOMATION_SCRIPT_START_TIME = System.currentTimeMillis();
	}

	/**
	 * Logs script testcase information and other metrics info
	 * <p>
	 *
	 * @param showResultFooter
	 *            true to show result metrics in the footer section of the log
	 *            results
	 */
	public static void autoCleanup(final boolean showResultFooter) {
		
		
		
		// Check if script contained any failures
		if (AUTOMATION_TESTCASE_FAILED) {
			AUTOMATION_TESTCASE_FAILED_COUNTER++;

		}

		if (!AUTOMATION_SCRIPT_FAILED) {
			AUTOMATION_SCRIPT_PASSED_COUNTER = 1;
		} else {
			AUTOMATION_SCRIPT_FAILED_COUNTER = 1;
			AUTOMATION_SCRIPT_PASSED_COUNTER = 0;
		}

		int testCasePassedCounter;

		// calculate number of passed test cases
		testCasePassedCounter = AUTOMATION_TESTCASE_COUNTER - AUTOMATION_TESTCASE_FAILED_COUNTER;

		if (AUTOMATION_TESTCASE_COUNTER < iCurrentScriptTestCaseNumber) {
			numScriptsRun += iCurrentScriptTestCaseNumber - AUTOMATION_TESTCASE_COUNTER;
		}

		if (showResultFooter) {
			// calculate percentages
			final double prctPassed = (int) ((double) testCasePassedCounter / (double) AUTOMATION_TESTCASE_COUNTER
					* 10000) / 100.0;
			final double prctFailed = (int) ((double) AUTOMATION_TESTCASE_FAILED_COUNTER
					/ (double) AUTOMATION_TESTCASE_COUNTER * 10000) / 100.0;

			// store elapsed time
			final String elapsedTime = DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME);
			final DateFormat dtFormat = DateFormat.getDateInstance();
			final DateFormat tmFormat = DateFormat.getTimeInstance();

			// Write Script Testcase Information to result log
			logScriptInfo("******************************************************************************",
					LOGTYPE_CONSOLE);
			logScriptInfo("* Testcases Executed:              " + AUTOMATION_TESTCASE_COUNTER, LOGTYPE_CONSOLE);
			logScriptInfo("* Testcases Passed:                " + testCasePassedCounter, LOGTYPE_CONSOLE);
			logScriptInfo("* Testcases Failed:                " + AUTOMATION_TESTCASE_FAILED_COUNTER, LOGTYPE_CONSOLE);
			logScriptInfo("* Percent Testcases Passed:        " + prctPassed + "%", LOGTYPE_CONSOLE);
			logScriptInfo("* Percent Testcases Failed:        " + prctFailed + "%", LOGTYPE_CONSOLE);
			logScriptInfo("* Number of Test Actions Executed: " + AUTOMATION_TEST_ACTION_COUNTER, LOGTYPE_CONSOLE);
			logScriptInfo("* Number of Errors found:          " + AUTOMATION_ERROR_COUNTER, LOGTYPE_CONSOLE);
			logScriptInfo("* End Date and Time:               " + dtFormat.format(new Date()) + " "
					+ tmFormat.format(new Date()), LOGTYPE_CONSOLE);
			logScriptInfo("* Elapsed Time:                    " + elapsedTime, LOGTYPE_CONSOLE);
			logScriptInfo("******************************************************************************",
					LOGTYPE_CONSOLE);

			// open temporary file to tally test statistics for suite results
			int inScriptCounter = 0;
			int inScriptPassedCounter = 0;
			int inScriptFailedCounter = 0;

			int inTestCaseCounter = 0;
			int inTestCasePassedCounter = 0;
			int inTestCaseFailCounter = 0;
			int inTestActionCounter = 0;
			int inErrorCounter = 0;
			long inElapsedTime = 0;

			try (FileInputStream in = new FileInputStream(AUTOMATION_DEFAULT_SUITE_STAT_FILE)) {
				final Properties settings = new Properties();
				settings.load(in);

				inScriptCounter = Integer.parseInt(settings.getProperty("ScriptCounter"));
				inScriptPassedCounter = Integer.parseInt(settings.getProperty("ScriptPassedCounter"));
				inScriptFailedCounter = Integer.parseInt(settings.getProperty("ScriptFailedCounter"));
				inTestCaseCounter = Integer.parseInt(settings.getProperty("TestCaseCounter"));
				inTestCasePassedCounter = Integer.parseInt(settings.getProperty("TestCasePassedCounter"));
				inTestCaseFailCounter = Integer.parseInt(settings.getProperty("TestCaseFailCounter"));
				inTestActionCounter = Integer.parseInt(settings.getProperty("TestActionCounter"));
				inErrorCounter = Integer.parseInt(settings.getProperty("ErrorCounter"));
				inElapsedTime = Long.parseLong(settings.getProperty("ElapsedTime"));
			} catch (final Exception e) {
				// commenting this out as the next code will create the file...
				// logScriptInfo("Error loading suite statistic INI variables in
				// file: " + AUTOMATION_DEFAULT_SUITE_STAT_FILE, 5);
			}

			// tally and update suite statistics file
			try (FileOutputStream out = new FileOutputStream(AUTOMATION_DEFAULT_SUITE_STAT_FILE)) {

				final Properties settings = new Properties();

				settings.put("ScriptCounter", String.valueOf(inScriptCounter + 1));
				settings.put("ScriptPassedCounter",
						String.valueOf(inScriptPassedCounter + AUTOMATION_SCRIPT_PASSED_COUNTER));
				settings.put("ScriptFailedCounter",
						String.valueOf(inScriptFailedCounter + AUTOMATION_SCRIPT_FAILED_COUNTER));
				settings.put("ScriptPrctPassed", String
						.valueOf((int) (((double) inScriptPassedCounter + (double) AUTOMATION_SCRIPT_PASSED_COUNTER)
								/ ((double) inScriptCounter + (double) AUTOMATION_SCRIPT_COUNTER) * 10000) / 100.0));
				settings.put("ScriptPrctFailed", String
						.valueOf((int) (((double) inScriptFailedCounter + (double) AUTOMATION_SCRIPT_FAILED_COUNTER)
								/ ((double) inScriptCounter + (double) AUTOMATION_SCRIPT_COUNTER) * 10000) / 100.0));

				settings.put("TestCaseCounter", String.valueOf(inTestCaseCounter + AUTOMATION_TESTCASE_COUNTER));
				settings.put("TestCasePassedCounter", String.valueOf(inTestCasePassedCounter + testCasePassedCounter));
				settings.put("TestCaseFailCounter",
						String.valueOf(inTestCaseFailCounter + AUTOMATION_TESTCASE_FAILED_COUNTER));
				settings.put("PrctPassed",
						String.valueOf((int) (((double) inTestCasePassedCounter + (double) testCasePassedCounter)
								/ ((double) inTestCaseCounter + (double) AUTOMATION_TESTCASE_COUNTER) * 10000)
								/ 100.0));
				settings.put("PrctFailed",
						String.valueOf(
								(int) (((double) inTestCaseFailCounter + (double) AUTOMATION_TESTCASE_FAILED_COUNTER)
										/ ((double) inTestCaseCounter + (double) AUTOMATION_TESTCASE_COUNTER) * 10000)
										/ 100.0));
				settings.put("TestActionCounter", String.valueOf(inTestActionCounter + AUTOMATION_TEST_ACTION_COUNTER));
				settings.put("ErrorCounter", String.valueOf(inErrorCounter + AUTOMATION_ERROR_COUNTER));
				settings.put("ElapsedTime",
						String.valueOf(inElapsedTime + DateTime.getElapsedTimeLong(AUTOMATION_SCRIPT_START_TIME)));

				// Close out properties file
				settings.store(out, "");
			} catch (final IOException e) {
				logScriptInfo("Error saving suite statistic property variables in file: "
						+ AUTOMATION_DEFAULT_SUITE_STAT_FILE, 5);
			}

			// display log results in specified Viewer when test script
			// completes
			// if script is part of suite execution do not display viewer
			if (!isSuite) {
				report.openHTMLReport();
			}
		}
	}

	/**
	 * Logs and tracks testcase information
	 * <p>
	 * <b>Note:</b> For AutomationRunner approach the method
	 * {@link #startTestCase(String)} is recommended instead of this one.
	 * <p>
	 *
	 * @param description
	 *            Description of testcase
	 */
	public static void startTestCase(final String description) {
		// calculate any previous pass-fail metrics
		autoCleanup(false);

		// Add one to testcase counter
		AUTOMATION_TESTCASE_COUNTER++;

		report.startTestCase(description);

		final DateFormat dtFormat = DateFormat.getDateInstance();
		final DateFormat tmFormat = DateFormat.getTimeInstance();

		// Display testcase description information
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);
		logScriptInfo(String.format("%s%s - Date: %s %s", AUTOMATION_TESTCASE_HEADER, description,
				dtFormat.format(new Date()), tmFormat.format(new Date())), LOGTYPE_SIMPLE);
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);

		// reset testcase failed to false
		AUTOMATION_TESTCASE_FAILED = false;
		// System.out.println(AUTOMATION_TESTCASE_FAILED);
	}
	
	
		
	

	
	/**
	 * Logs test case completion info.
	 */
	public static void finishTestCase() {
		final DateFormat dtformat = DateFormat.getDateInstance();
		final DateFormat tmformat = DateFormat.getTimeInstance();

		// Display testcase completion information
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);
		logScriptInfo("* Testcase - Finish - Date: " + dtformat.format(new Date()) + "  " + tmformat.format(new Date()),
				LOGTYPE_SIMPLE);
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);

		report.finishTestCase();
	}

	/**
	 * Logs and tracks test group information
	 * <p>
	 *
	 * @param sGroupName
	 *            name of test group
	 */
	public static void startGroup(final String sGroupName) {
		report.startGroup(sGroupName);

		final DateFormat dtformat = DateFormat.getDateInstance();
		final DateFormat tmformat = DateFormat.getTimeInstance();

		// Display testcase description information
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);
		logScriptInfo("* Group - Start - " + sGroupName + " - Date: " + dtformat.format(new Date()) + "  "
				+ tmformat.format(new Date()), LOGTYPE_SIMPLE);
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);
	}

	/**
	 * Logs test group completion info.
	 */
	public static void finishGroup() {
		final DateFormat dtformat = DateFormat.getDateInstance();
		final DateFormat tmformat = DateFormat.getTimeInstance();

		// Display testcase completion information
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);
		logScriptInfo("* Group - Finish - Date: " + dtformat.format(new Date()) + "  " + tmformat.format(new Date()),
				LOGTYPE_SIMPLE);
		logScriptInfo("==============================================================================", LOGTYPE_SIMPLE);

		report.finishGroup();
	}

	/**
	 * Dumps non-exception based error information out to console, text result
	 * and HTML result logs together with screen capture if screen capture in
	 * automation log is set to true
	 * <p>
	 *
	 * @param log
	 *            The error description text to write out to the log file
	 */
	public static void errorHandler(final String log) {
		errorHandler(log, null);
	}
	
	
	

	/**
	 * errorHandler function Dumps exception based error information out to
	 * console, text result and HTML result logs
	 * <p>
	 *
	 * @param log
	 *            The text you want to write out to the log file
	 * @param e
	 *            Exception error info
	 */
	public static void errorHandler(final String log, final Exception e) {
		final DateFormat tmFormat = DateFormat.getTimeInstance();

		// spacer
		logScriptInfo("", LOGTYPE_SIMPLE);

		// Log error to output result logs
		String stackTraceOutput = String.format("%s - %s - %s%s%s", tmFormat.format(new Date()),
				DateTime.getElapsedTime(AUTOMATION_SCRIPT_START_TIME), AUTOMATION_FAIL_MARKER, log,
				System.lineSeparator());

		AUTOMATION_TEST_ACTION_COUNTER++; // add one to test action counter

		// Add 1 to error counter and set testcase and script failed booleans to
		// true
		AUTOMATION_ERROR_COUNTER++;
		if (AUTOMATION_TESTCASE_COUNTER > 0) {
			AUTOMATION_TESTCASE_FAILED = true;

		}
		if (AUTOMATION_SCRIPT_COUNTER > 0) {
			AUTOMATION_SCRIPT_FAILED = true;
		}

		// Get stack and error info
		if (e != null && e.getMessage() != null && !log.contains(e.getMessage())) {
			stackTraceOutput += e.getMessage() + System.lineSeparator();
		}

		if (AUTOMATION_STACK_TRACE) {
			stackTraceOutput += String.format("%s%1$sStack Trace:%1$s%s%1$s", System.lineSeparator(), getStackTrace(e));
		}

		logScriptInfo(stackTraceOutput, LOGTYPE_ERROR_OUTPUT);

		if (AUTOMATION_ERROR_IMAGE_CAPTURE) {
			logScreenCapture(log, true);
		}

		if (report != null) {
			report.handleError();
			report.handleJUnitReportError(e, log);
		}

		// Log JUnit failure
//		if (AUTOMATION_JUNIT_FAIL_ENABLE) {
//			if (log == null) {
//				throw new JUnitAssertionError();
//			}
//
//			throw new JUnitAssertionError(log);
		
		Assert.fail(log);
//		}

		// spacer
		logScriptInfo("", LOGTYPE_SIMPLE);
	}

	/**
	 * Gets stack trace information and returns it as a string
	 * <p>
	 *
	 * @param e
	 *            Exception
	 * @return String containing stack trace
	 */
	public static String getStackTrace(Exception e) {
		if (e == null) {
			e = new Exception();
		}

		StringWriter sw = null;
		PrintWriter pw = null;
		sw = new StringWriter();
		pw = new PrintWriter(sw);
		e.printStackTrace(pw);

		return sw.toString();
	}

	/**
	 * Initialize suite statistics file
	 */
	public static void initializeSuiteStats() {

		// set boolean isSuite to true. This means a suite is running (not a
		// single script)
		isSuite = true;

		// get suite file name
		AUTOMATION_SCRIPT_NAME = getScriptName();
		try {
			setScriptName(AUTOMATION_SCRIPT_NAME);
		} catch (final Exception e) // in case the calling script doesn't have a
									// matching script definition in the
									// resources directory for
									// some
		// reason, just bail
		{
			AUTOMATION_SCRIPT_NAME = "suite";
			setScriptName(AUTOMATION_SCRIPT_NAME);
		}

		AUTOMATION_CURRENT_DATE_TIME = DateFormat.getDateInstance().format(new Date()) + " "
				+ DateFormat.getTimeInstance().format(new Date());

		// load global automation variables
		loadAutomationPropertySettings();

		// clear result folder
		if (isSuite && AUTOMATION_CLEAR_RESULTS && report == null) {
			FileIO.clearDirectory(Paths.get(AUTOMATION_TEST_RESULTS_PATH));
			Platform.sleep(AUTOMATION_WAIT_VALUE_1);
			FileIO.dirExists(Paths.get(AUTOMATION_TEST_RESULTS_PATH));
		}

		if (report == null) {
			report = new AutomationReport(
					AUTOMATION_TEST_RESULTS_PATH + AUTOMATION_SCRIPT_NAME + "_" + AUTOMATION_SCRIPT_UID);
		}

		report.startSuite(AUTOMATION_SCRIPT_NAME);

		generateHeaderInfo("Suite");

		// Initialize suite stats
		try (FileOutputStream out = new FileOutputStream(AUTOMATION_DEFAULT_SUITE_STAT_FILE)) {

			final Properties settings = new Properties();

			settings.put("ScriptCounter", String.valueOf(0));
			settings.put("ScriptPassedCounter", String.valueOf(0));
			settings.put("ScriptFailedCounter", String.valueOf(0));
			settings.put("ScriptPrctPassed", String.valueOf(0));
			settings.put("ScriptPrctFailed", String.valueOf(0));

			settings.put("TestCaseCounter", String.valueOf(0));
			settings.put("TestCasePassedCounter", String.valueOf(0));
			settings.put("TestCaseFailCounter", String.valueOf(0));
			settings.put("PrctPassed", String.valueOf(0));
			settings.put("PrctFailed", String.valueOf(0));
			settings.put("TestActionCounter", String.valueOf(0));
			settings.put("ErrorCounter", String.valueOf(0));
			settings.put("ElapsedTime", String.valueOf(0));

			// Close out properties file
			settings.store(out, "");
		} catch (final IOException ioe) {
			errorHandler("Error saving suite statistic INI variables in file: " + AUTOMATION_DEFAULT_SUITE_STAT_FILE,
					ioe);
		}
	}

	/**
	 * Displays a banner in the result log output
	 *
	 * @param banner
	 *            message to display in banner
	 */
	public static void logBanner(final String banner) {
		// logs a banner like this in log output:
		// ==============================================================================
		// My Test Info
		// ==============================================================================
		final String lineSeparator = "==============================================================================";
		logScriptInfo(lineSeparator, LOGTYPE_SIMPLE);
		logScriptInfo(banner, LOGTYPE_SIMPLE);
		logScriptInfo(lineSeparator, LOGTYPE_SIMPLE);
	}

	/**
	 * Logs test suite statistics
	 */
	public static void logSuiteStats() {
		final StringBuilder summary = new StringBuilder();

		int scriptCounter;
		int scriptPassedCounter;
		int scriptFailedCounter;
		double scriptPrctPassed;
		double scriptPrctFailed;

		int testCaseCounter;
		int testCasePassedCounter;
		int testCaseFailCounter;
		double prctPassed;
		double prctFailed;
		int testActionCounter;
		int errorCounter;
		long elapsedTime;

		// open temporary file to tally test statistics for suite results

		try (FileInputStream in = new FileInputStream(AUTOMATION_DEFAULT_SUITE_STAT_FILE)) {
			final Properties settings = new Properties();
			settings.load(in);

			scriptCounter = Integer.parseInt(settings.getProperty("ScriptCounter"));
			scriptPassedCounter = Integer.parseInt(settings.getProperty("ScriptPassedCounter"));
			scriptFailedCounter = Integer.parseInt(settings.getProperty("ScriptFailedCounter"));
			scriptPrctPassed = Double.parseDouble(settings.getProperty("ScriptPrctPassed"));
			scriptPrctFailed = Double.parseDouble(settings.getProperty("ScriptPrctFailed"));
			testCaseCounter = Integer.parseInt(settings.getProperty("TestCaseCounter"));
			testCasePassedCounter = Integer.parseInt(settings.getProperty("TestCasePassedCounter"));
			testCaseFailCounter = Integer.parseInt(settings.getProperty("TestCaseFailCounter"));
			prctPassed = Double.parseDouble(settings.getProperty("PrctPassed"));
			prctFailed = Double.parseDouble(settings.getProperty("PrctFailed"));
			testActionCounter = Integer.parseInt(settings.getProperty("TestActionCounter"));
			errorCounter = Integer.parseInt(settings.getProperty("ErrorCounter"));
			elapsedTime = Long.parseLong(settings.getProperty("ElapsedTime"));

			final DateFormat dtFormat = DateFormat.getDateInstance();
			final DateFormat tmFormat = DateFormat.getTimeInstance();

			in.close();

			// Get Suite Test Summary information
			summary.delete(0, summary.length()); // clear stringbuffer
			summary.append("******************************************************************************" + gsNewline) // inserted
																															// gsNewline
																															// instead
																															// of
																															// /n
					.append("* Test Suite Summary\n")
					.append("******************************************************************************"
							+ gsNewline)
					.append("* Total Scripts Executed:                " + scriptCounter + gsNewline)
					.append("* Total Scripts Passed:                  " + scriptPassedCounter + gsNewline)
					.append("* Total Scripts Failed:                  " + scriptFailedCounter + gsNewline)
					.append("* Total Percent Scripts Passed:          " + scriptPrctPassed + "%" + gsNewline)
					.append("* Total Percent Scripts Failed:          " + scriptPrctFailed + "%" + gsNewline)
					.append("* Total Testcases Executed:                " + testCaseCounter + gsNewline)
					.append("* Total Testcases Passed:                  " + testCasePassedCounter + gsNewline)
					.append("* Total Testcases Failed:                  " + testCaseFailCounter + gsNewline)
					.append("* Total Percent Testcases Passed:          " + prctPassed + "%" + gsNewline)
					.append("* Total Percent Testcases Failed:          " + prctFailed + "%" + gsNewline)
					.append("* Total Number of Test Actions:            " + testActionCounter + gsNewline)
					.append("* Total Number of Errors found:            " + errorCounter + gsNewline)
					.append("* End Date and Time:                       " + dtFormat.format(new Date()) + " "
							+ tmFormat.format(new Date()))
					.append("* Total Elapsed Time:                      "
							+ (report.isMasterSuite() ? report.getCurrentSuite().getTimer().getElapsedTime()
									: DateTime.getFormattedDateTimeForElapsedTime(elapsedTime))
							+ gsNewline) // getFormattedDateTime
					.append("******************************************************************************"
							+ gsNewline);

		} catch (final Exception e) {
			errorHandler(
					"Error loading suite statistic property variables in file: " + AUTOMATION_DEFAULT_SUITE_STAT_FILE,
					e);
		}

		report.finishSuite();

		report.generateStatistic();

		if (report.isMasterSuite()) {

			report.openHTMLReport();

		} else {
			report.updateCurrentSuite();
		}

		engine = null;
	}

	/**
	 * Displays interactive dialog message
	 * <p>
	 *
	 * @param msg
	 *            message to display
	 * @param title
	 *            title of the dialog
	 */
	public static void displayMessageDlg(final String msg, final String title) {
		JOptionPane.showMessageDialog(null, msg, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Compares 2 boolean values and logs result info
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final boolean expected, final boolean actual, final String desc) {
		if (actual == expected) {
			logScriptInfo(String.format("Verify %s Expected: \"%s\" Actual: \"%s\"", desc, expected, actual),
					AUTOMATION_LOG_TYPE);
			return true;
		}

		errorHandler(String.format("Verify %s Expected: \"%s\" Actual: \"%s\"", desc, expected, actual));
		return false;
	}

	/**
	 * Compares 2 boolean values and displays all result info, throws exception
	 * if no match
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @return true if matched false if no-match Intended to be called within a
	 *         try/catch block of a testcase and cease execution of subsequent
	 *         commands in the testcase if the exception is thrown. If the
	 *         calling method should continue execution even if the comparison
	 *         fails, use altVerify instead of altVerifyFatal
	 * @throws Exception
	 *             error
	 */
	public static boolean verifyFatal(final boolean expected, final boolean actual, final String desc)
			throws Exception {
		if (!verify(expected, actual, desc)) {
			throw new Exception(String.format("Error performing: %s Expected: %s Actual: %s", desc, expected, actual));
		}

		return true;
	}

	/**
	 * Compares 2 boolean values and displays result info - only log output of
	 * errors
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @return true if matched false if no-match
	 */
	public static boolean confirm(final boolean expected, final boolean actual, final String desc) {
		if (actual == expected) {
			// logScriptInfo(String.format("Verify %s Expected: %s Actual: %s",
			// desc, expected, actual), AUTOMATION_LOG_TYPE);
			return true;
		}

		errorHandler(String.format("%s Expected: %s Actual: %s", desc, expected, actual));
		return false;
	}

	/**
	 * Compares 2 String values and displays all result info, throws exception
	 * if no match
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param exact
	 *            true if exact match is required, false for case insensitive
	 * @param desc
	 *            Description of boolean comparison
	 * @return true if matched false if no-match Intended to be called within a
	 *         try/catch block of a testcase and cease execution of subsequent
	 *         commands in the testcase if the exception is thrown. If the
	 *         calling method should continue execution even if the comparison
	 *         fails, use altVerify instead of altVerifyFatal
	 * @throws Exception
	 *             error
	 */
	public static boolean verifyFatal(final String expected, final String actual, final boolean exact,
			final String desc) throws Exception {
		if (!verify(expected, actual, exact, desc)) {
			throw new Exception(
					String.format("Error verifying: %s Expected: \"%s\" Actual: \"%s\"", desc, expected, actual));
		}

		return true;
	}

	/**
	 * Compares 2 string values and displays all result info
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param exactMatch
	 *            true means actual and expected must be exact, false means
	 *            Expected must be contained within Actual
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final String expected, final String actual, final boolean exactMatch) {
		if (exactMatch) {
			if (actual.equalsIgnoreCase(expected)) {
				logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\"", expected, actual),
						AUTOMATION_LOG_TYPE);
				return true;
			}

			errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\"", expected, actual));
			return false;
		}

		if (actual.toUpperCase().contains(expected.toUpperCase())) {
			logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\"", expected, actual),
					AUTOMATION_LOG_TYPE);
			return true;
		}

		errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\"", expected, actual));
		return false;

	}

	/**
	 * Compares 2 string values and displays all result info. Can test for exact
	 * match
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param exactMatch
	 *            true means comparison variables must match exactly to pass,
	 *            false means comparison variables can be a partial match or
	 *            subset
	 * @param description
	 *            Description of the test
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final String expected, final String actual, final boolean exactMatch,
			final String description) {
		if (exactMatch) {

			if (actual.equalsIgnoreCase(expected)) {
				logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected, actual, description),
						AUTOMATION_LOG_TYPE);
				return true;
			}
			errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected, actual, description));
			return false;
		}
		if (actual.toUpperCase().contains(expected.toUpperCase())) {
			logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected, actual, description),
					AUTOMATION_LOG_TYPE);
			return true;
		}

		errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected, actual, description));
		return false;
	}

	/**
	 * Compares 2 List values and displays all result info
	 * <p>
	 * 
	 * @param expected
	 *            Expected List values
	 * @param actual
	 *            Actual list values
	 * @param description
	 *            Description of the test
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final List<String> expected, final List<String> actual, final String description) {
		boolean bMatch = true;
		try {

			for (int i = 0; i < expected.size(); i++) {
				if (expected.get(i).equals(actual.get(i))) {
					logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected.get(i),
							actual.get(i), description), AUTOMATION_LOG_TYPE);
				} else {
					errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected.get(i),
							actual.get(i), description));
					bMatch = false;
				}
			}

			return bMatch;
		} catch (final Exception e) {
			errorHandler("Lists did not match", e);
			return false;

		}
	}

	/**
	 * Compares 2 String array values and displays all result info
	 * <p>
	 *
	 * @param expected
	 *            Expected String array values
	 * @param actual
	 *            Actual String array values
	 * @param description
	 *            Description of the test
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final String[] expected, final String[] actual, final String description) {
		boolean bMatch = true;
		try {
			for (int i = 0; i < expected.length; i++) {
				if (expected[i].equals(actual[i])) {
					logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected[i], actual[i],
							description), AUTOMATION_LOG_TYPE);
				} else {
					errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", expected[i], actual[i],
							description));
					bMatch = false;
				}
			}
			return bMatch;
		} catch (final Exception e) {
			errorHandler("Lists did not match", e);
			return false;

		}
	}

	/**
	 * Compares 2 String Array values and displays all result info
	 * <p>
	 *
	 * @param expected
	 *            Expected array values
	 * @param actual
	 *            Actual array values
	 * @param description
	 *            Description of the test
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final String[][] expected, final String[][] actual, final String description) {
		try {
			final boolean result = Arrays.deepEquals(expected, actual);

			if (result) {
				logScriptInfo(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", Arrays.deepToString(expected),
						Arrays.deepToString(actual), description), AUTOMATION_LOG_TYPE);
				return true;
			}
			errorHandler(String.format("Verify Expected: \"%s\" Actual: \"%s\" %s", Arrays.deepToString(expected),
					Arrays.deepToString(actual), description));
			return false;

		} catch (final Exception e) {
			errorHandler("Error occurred comparing Arrays", e);
			return false;
		}
	}

	/**
	 * Compares 2 HashMaps and displays all result info
	 * <p>
	 * 
	 * @param expected
	 *            Expected array values
	 * @param actual
	 *            Actual array values
	 * @param description
	 *            Description of the test
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final Map<String, String> expected, final Map<String, String> actual,
			final String description) {
		final List<String> lsExpected = new ArrayList<>();
		final List<String> lsActual = new ArrayList<>();
		try {

			for (final Map.Entry<String, String> a : actual.entrySet()) {
				a.getKey();
				a.getValue();
				lsActual.add(a.getKey() + "=" + a.getValue());
			}

			for (final Map.Entry<String, String> e : expected.entrySet()) {
				e.getKey();
				e.getValue();
				lsExpected.add(e.getKey() + "=" + e.getValue());
			}

			return verify(lsExpected, lsActual, description);

		} catch (final Exception e) {
			errorHandler("Maps did not match", e);
			return false;

		}
	}

	/**
	 * Compares 2 integer values and displays all result info
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @return true if matched false if no-match
	 */
	public static boolean verify(final int expected, final int actual, final String desc) {
		if (actual == expected) {
			logScriptInfo(String.format("Verify %s Expected: \"%s\" Actual: \"%s\"", desc, expected, actual),
					AUTOMATION_LOG_TYPE);
			return true;
		}

		errorHandler(String.format("Verify %s Expected: \"%s\" Actual: \"%s\"", desc, expected, actual));
		return false;
	}

	/**
	 * Check value is null and displays all result info
	 *
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @param <T>
	 *            class
	 * @return true if matched false if no-match
	 */
	public static <T> boolean verifyNull(final T actual, final String desc) {
		if (null == actual) {
			logScriptInfo(String.format("Verify %s is null", desc), AUTOMATION_LOG_TYPE);
			return true;
		}
		errorHandler(String.format("Verify %s is not null", desc));
		return false;
	}

	/**
	 * Check value is not null and displays all result info
	 *
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @param <T>
	 *            class
	 * @return true if matched false if no-match
	 */
	public static <T> boolean verifyNotNull(final T actual, final String desc) {
		if (null != actual) {
			logScriptInfo(String.format("Verify %s is not null", desc), AUTOMATION_LOG_TYPE);
			return true;
		}

		errorHandler(String.format("Verify %s is null", desc));
		return false;
	}

	/**
	 * Compares 2 integer values and displays all result info. Throws Exception
	 * that will stop script upon failure
	 * <p>
	 * 
	 * @param expected
	 *            Expected Value
	 * @param actual
	 *            Actual Value
	 * @param desc
	 *            Description of boolean comparison
	 * @return true if matched false if no-match
	 * @throws Exception
	 *             error
	 */
	public static boolean verifyFatal(final int expected, final int actual, final String desc) throws Exception {
		if (!verify(expected, actual, desc)) {
			throw new Exception("Error verifying: " + desc);
		}

		return true;
	}

	// ****************************Automation Properties methods

	/**
	 * Loads system specific global automation variables
	 */
	public static void loadAutomationPropertySettings() {

		// get automation properties from files or other sources
		final Properties autoProps = getAutomationProperties();

		// assign Automation property values
		try {
			String key = "";

			// Parent Automation Directory
			key = AUTOMATION_AUTO_PATH;
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_TEST_PROJECT_PATH = autoProps.getProperty(key);
			}

			// Test Data Directory
			key = "AUTOMATION_TEST_DATA_PATH";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_TEST_DATA_PATH = autoProps.getProperty(key);
			}

			// Result Log Directory
			key = "AUTOMATION_TEST_RESULTS_PATH";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_TEST_RESULTS_PATH = autoProps.getProperty(key);
			}

			// If result directory doesn't exist create it
			FileIO.dirExists(Paths.get(AUTOMATION_TEST_RESULTS_PATH));

			// Image suffix
			key = "AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX = autoProps.getProperty(key);
			}

			// Spreadsheet suffix
			key = "AUTOMATION_SPREADSHEET_SUFFIX";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_SPREADSHEET_SUFFIX = autoProps.getProperty(key);
			}

			// Log type
			key = "AUTOMATION_LOG_TYPE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_LOG_TYPE = Integer.parseInt(autoProps.getProperty(key));
			}

			// Log Level
			key = "AUTOMATION_REPORT_TYPE";
			if (autoProps.containsKey(key)) {
				AUTOMATION_REPORT_TYPE = autoProps.getProperty(key);
			}

			// Print locator
			key = "AUTOMATION_LOCATOR_PRINT";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_LOCATOR_PRINT = Boolean.valueOf(autoProps.getProperty(key));
			}
			// JUnit Fails
			key = "AUTOMATION_JUNIT_FAIL_ENABLE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_JUNIT_FAIL_ENABLE = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Update data in runtime
			key = "AUTOMATION_RUNTIME_UPDATE_DATA";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_RUNTIME_UPDATE_DATA = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Stack Trace
			key = "AUTOMATION_STACK_TRACE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_STACK_TRACE = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Auto Image Capture on every testcase
			key = "AUTOMATION_IMAGE_CAPTURE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_IMAGE_CAPTURE = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Browser Capture instead of desktop capture
			key = "AUTOMATION_BROWSER_CAPTURE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSER_CAPTURE = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Delete all files in results folder before start script
			key = "AUTOMATION_CLEAR_RESULTS";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_CLEAR_RESULTS = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Auto Image capture on every error
			key = "AUTOMATION_ERROR_IMAGE_CAPTURE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_ERROR_IMAGE_CAPTURE = Boolean.valueOf(autoProps.getProperty(key));
			}

			key = "AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Close browser at script completion
			key = "AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION = Boolean.valueOf(autoProps.getProperty(key));
			}

			// Global Automation Time out / Wait variables
			key = "AUTOMATION_WAIT_VALUE_1";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_1 = Integer.parseInt(autoProps.getProperty(key));
			}

			key = "AUTOMATION_WAIT_VALUE_2";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_2 = Integer.parseInt(autoProps.getProperty(key));
			}

			key = "AUTOMATION_WAIT_VALUE_5";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_5 = Integer.parseInt(autoProps.getProperty(key));
			}

			key = "AUTOMATION_WAIT_VALUE_10";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_10 = Integer.parseInt(autoProps.getProperty(key));
			}

			key = "AUTOMATION_WAIT_VALUE_15";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_15 = Integer.parseInt(autoProps.getProperty(key));
			}

			key = "AUTOMATION_WAIT_VALUE_30";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_30 = Integer.parseInt(autoProps.getProperty(key));
			}

			key = "AUTOMATION_WAIT_VALUE_60";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_WAIT_VALUE_60 = Integer.parseInt(autoProps.getProperty(key));
			}

			// Test browser
			key = "AUTOMATION_TEST_BROWSER";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_TEST_BROWSER = autoProps.getProperty(key);
			}

			// Determines if browser is Internet Explorer, and if so, what
			// version to set a standard
			// value for the browser based on the value in Automation.properties
			// Default IE browser is IE6. Look for most recent version of
			// browser first
			if (AUTOMATION_TEST_BROWSER.indexOf("Internet Explorer") != -1
					|| AUTOMATION_TEST_BROWSER.indexOf("IE") != -1) {
				if (AUTOMATION_TEST_BROWSER.contains("8")) {
					AUTOMATION_TEST_BROWSER = Browser.gsInternetExplorer8;
				} else if (AUTOMATION_TEST_BROWSER.contains("7")) {
					AUTOMATION_TEST_BROWSER = Browser.gsInternetExplorer7;
				} else if (AUTOMATION_TEST_BROWSER.contains("9")) {
					AUTOMATION_TEST_BROWSER = Browser.gsInternetExplorer9;
				} else if (AUTOMATION_TEST_BROWSER.contains("10")) {
					AUTOMATION_TEST_BROWSER = Browser.gsInternetExplorer10;
				} else if (AUTOMATION_TEST_BROWSER.contains("11")) {
					AUTOMATION_TEST_BROWSER = Browser.gsInternetExplorer11;

				}

			}

			// Test Result Viewer application/browser
			key = "AUTOMATION_RESULT_VIEWER_APP";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_RESULT_VIEWER_APP = autoProps.getProperty(key);
			}

			// Browser Profile Properties
			key = "AUTOMATION_BROWSER_PROFILE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSER_PROFILE = autoProps.getProperty(key);
			}

			key = "AUTOMATION_CHROME_COMMANDLINE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_CHROME_COMMANDLINE = autoProps.getProperty(key);
			}

			// BrowserPath
			key = "AUTOMATION_BROWSER_PATH";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSER_PATH = autoProps.getProperty(key);
			}

			// Test level
			key = "AUTOMATION_TEST_LEVEL";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_TEST_LEVEL = Integer.parseInt(autoProps.getProperty(key));
			}
			
			//Copy results to another location
			key = "AUTOMATION_COPY_RESULTS_TO";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_COPY_RESULTS_TO = autoProps.getProperty(key);
			}
			
			
			

			// Remote server name or ip address
			key = "AUTOMATION_SCM_REMOTE_SERVER_NAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_SCM_REMOTE_SERVER_NAME = autoProps.getProperty(key);
			}

			// Remote server user name
			key = "AUTOMATION_SCM_REMOTE_SERVER_USERNAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_SCM_REMOTE_SERVER_USERNAME = autoProps.getProperty(key);
			}

			// Remote server user password
			key = "AUTOMATION_SCM_REMOTE_SERVER_PASSWORD";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_SCM_REMOTE_SERVER_PASSWORD = autoProps.getProperty(key);
			}

			// Remote server port
			key = "AUTOMATION_SCM_REMOTE_SERVER_PORT";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_SCM_REMOTE_SERVER_PORT = autoProps.getProperty(key);
				if (AUTOMATION_SCM_REMOTE_SERVER_PORT == null) {
					AUTOMATION_SCM_REMOTE_SERVER_PORT = "22";
				}
			}

			// FTP server name or ip address
			key = "AUTOMATION_FTP_SERVER_NAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_FTP_SERVER_NAME = autoProps.getProperty(key);
			}

			// FTP server user name
			key = "AUTOMATION_FTP_SERVER_USERNAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_FTP_SERVER_USERNAME = autoProps.getProperty(key);
			}

			// FTP server user password
			key = "AUTOMATION_FTP_SERVER_PASSWORD";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_FTP_SERVER_PASSWORD = autoProps.getProperty(key);
			}

			// FTP server port
			key = "AUTOMATION_FTP_SERVER_PORTAL";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_FTP_SERVER_PORTAL = autoProps.getProperty(key);
				if (AUTOMATION_FTP_SERVER_PORTAL == null) {
					AUTOMATION_FTP_SERVER_PORTAL = "21";
				}
			}
			// Database server driver system
			key = "AUTOMATION_DATABASE_SERVER_SYSTEM";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_DATABASE_SERVER_SYSTEM = autoProps.getProperty(key);

			}

			// Database server name or ip address
			key = "AUTOMATION_DATABASE_SERVER_NAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_DATABASE_SERVER_NAME = autoProps.getProperty(key);
			}

			// Database server instance name
			key = "AUTOMATION_DATABASE_SERVER_INSTANCE_NAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_DATABASE_SERVER_INSTANCE_NAME = autoProps.getProperty(key);
			}

			// Database server user name
			key = "AUTOMATION_DATABASE_SERVER_USERNAME";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_DATABASE_SERVER_USERNAME = autoProps.getProperty(key);
			}

			// Database server user password
			key = "AUTOMATION_DATABASE_SERVER_PASSWORD";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_DATABASE_SERVER_PASSWORD = autoProps.getProperty(key);
			}

			// Database server port
			key = "AUTOMATION_DATABASE_SERVER_PORT";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_DATABASE_SERVER_PORT = autoProps.getProperty(key);

			}
			// BrowserStack
			key = "AUTOMATION_BROWSERSTACK_ENABLE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_ENABLE = Boolean.valueOf(autoProps.getProperty(key));
			}

			// BrowserStack license key
			key = "AUTOMATION_BROWSERSTACK_LICENSE_KEY";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_LICENSE_KEY = autoProps.getProperty(key);
			}

			// BrowserStack license user
			key = "AUTOMATION_BROWSERSTACK_LICENSE_USER";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_LICENSE_USER = autoProps.getProperty(key);
			}

			// BrowserStack Operating System
			key = "AUTOMATION_BROWSERSTACK_OS";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_OS = autoProps.getProperty(key);
			}

			// BrowserStack Operating System
			key = "AUTOMATION_BROWSERSTACK_OS_VERSION";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_OS_VERSION = autoProps.getProperty(key);
			}

			// Screen resolution for BrowserStack
			key = "AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION = autoProps.getProperty(key);
			}

			// BrowserStack Browser Version
			key = "AUTOMATION_BROWSERSTACK_BROWSER_VERSION";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BROWSERSTACK_BROWSER_VERSION = autoProps.getProperty(key);
			}

			// BandWidth limit
			key = "AUTOMATION_BANDWIDTH_LIMIT";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BANDWIDTH_LIMIT = Boolean.valueOf(autoProps.getProperty(key));
			}

			// BandWidth read limit
			key = "AUTOMATION_BANDWIDTH_LIMIT_READ";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BANDWIDTH_LIMIT_READ = Integer.valueOf(autoProps.getProperty(key));
			}

			// BandWidth write limit
			key = "AUTOMATION_BANDWIDTH_LIMIT_WRITE";
			if (autoProps.containsKey(key) && !autoProps.getProperty(key).isEmpty()) {
				AUTOMATION_BANDWIDTH_LIMIT_WRITE = Integer.valueOf(autoProps.getProperty(key));
			}
		} catch (final Exception e) {
			errorHandler("Error loading automation Property settings", e);
		}

	}

	/**
	 * This method prints out the global variables in the automation.properties
	 * file after they have been initialized
	 */
	public static void printInitializedAutomationProperties() {
		logBanner("Displaying the initialized automation.properties values");
		logScriptInfo("AUTOMATION_TEST_PROJECT_PATH=" + AUTOMATION_TEST_PROJECT_PATH);
		logScriptInfo("AUTOMATION_TEST_DATA_PATH=" + AUTOMATION_TEST_DATA_PATH);
		logScriptInfo("AUTOMATION_TEST_RESULTS_PATH=" + AUTOMATION_TEST_RESULTS_PATH);
		logScriptInfo("AUTOMATION_TEST_BROWSER=" + AUTOMATION_TEST_BROWSER);
		logScriptInfo(
				"AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION=" + String.valueOf(AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION));
		logScriptInfo("AUTOMATION_CLEAR_RESULTS=" + String.valueOf(AUTOMATION_CLEAR_RESULTS));
		logScriptInfo("AUTOMATION_SPREADSHEET_SUFFIX=" + AUTOMATION_SPREADSHEET_SUFFIX);
		logScriptInfo("AUTOMATION_LOG_TYPE=" + String.valueOf(AUTOMATION_LOG_TYPE));
		logScriptInfo("AUTOMATION_REPORT_TYPE=" + AUTOMATION_REPORT_TYPE);
		logScriptInfo("AUTOMATION_IMAGE_CAPTURE=" + String.valueOf(AUTOMATION_IMAGE_CAPTURE));
		logScriptInfo("AUTOMATION_BROWSER_CAPTURE=" + String.valueOf(AUTOMATION_BROWSER_CAPTURE));
		logScriptInfo("AUTOMATION_LOCATOR_PRINT=" + String.valueOf(AUTOMATION_LOCATOR_PRINT));
		logScriptInfo("AUTOMATION_JUNIT_FAIL_ENABLE=" + String.valueOf(AUTOMATION_JUNIT_FAIL_ENABLE));
		logScriptInfo("AUTOMATION_RUNTIME_UPDATE_DATA=" + String.valueOf(AUTOMATION_RUNTIME_UPDATE_DATA));
		logScriptInfo("AUTOMATION_STACK_TRACE=" + String.valueOf(AUTOMATION_STACK_TRACE));
		logScriptInfo("AUTOMATION_ERROR_IMAGE_CAPTURE=" + String.valueOf(AUTOMATION_ERROR_IMAGE_CAPTURE));
		logScriptInfo("AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION="
				+ String.valueOf(AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION));
		// logScriptInfo("gbAutomationSilkCentral=" +
		// String.valueOf(gbAutomationSilkCentral));
		logScriptInfo("AUTOMATION_WAIT_VALUE_1=" + String.valueOf(AUTOMATION_WAIT_VALUE_1));
		logScriptInfo("AUTOMATION_WAIT_VALUE_2=" + String.valueOf(AUTOMATION_WAIT_VALUE_2));
		logScriptInfo("AUTOMATION_WAIT_VALUE_5=" + String.valueOf(AUTOMATION_WAIT_VALUE_5));
		logScriptInfo("AUTOMATION_WAIT_VALUE_10=" + String.valueOf(AUTOMATION_WAIT_VALUE_10));
		logScriptInfo("AUTOMATION_WAIT_VALUE_15=" + String.valueOf(AUTOMATION_WAIT_VALUE_15));
		logScriptInfo("AUTOMATION_WAIT_VALUE_30=" + String.valueOf(AUTOMATION_WAIT_VALUE_30));
		logScriptInfo("AUTOMATION_WAIT_VALUE_60=" + String.valueOf(AUTOMATION_WAIT_VALUE_60));
		logScriptInfo("AUTOMATION_RESULT_VIEWER_APP=" + AUTOMATION_RESULT_VIEWER_APP);
		logScriptInfo("AUTOMATION_BROWSER_PROFILE=" + AUTOMATION_BROWSER_PROFILE);
		logScriptInfo("AUTOMATION_BROWSER_PATH=" + AUTOMATION_BROWSER_PATH);
		logScriptInfo("AUTOMATION_CHROME_COMMANDLINE=" + AUTOMATION_CHROME_COMMANDLINE);
		logScriptInfo("AUTOMATION_TEST_LEVEL=" + String.valueOf(AUTOMATION_TEST_LEVEL));
		logScriptInfo("AUTOMATION_COPY_RESULTS_TO=" + AUTOMATION_COPY_RESULTS_TO);

		logScriptInfo("AUTOMATION_SCM_REMOTE_SERVER_NAME=" + AUTOMATION_SCM_REMOTE_SERVER_NAME);
		logScriptInfo("AUTOMATION_SCM_REMOTE_SERVER_USERNAME=" + AUTOMATION_SCM_REMOTE_SERVER_USERNAME);
		logScriptInfo("AUTOMATION_SCM_REMOTE_SERVER_PASSWORD=" + AUTOMATION_SCM_REMOTE_SERVER_PASSWORD);
		logScriptInfo("AUTOMATION_SCM_REMOTE_SERVER_PORT=" + AUTOMATION_SCM_REMOTE_SERVER_PORT);

		logScriptInfo("AUTOMATION_FTP_SERVER_NAME=" + AUTOMATION_FTP_SERVER_NAME);
		logScriptInfo("AUTOMATION_FTP_SERVER_USERNAME=" + AUTOMATION_FTP_SERVER_USERNAME);
		logScriptInfo("AUTOMATION_FTP_SERVER_PASSWORD=" + AUTOMATION_FTP_SERVER_PASSWORD);
		logScriptInfo("AUTOMATION_FTP_SERVER_PORTAL=" + AUTOMATION_FTP_SERVER_PORTAL);

		logScriptInfo("AUTOMATION_DATABASE_SERVER_SYSTEM=" + AUTOMATION_DATABASE_SERVER_SYSTEM);
		logScriptInfo("AUTOMATION_DATABASE_SERVER_NAME=" + AUTOMATION_DATABASE_SERVER_NAME);
		logScriptInfo("AUTOMATION_DATABASE_SERVER_INSTANCE_NAME=" + AUTOMATION_DATABASE_SERVER_INSTANCE_NAME);
		logScriptInfo("AUTOMATION_DATABASE_SERVER_USERNAME=" + AUTOMATION_DATABASE_SERVER_USERNAME);
		logScriptInfo("AUTOMATION_DATABASE_SERVER_PASSWORD=" + AUTOMATION_DATABASE_SERVER_PASSWORD);
		logScriptInfo("AUTOMATION_DATABASE_SERVER_PORT=" + AUTOMATION_DATABASE_SERVER_PORT);

		// BrowserStack
		logScriptInfo("AUTOMATION_BROWSERSTACK_ENABLE=" + String.valueOf(AUTOMATION_BROWSERSTACK_ENABLE));
		logScriptInfo("AUTOMATION_BROWSERSTACK_LICENSE_KEY=" + AUTOMATION_BROWSERSTACK_LICENSE_KEY.toString());
		logScriptInfo("AUTOMATION_BROWSERSTACK_LICENSE_USER=" + AUTOMATION_BROWSERSTACK_LICENSE_USER.toString());
		logScriptInfo("AUTOMATION_BROWSERSTACK_OS=" + AUTOMATION_BROWSERSTACK_OS.toString());
		logScriptInfo("AUTOMATION_BROWSERSTACK_OS_VERSION=" + AUTOMATION_BROWSERSTACK_OS_VERSION.toString());
		logScriptInfo(
				"AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION=" + AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION.toString());
		logScriptInfo("AUTOMATION_BROWSERSTACK_BROWSER_VERSION=" + AUTOMATION_BROWSERSTACK_BROWSER_VERSION.toString());
		// BandWidth
		logScriptInfo("AUTOMATION_BANDWIDTH_LIMIT=" + String.valueOf(AUTOMATION_BANDWIDTH_LIMIT));
		logScriptInfo("AUTOMATION_BANDWIDTH_LIMIT_READ=" + String.valueOf(AUTOMATION_BANDWIDTH_LIMIT_READ));
		logScriptInfo("AUTOMATION_BANDWIDTH_LIMIT_WRITE=" + String.valueOf(AUTOMATION_BANDWIDTH_LIMIT_WRITE));

	}

	/**
	 * Returns an alternate file directory, for the automation.properties file,
	 * specified in the System environment variable
	 * ALT_AUTOMATION_PROPERTIES_DIR
	 *
	 * @return Sting containing alternate location for Automation properties
	 *         files
	 */
	public static String getAlternateAutoPropDir() {
		String dirName = System.getenv("ALT_AUTOMATION_PROPERTIES_DIR");

		if (dirName == null) {
			dirName = "";
		} else if (!dirName.endsWith(Platform.getFileSeparator())) {
			dirName = dirName + Platform.getFileSeparator();
		}

		return dirName;
	}

	/**
	 * Creates a file containing the default settings for the
	 * automation.properties file
	 *
	 * @param file
	 *            to use for default automation properties file
	 */
	public static void createDefaultAutomationPropertiesFile(final String file) {

		// clear file content
		FileIO.writeFileContents(file, "");

		// write out default automation.properties file
		FileIO.appendStringToFile(file, "#Visiant Selenium Automation Properties");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "# Place this properties file in your local user home directory");
		FileIO.appendStringToFile(file, "# To find your home folder in windows Click Start->Run enter %HOMEPATH% and click OK - Windows will");
		FileIO.appendStringToFile(file, "# open your home folder. This is the folder that the automation.properties file should be placed in. ");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#General Automation Properties");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#Local Automation directory");
		FileIO.appendStringToFile(file, "AUTOMATION_TEST_PROJECT_PATH=C:\\automation\\google\\src\\main\\java\\google\\");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Test data and support document directory");
		FileIO.appendStringToFile(file, "#gsAutomationAutoSupportDocs=C:\\automation\\google\\src\\main\\resources\\google\\testdata\\");
		FileIO.appendStringToFile(file, "AUTOMATION_TEST_DATA_PATH=C:\\automation\\google\\src\\main\\resources\\google\\testdata\\");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Result log directory");
		FileIO.appendStringToFile(file, "AUTOMATION_TEST_RESULTS_PATH=C:\\results\\google\\");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Test Browser Name");
		FileIO.appendStringToFile(file, "AUTOMATION_TEST_BROWSER=Firefox");
		FileIO.appendStringToFile(file, "#AUTOMATION_TEST_BROWSER=Internet Explorer");
		FileIO.appendStringToFile(file, "#AUTOMATION_TEST_BROWSER=Google Chrome");
		FileIO.appendStringToFile(file, "#AUTOMATION_TEST_BROWSER=Safari");
		FileIO.appendStringToFile(file, "#AUTOMATION_TEST_BROWSER=Opera");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Close Test Browsers upon test script completion");
		FileIO.appendStringToFile(file, "AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION=true");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Delete all files in results folder before start script");
		FileIO.appendStringToFile(file, "AUTOMATION_CLEAR_RESULTS=false");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Global boolean to capture browser window");
		FileIO.appendStringToFile(file, "#If variable is false then entire screen (primary desktop) will be captured");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSER_CAPTURE=true");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Browser profiles allow you to customize your browsers options. To use use a browser");
		FileIO.appendStringToFile(file, "#profile file enter the name of the profile file here. Leave empty if the default browser is what you want.");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSER_PROFILE=");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Browser Path. leaving this property empty tells automation to use the default browser path");
		FileIO.appendStringToFile(file, "#If you are using a Opera or require an alternate browser directory, you can set it here");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSER_PATH=");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Chrome Command line commands ");
		FileIO.appendStringToFile(file, "AUTOMATION_CHROME_COMMANDLINE=");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Log Type. There 9 different log output formats.");
		FileIO.appendStringToFile(file, "AUTOMATION_LOG_TYPE=1");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Capture Stack Trace upon failure");
		FileIO.appendStringToFile(file, "AUTOMATION_STACK_TRACE=true");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Capture Screen Image upon failure");
		FileIO.appendStringToFile(file, "AUTOMATION_ERROR_IMAGE_CAPTURE=true");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Capture a Screen Image during every testcase");
		FileIO.appendStringToFile(file, "AUTOMATION_IMAGE_CAPTURE=false");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Enable JUnit fails upon each testcase failure");
		FileIO.appendStringToFile(file, "AUTOMATION_JUNIT_FAIL_ENABLE=true");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Print widgets locators into the output");
		FileIO.appendStringToFile(file, "AUTOMATION_LOCATOR_PRINT=false");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Test Level");
		FileIO.appendStringToFile(file, "AUTOMATION_TEST_LEVEL=2");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Test Data Spreadsheet suffix");
		FileIO.appendStringToFile(file, "AUTOMATION_SPREADSHEET_SUFFIX=.xls");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Result log error image suffix");
		FileIO.appendStringToFile(file, "AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX=.jpg");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Global boolean to update test case data in runtime before starting of each test case.");
		FileIO.appendStringToFile(file, "#Number of test cases must be static and known before execution.");
		FileIO.appendStringToFile(file, "#To use this feature the test script should be instance of AbstractTestScript");
		FileIO.appendStringToFile(file, "#and use core dataproviders.");
		FileIO.appendStringToFile(file, "AUTOMATION_RUNTIME_UPDATE_DATA=false;");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Report type. TXT,HTML,XML,JUNIT. Leave this property empty for disable creation of report files.");
		FileIO.appendStringToFile(file, "AUTOMATION_REPORT_TYPE=TXT,HTML,XML,JUNIT");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Default Result Viewer Application");
		FileIO.appendStringToFile(file, "AUTOMATION_RESULT_VIEWER_APP=");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Automatically open HTML Result log in your default browser when script completes");
		FileIO.appendStringToFile(file, "AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION=false");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#If directory is specified this will copy all result files to the specified alternate results folder");
		FileIO.appendStringToFile(file, "AUTOMATION_COPY_RESULTS_TO=");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Global Sleep (wait) values in seconds");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_1=1");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_2=2");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_5=5");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_10=10");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_15=15");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_30=30");
		FileIO.appendStringToFile(file, "AUTOMATION_WAIT_VALUE_60=60");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#Remote Server Properties for Scm class");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#Remote Server Name or IP Address");
		FileIO.appendStringToFile(file, "#gsRemoteServerName=10.221.221.116");
		FileIO.appendStringToFile(file, "AUTOMATION_SCM_REMOTE_SERVER_NAME=10.221.221.116");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Remote Server User Name");
		FileIO.appendStringToFile(file, "#gsRemoteServerUserName=pmtplus");
		FileIO.appendStringToFile(file, "AUTOMATION_SCM_REMOTE_SERVER_USERNAME=tonyv");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Remote Server Password (ebit/ebituat, pmtplus/r3d123)");
		FileIO.appendStringToFile(file, "#gsRemoteServerPwd=cjNkMTIz");
		FileIO.appendStringToFile(file, "AUTOMATION_SCM_REMOTE_SERVER_PASSWORD=cjNkMTIz");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Remote Server Port (Default value is 22)");
		FileIO.appendStringToFile(file, "#gsRemoteServerPort=22");
		FileIO.appendStringToFile(file, "AUTOMATION_SCM_REMOTE_SERVER_PORT=22");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#FTP Remote Server Properties for FTPUtil class");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#FTP Server Name or IP Address");
		FileIO.appendStringToFile(file, "#gsFTPServerName=10.32.136.134");
		FileIO.appendStringToFile(file, "AUTOMATION_FTP_SERVER_NAME=10.32.136.134");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#FTP Server User Name");
		FileIO.appendStringToFile(file, "#gsFTPServerUserName=aganaparthi");
		FileIO.appendStringToFile(file, "AUTOMATION_FTP_USERNAME=tonyv");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#FTP Server Password: T8dlz2ps");
		FileIO.appendStringToFile(file, "#gsFTPServerPwd=VDhkbHoycHM=");
		FileIO.appendStringToFile(file, "AUTOMATION_FTP_SERVER_PASSWORD=VDhkbHoycHM=");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#FTP Server Port (Default value is 21)");
		FileIO.appendStringToFile(file, "#gsFTPServerPort=21");
		FileIO.appendStringToFile(file, "AUTOMATION_FTP_SERVER_PORTAL=21");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#Database Server Properties for Database class");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#Database System Name (SQLServer)");
		FileIO.appendStringToFile(file, "# Valid options are : ORACLE or MYSQL or MSSQL or SQLSERVER or DB2 or SYBASE");
		FileIO.appendStringToFile(file, "#gsDBServerSystem=SQLSERVER");
		FileIO.appendStringToFile(file, "AUTOMATION_DATABASE_SERVER_SYSTEM=SQLSERVER");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Database Server Name or IP Address");
		FileIO.appendStringToFile(file, "#gsDBServerName=10.221.221.180");
		FileIO.appendStringToFile(file, "AUTOMATION_DATABASE_SERVER_NAME=10.221.221.180");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Database Server Instance Name");
		FileIO.appendStringToFile(file, "#gsDBServerInstance=LP560Test2");
		FileIO.appendStringToFile(file, "AUTOMATION_DATABASE_SERVER_INSTANCE_NAME=LP560Test2");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Database Server User Name");
		FileIO.appendStringToFile(file, "#gsDBServerUserName=LP560Test2");
		FileIO.appendStringToFile(file, "AUTOMATION_DATABASE_SERVER_USERNAME=tonyv");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Database Server Password (r3d123)");
		FileIO.appendStringToFile(file, "#gsDBServerPwd=cjNkMTIz");
		FileIO.appendStringToFile(file, "AUTOMATION_DATABASE_SERVER_PASSWORD=cjNkMTIz");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#Database Server Port ");
		FileIO.appendStringToFile(file, "#gsDBServerPort=1433");
		FileIO.appendStringToFile(file, "AUTOMATION_DATABASE_SERVER_PORT=1433");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#BrowserStack Properties");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_ENABLE=false");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_LICENSE_KEY=zKKmyPaATM8cMsg9pdoC");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_LICENSE_USER=tonyvenditti1");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_OS=Windows");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_OS_VERSION=7");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION=1024x768");
		FileIO.appendStringToFile(file, "AUTOMATION_BROWSERSTACK_BROWSER_VERSION=46.0");
		FileIO.appendStringToFile(file, "");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "#BandWidth limit Properties");
		FileIO.appendStringToFile(file, "#BANDWIDTH_LIMIT_READ and BANDWIDTH_LIMIT_WRITE in bytes per second ");
		FileIO.appendStringToFile(file, "#****************************************************");
		FileIO.appendStringToFile(file, "AUTOMATION_BANDWIDTH_LIMIT = false");
		FileIO.appendStringToFile(file, "AUTOMATION_BANDWIDTH_LIMIT_READ = 10000");
		FileIO.appendStringToFile(file, "AUTOMATION_BANDWIDTH_LIMIT_WRITE = 10000");
		FileIO.appendStringToFile(file, "");
		
				
		

	}

	/**
	 * Prints automation properties and values to the console
	 *
	 * @param p
	 *            Properties to print
	 */
	public static void printAutomationProperties(final Properties p) {
		printAutomationProperties(p, "Display Global Automation Properties and Settings");
	}

	/**
	 * Prints automation properties and values to the console
	 *
	 * @param p
	 *            the Properties to print
	 * @param desc
	 *            description of properties
	 */
	public static void printAutomationProperties(final Properties p, final String desc) {
		try {
			logBanner(desc);

			final Enumeration<?> e = p.propertyNames();

			while (e.hasMoreElements()) {
				final String key = (String) e.nextElement();
				System.out.println(key + "=" + p.getProperty(key));
			}

		} catch (final Exception e) {
			errorHandler("Error loading automation Property settings");
		}
	}

	/**
	 * Prints automation properties and values to the console
	 */
	public static void printAutomationProperties() {
		// Get machine specific global automation variables
		final File propFile = new File(AUTOMATION_DEFAULT_AUTOMATION_PROPERTIES_FILE);
		if (propFile.exists()) {

			try (FileInputStream in = new FileInputStream(AUTOMATION_DEFAULT_AUTOMATION_PROPERTIES_FILE)) {
				// load automation properties from properties file
				final Properties autoSettings = new Properties();
				autoSettings.load(in);

				printAutomationProperties(autoSettings,
						"Display Global Automation Properties and Settings from users automation.properties file or alternate");
			} catch (final Exception e) {
				errorHandler("Error loading automation Property settings");
			}
		}
	}

	/**
	 * Prints out the directory into which you should put the properties file
	 */
	public static void printDirForPropertiesFile() {
		System.out.println(getPropertiesDir());
	}

	/**
	 * Gets the directory into which you should put properties files
	 *
	 * @return String with properties directory path
	 */
	public static String getPropertiesDir() {
		return getPropertiesDir(AUTOMATION_DEFAULT_AUTOMATION_PROPERTIES_FILE);
	}

	/**
	 * Returns the directory of the specified properties file
	 *
	 * @param file
	 *            String of properties file to check for
	 * @return String with properties directory path
	 */
	public static String getPropertiesDir(String file) {
		final File f = new File(file);
		final String filename = f.getName();
		final String userhome = AUTOMATION_BASE_PATH + filename;
		final String autohome = AUTOMATION_TEST_PROJECT_PATH + filename;
		final String projhome = Platform.getCurrentProjectPath() + filename;
		final String altFile = getAlternateAutoPropDir() + filename;

		// Check given path for properties file.
		if (FileIO.fileExists(file) || FileIO.fileExists(file = userhome) || FileIO.fileExists(file = autohome)
				|| FileIO.fileExists(file = projhome)
				|| (!Strings.isEmpty(file = altFile) && FileIO.fileExists(altFile))) {
			return FileIO.getParentPath(file);
		}

		logScriptInfo("***************WARNING - Could NOT find property file.*********************");
		return Platform.getUserHome() + Platform.getFileSeparator(); // return
																		// default
																		// user
																		// home
																		// folder
	}

	/**
	 * Loads properties from a file. Properties are appended to the existing
	 * properties object.
	 * <p>
	 *
	 * @param p
	 *            properties to fill in
	 * @param file
	 *            file to read properties from
	 * @return Properties from specified file
	 * @throws IOException
	 *             error
	 */
	public static Properties loadFromFile(final Properties p, final File file) throws IOException {
		if (p == null) {
			return p;
		}

		try (FileInputStream fis = new FileInputStream(file)) {
			p.load(fis);
			return p;
		}
	}

	/**
	 * Loads properties from a file into a Property object
	 * <p>
	 *
	 * @param filePath
	 *            string path for file to read properties from
	 * @return Properties object containing all key=value pairings in the
	 *         specified file
	 */
	public static Properties loadFromFile(final String filePath) {
		return loadFromFile(new File(filePath));
	}

	/**
	 * Loads properties from a file into a Property object
	 * <p>
	 *
	 * @param file
	 *            file to read properties from
	 * @return Properties object containing all key=value pairings in the
	 *         specified file
	 */
	public static Properties loadFromFile(final File file) {
		final Properties p = new Properties();
		try (FileInputStream fis = new FileInputStream(file)) {
			p.load(fis);
			return p;
		} catch (final Exception e) {
			errorHandler("Error loading properties from file: " + String.valueOf(file), e);
			return null;
		}
	}

	/**
	 * Returns all properties from a given properties file
	 * <p>
	 *
	 * @param file
	 *            properties file name to load
	 * @return Properties containing all key=value pairings from the specified
	 *         properties file
	 */
	public static Properties getPropertiesFromFile(String file) {
		final Properties out = new Properties();
		boolean isFound = false;

		// find properties file
		if (FileIO.fileExists(file) || FileIO.fileExists(file = getPropertiesDir(file) + FileIO.stripPath(file))) {
			isFound = true;

			out.putAll(loadFromFile(file));
			if (file.toLowerCase().contains(AUTOMATION_PROPERTIES_FILE)) {
				AUTOMATION_PROPERTIES_FILE = file;
			} else {
				APPLICATION_PROPERTIES_FILE = file;
			}
		} else {
			logScriptInfo("***************WARNING - Could NOT find property file.*********************");
		}

		// If Script was executed from Silk Central - get automation properties
		// from Java system properties
		if (System.getProperty(SCTM_TEST_RESULTS_DIR) != null && System.getProperty(AUTOMATION_AUTO_PATH) != null
				|| System.getProperty(EXECUTION_TOOL) != null) { // check to see
																	// if script
																	// is
																	// running
																	// from
																	// another
																	// tool
			JAVA_SYSTEM_PROPERTIES = System.getProperties();
			out.putAll(JAVA_SYSTEM_PROPERTIES);
		} else {
			if (!isFound) {
				throw new SetupException("FATAL ERROR - property file does NOT exist.");
			}
		}

		// output properties
		return out;
	}

	/**
	 * Returns the specified property from the given properties file
	 * <p>
	 *
	 * @param key
	 *            Property Key for value to return
	 * @param fileName
	 *            properties file name to load
	 * @return String containing value of the specified key variable from the
	 *         specified properties file
	 */
	public static String getPropertyFromFile(final String key, final String fileName) {
		final Properties p = getPropertiesFromFile(fileName);
		return p.getProperty(key);
	}

	/**
	 * Returns the path and filename of the current, active
	 * automation.properties file
	 *
	 * @return String containing the name and path of the active
	 *         automation.properties file
	 */
	public static String getCurrentAutomationPropertiesFile() {
		return AUTOMATION_PROPERTIES_FILE;
	}

	/**
	 * Returns the path and filename of the current, application properties file
	 *
	 * @return String containing the name and path of the current application
	 *         properties file
	 */
	public static String getCurrentApplicationPropertiesFile() {
		return APPLICATION_PROPERTIES_FILE;
	}

	/**
	 * Sets/writes/changes the specified automation property (sKey) value (sVal)
	 * in the specified automation properties file at runtime - example usage:
	 * setAutomationProperty ("automation.properties", "gsAutomationResultPath",
	 * "c:\\myautodir\\");
	 * <p>
	 *
	 * @param propFileName
	 *            filename (name only - no path info) of the automation property
	 *            file you want to update
	 * @param key
	 *            the property to modify
	 * @param value
	 *            the new value to set for the above sKey property
	 * @return new value for specified property
	 */
	public static String setAutomationProperty(final String propFileName, final String key, final String value) {
		try {
			// Get system specific global variables
			if (System.getProperty(SCTM_TEST_RESULTS_DIR) != null && System.getProperty(AUTOMATION_AUTO_PATH) != null
					|| System.getProperty(EXECUTION_TOOL) != null) {
				System.setProperty(key, value);
				return System.getProperty(key);
			}

			// Append the path to the file name
			final String propFile = getPropertiesDir(propFileName) + FileIO.stripPath(propFileName);

			// Write new property in specified file
			FileIO.updatePropertyFile(propFile, key, value);
			return getPropertyFromFile(key, propFile);
		} catch (final Exception e) {
			errorHandler(String.format("Error setting \"%s\" property in %s property file", key, propFileName), e);
		}

		return "Property Not Found";
	}

	/**
	 * Returns String containing the current scripts path information
	 *
	 * @return String containing the current scripts path information
	 */
	public static String getTopScriptDir() {
		return Platform.getCurrentProjectPath() + File.separator;
	}

	/**
	 * Returns a string containing the automation datastore directory
	 *
	 * @return string containing the automation datastore directory
	 */
	public static String getDatastoreDir() {
		return Platform.getCurrentProjectPath() + Platform.getFileSeparator();
	}

	/**
	 * Returns a string containing the automation datastore parent directory
	 *
	 * @return string containing the automation datastore parent directory
	 */
	public static String getDatastoreParentDir() {
		return getDatastoreDir();
	}

	/**
	 * Returns a string containing the local host name of the test client system
	 *
	 * @return string containing the local host name of the test client system
	 */
	public static String getLocalClientName() {
		try {
			return java.net.InetAddress.getLocalHost().toString();
		} catch (final Exception e) {
			return "Could not get local host client name";
		}
	}

	/**
	 * Append stack trace after log content. This will be very helpful to locate
	 * hidden performance issue
	 *
	 * @return String with invocation code
	 */
	public static String getInvocationInfo() {
		// if not append log type, return empty string
		if (AUTOMATION_LOG_TYPE != LOGTYPE_APPEND_INVOCATION) {
			return "";
		}

		// get stack trace information
		final Map<?, ?> stackTraces = Thread.getAllStackTraces();
		final Set<?> keys = stackTraces.keySet();
		final Iterator<?> it = keys.iterator();
		Object key;

		final StringBuffer ivkInfo = new StringBuffer();

		// go throught
		while (it.hasNext()) {
			key = it.next();
			final StackTraceElement[] trace = (StackTraceElement[]) stackTraces.get(key);
			boolean found = false;
			String className;
			String methodName;
			for (final StackTraceElement element : trace) {
				methodName = element.getMethodName();
				className = element.getClassName();
				if (methodName.equals("getInvocationInfo")) {
					found = true;
				} else if (found && !className.startsWith("core")) {
					ivkInfo.append(" Trace[" + element + "]");
					break;
				}
			}
		}
		return ivkInfo.toString();
	}

	/**
	 * Initializes Automation properties using default values.
	 */
	private static Properties initializeDefaultAutomationProperties() {

		final Properties p = new Properties();

		// AUTOMATION_TEST_PROJECT_PATH =
		// FileIO.getParentPath(Platform.getCurrentProjectPath() +
		// AUTOMATION_SCRIPT_NAME.replace(".",
		// Platform.getFileSeparator()).replace("scripts", "")) +
		// Platform.getFileSeparator();
		AUTOMATION_TEST_PROJECT_PATH = Platform.getCurrentProjectPath();

		p.setProperty(AUTOMATION_AUTO_PATH, AUTOMATION_TEST_PROJECT_PATH);

		AUTOMATION_PROPERTIES_PATH = AUTOMATION_TEST_PROJECT_PATH + AUTOMATION_PROPERTIES;
		p.setProperty("AUTOMATION_PROPERTIES_PATH", AUTOMATION_PROPERTIES_PATH);

		AUTOMATION_TEST_RESULTS_PATH = AUTOMATION_TEST_PROJECT_PATH + "results" + Platform.getFileSeparator();
		p.setProperty("AUTOMATION_TEST_RESULTS_PATH", AUTOMATION_TEST_RESULTS_PATH);

		AUTOMATION_TEST_DATA_PATH = AUTOMATION_TEST_PROJECT_PATH + "testdata" + Platform.getFileSeparator();
		p.setProperty("AUTOMATION_TEST_DATA_PATH", AUTOMATION_TEST_DATA_PATH);

		AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX = ".jpg";
		p.setProperty("AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX", ".jpg");

		AUTOMATION_SPREADSHEET_SUFFIX = ".xls";
		p.setProperty("AUTOMATION_SPREADSHEET_SUFFIX", ".xls");

		AUTOMATION_CSV_EXT = ".csv";
		p.setProperty("AUTOMATION_CSV_EXT", ".csv");

		AUTOMATION_BROWSER_PROFILE = null;
		p.setProperty("AUTOMATION_BROWSER_PROFILE", "");

		AUTOMATION_BROWSER_PATH = null;
		p.setProperty("AUTOMATION_BROWSER_PATH", "");

		AUTOMATION_WAIT_VALUE_1 = 1;
		p.setProperty("AUTOMATION_WAIT_VALUE_1", "1");

		AUTOMATION_WAIT_VALUE_2 = 2;
		p.setProperty("AUTOMATION_WAIT_VALUE_2", "2");

		AUTOMATION_WAIT_VALUE_5 = 5;
		p.setProperty("AUTOMATION_WAIT_VALUE_5", "5");

		AUTOMATION_WAIT_VALUE_10 = 10;
		p.setProperty("AUTOMATION_WAIT_VALUE_10", "10");

		AUTOMATION_WAIT_VALUE_15 = 15;
		p.setProperty("AUTOMATION_WAIT_VALUE_15", "15");

		AUTOMATION_WAIT_VALUE_30 = 30;
		p.setProperty("AUTOMATION_WAIT_VALUE_30", "30");

		AUTOMATION_WAIT_VALUE_60 = 60;
		p.setProperty("AUTOMATION_WAIT_VALUE_60", "60");

		AUTOMATION_LOG_TYPE = LOGTYPE_TIME_PASS_FAIL;
		p.setProperty("AUTOMATION_LOG_TYPE", "1");

		AUTOMATION_IMAGE_CAPTURE = false;
		p.setProperty("AUTOMATION_IMAGE_CAPTURE", "false");

		AUTOMATION_BROWSER_CAPTURE = false;
		p.setProperty("AUTOMATION_BROWSER_CAPTURE", "false");

		AUTOMATION_CLEAR_RESULTS = false;
		p.setProperty("AUTOMATION_CLEAR_RESULTS", "false");

		AUTOMATION_LOCATOR_PRINT = false;
		p.setProperty("AUTOMATION_LOCATOR_PRINT", "false");

		AUTOMATION_JUNIT_FAIL_ENABLE = true;
		p.setProperty("AUTOMATION_JUNIT_FAIL_ENABLE", "true");

		AUTOMATION_RUNTIME_UPDATE_DATA = false;
		p.setProperty("AUTOMATION_RUNTIME_UPDATE_DATA", "false");

		AUTOMATION_STACK_TRACE = true;
		p.setProperty("AUTOMATION_STACK_TRACE", "true");

		AUTOMATION_ERROR_IMAGE_CAPTURE = true;
		p.setProperty("AUTOMATION_ERROR_IMAGE_CAPTURE", "true");

		AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION = false;
		p.setProperty("AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION", "false");

		AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION = true;
		p.setProperty("AUTOMATION_CLOSE_BROWSER_UPON_COMPLETION", "true");

		AUTOMATION_RESULT_VIEWER_APP = null;
		p.setProperty("AUTOMATION_RESULT_VIEWER_APP", "");
		
		AUTOMATION_COPY_RESULTS_TO = null;
		p.setProperty("AUTOMATION_COPY_RESULTS_TO", "");

		AUTOMATION_TEST_LEVEL = 2;
		p.setProperty("AUTOMATION_TEST_LEVEL", "2");

		AUTOMATION_REPORT_TYPE = "TXT,XML,JUNIT,HTML";
		p.setProperty("AUTOMATION_REPORT_TYPE", "TXT,XML,JUNIT,HTML");

		// BrowserStack settings
		AUTOMATION_BROWSERSTACK_ENABLE = false;
		p.setProperty("AUTOMATION_BROWSERSTACK_ENABLE", "false");

		AUTOMATION_BROWSERSTACK_LICENSE_KEY = "";
		p.setProperty("AUTOMATION_BROWSERSTACK_LICENSE_KEY", "");

		AUTOMATION_BROWSERSTACK_LICENSE_USER = "";
		p.setProperty("AUTOMATION_BROWSERSTACK_LICENSE_USER", "");

		AUTOMATION_BROWSERSTACK_OS = "Windows";
		p.setProperty("AUTOMATION_BROWSERSTACK_OS", "Windows");

		AUTOMATION_BROWSERSTACK_OS_VERSION = "7";
		p.setProperty("AUTOMATION_BROWSERSTACK_OS_VERSION", "7");

		AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION = "1024x768";
		p.setProperty("AUTOMATION_BROWSERSTACK_SCREEN_RESOLUTION", "1024x768");

		AUTOMATION_BROWSERSTACK_BROWSER_VERSION = "46.0";
		p.setProperty("AUTOMATION_BROWSERSTACK_BROWSER_VERSION", "46.0");

		// BandWidth limit settings
		AUTOMATION_BANDWIDTH_LIMIT = false;
		p.setProperty("AUTOMATION_BANDWIDTH_LIMIT", "false");
		AUTOMATION_BANDWIDTH_LIMIT_READ = 10000;
		p.setProperty("AUTOMATION_BANDWIDTH_LIMIT_READ", "10000");
		AUTOMATION_BANDWIDTH_LIMIT_WRITE = 10000;
		p.setProperty("AUTOMATION_BANDWIDTH_LIMIT_WRITE", "10000");
		return p;
	}

	/**
	 * Gets all Automation Properties from multiple sources and loads them into
	 * a Properties type
	 *
	 * @return all Automation Properties as Properties type
	 */
	public static Properties getAutomationProperties() {

		// Initialize default automation properties
		final Properties out = initializeDefaultAutomationProperties();

		// Load automation properties from automation.properties in the user
		// home directory, project directory or alternate directory
		out.putAll(getPropertiesFromFile(AUTOMATION_DEFAULT_AUTOMATION_PROPERTIES_FILE));

		return out;
	}

	/**
	 * Return report object.
	 *
	 * @return AutomationReport object
	 */
	public static AutomationReport getReport() {
		return report;
	}

	/**
	 * Prints to console skip info of skipped test case.
	 *
	 * @param skipComments
	 *            comments skipped test cases
	 */
	public static void logSkipTestCaseInfo(final String skipComments) {
		final DateFormat tmFormat = DateFormat.getTimeInstance();

		final String stepInfo = String.format("%s - Skip info - %s", tmFormat.format(new Date()), skipComments);
		System.out.println(stepInfo);
		report.logStep(stepInfo);
	}

	/**
	 * Returns current engine.
	 *
	 * @return the engine
	 */
	public static Engine getEngine() {
		return engine;
	}

	/**
	 * Set engine.
	 *
	 * @param engine
	 *            the engine to set
	 */
	public static void setEngine(final Engine engine) {
		Log.engine = engine;
		Log.engine.setEngine();
	}

	/**
	 * Returns the path to a resource file in the core framework
	 *
	 * @param resource
	 *            to get using forward slash as separator i.e.
	 *            "core/tools/autoit/MaximizeWindow.exe"
	 * @return full path for resource file i.e.
	 *         "c:\\automation\\trunk\\core\\tools\\autoit\\MaximizeWindow.exe"
	 */
	public static String getResource(final String resource) {
		final URL appPath = Log.class.getClassLoader().getResource(resource);
		final String s = Strings.replace(appPath.toString(), "file:/", "");
		final String x = Strings.replace(s, "/", Platform.getFileSeparator());
		logScriptInfo("Resource file: " + x);
		return x;
	}

	/**
	 * Specific error to define assertion errors occurring during the logging of
	 * exceptions when {@link Log#AUTOMATION_JUNIT_FAIL_ENABLE} is true.
	 */
	public static final class JUnitAssertionError extends AssertionError {
		private static final long serialVersionUID = 1;

		private JUnitAssertionError() {
			super();
		}

		private JUnitAssertionError(final String message) {
			super(message);
		}
	}
	
	
	
	
}
