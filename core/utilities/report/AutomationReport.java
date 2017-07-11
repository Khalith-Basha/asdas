
package core.utilities.report;

import java.awt.Desktop;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import core.utilities.DateTime;
import core.utilities.Enums.Symbol;
import core.utilities.Images;
import core.utilities.Log;
import core.utilities.exceptions.SetupException;
import core.utilities.report.XMLReport.Tags;
import core.utilities.report.essences.Group;
import core.utilities.report.essences.Script;
import core.utilities.report.essences.Suite;
import core.utilities.report.essences.TestCase;

public class AutomationReport {
	private static XMLReport xmlReport;
	private static HTMLReport htmlReport;
	private static TXTReport txtReport;

	private static JUnitReport junitReport;
	private static ArrayDeque<Suite> suite = new ArrayDeque<>();
	private Script script = null;
	private Group group = null;
	private TestCase testCase = null;
	private Suite currentSuite = null;

	public enum Section {
		@Symbol("BeforeTest")
		BEFORETEST, @Symbol("AfterTest")
		AFTERTEST, @Symbol("CURRENT")
		CURRENT;
	}

	public AutomationReport(String fileName) {
		List<String> reports = Arrays.asList(Log.AUTOMATION_REPORT_TYPE.replaceAll("\\s", "").split(","));

		if (reports.contains("XML") && xmlReport == null) {
			xmlReport = new XMLReport(fileName);
		}
		if (reports.contains("HTML") && htmlReport == null) {
			htmlReport = new HTMLReport(fileName);
		}
		if (reports.contains("JUNIT") && junitReport == null) {
			junitReport = new JUnitReport(fileName);
		}
		if (reports.contains("TXT") && txtReport == null) {
			txtReport = new TXTReport(fileName);
		}
	}

	public static XMLReport getXmlReport() {
		return xmlReport;
	}

	public static HTMLReport getHtmlReport() {
		return htmlReport;
	}

	public static TXTReport getTxtReport() {
		return txtReport;
	}

	/**
	 * Configures logging system to run the test case.
	 *
	 * @param description
	 *            is information of test case to log starting test case
	 */
	public void startTestCase(String description) {
		if (testCase != null) {
			finishTestCase();
		}

		testCase = new TestCase((group != null) ? group : script, description);

		if (junitReport != null) {
			junitReport.startTest(description);
		}
	}



	/**
	 * Calculates statistic and outputs the script result.
	 */
	public void finishTestCase() {
		testCase.finish();
		if (junitReport != null) {
			junitReport.stopTest(testCase.getTimer().getElapsedTimeInSeconds());
		}
		testCase = null;
	}

	/**
	 * Configures logging system to run the group.
	 *
	 * @param description
	 *            name of the group
	 */
	public void startGroup(String description) {
		group = new Group(script, description);
	}

	/**
	 * Configures logging system to run the group.
	 */
	public void finishGroup() {
		if (testCase != null) {
			finishTestCase();
		}

		group.finish();
		group = null;
	}

	/**
	 * Configures logging system to run the script.
	 *
	 * @param scriptPath
	 *            always:
	 *            Thread.currentThread().getStackTrace()[1].getClassName()
	 */
	public void startScript(String scriptPath) {
		script = new Script(currentSuite, scriptPath);
		if (junitReport != null) {
			if (junitReport.getTestSuite() == null) {
				junitReport.testSuite(scriptPath);
			}
		}
	}

	/**
	 * Calculates statistic, outputs the script result and generates the html
	 * file.
	 */
	public void finishScript() {
		if (testCase != null) {
			finishTestCase();
		}

		if (group != null) {
			group.finish();
			group = null;
		}

		if (script != null) {
			if ((script.getStatistic().getFailedGroupsCounter() > 0
					|| (script.getStatistic().getExecutedGroupsCounter() == 0
							&& script.getStatistic().getFailedTestCasesCounter() > 0))) {
				script.setFailed();
			}

			if (currentSuite == null) {
				if (junitReport != null) {
					junitReport.finishSuite(script.getTimer().getElapsedTimeInSeconds());
				}
			}
			script.finish();
		}
	}

	/**
	 * Generates statistic of master suite.
	 */
	public void generateStatistic() {
		Log.logScriptInfo("******************************************************************************",
				Log.LOGTYPE_CONSOLE);
		Log.logScriptInfo(
				currentSuite.generateResultMessage() + LogTemplates.generateEndDateAndTime(currentSuite.getTimer())
						+ LogTemplates.generateElapsedTime(currentSuite.getTimer(), true),
				Log.LOGTYPE_CONSOLE);
		Log.logScriptInfo("******************************************************************************",
				Log.LOGTYPE_CONSOLE);
	}

	public void updateCurrentSuite() {
		if (currentSuite.hasParentSuite()) {
			currentSuite = suite.getLast();
			suite.removeLast();
		}
	}

	/**
	 * Configures logging system to run the script.
	 *
	 * @param suitePath
	 *            always:
	 *            Thread.currentThread().getStackTrace()[1].getClassName()
	 */
	public void startSuite(String suitePath) {
		if (currentSuite == null) {
			currentSuite = new Suite(suitePath);
			if (junitReport != null) {
				junitReport.testSuite(suitePath);
			}
		} else {
			suite.add(currentSuite);
			currentSuite = new Suite(suitePath, currentSuite);
			if (junitReport != null) {
				junitReport.testSuite(suitePath, junitReport.getTestSuite());
			}
		}

	}

	/**
	 * Completes execution of suite.
	 */
	public void finishSuite() {
		if (script != null) {
			script = null;
		}
		if (junitReport != null) {
			junitReport.finishSuite(currentSuite.getTimer().getElapsedTimeInSeconds());
		}
		currentSuite.finish();
	}

	/**
	 * Checks whether test case is started.
	 *
	 * @return true if test case is running
	 */
	public boolean isTestCaseStarted() {
		return testCase != null;
	}

	/**
	 * Checks whether group is started.
	 *
	 * @return true if group is running
	 */
	public boolean isGroupStarted() {
		return group != null;
	}

	/**
	 * Checks whether script is started.
	 *
	 * @return true if script is running
	 */
	public boolean isScriptStarted() {
		return script != null;
	}

	/**
	 * Logs each passed iteration of test case.
	 *
	 * @param actionDescription
	 *            description of the action
	 */
	public void logStep(String actionDescription) {
		if (htmlReport != null) {
			htmlReport.updateLastStep(actionDescription);
		}

		if (!actionDescription.isEmpty()) {
			String fileName = null;

			if ((Log.AUTOMATION_IMAGE_CAPTURE && actionDescription.contains(Log.AUTOMATION_PASS_MARKER))
					|| actionDescription.contains(Log.AUTOMATION_IMAGE_MARKER)) {
				fileName = Log.AUTOMATION_TEST_RESULTS_PATH + Log.AUTOMATION_SCRIPT_NAME + "_"
						+ DateTime.getFormattedDateTime(new Date().getTime(), "MMddHHmmssSSS")
						+ Log.AUTOMATION_RESULT_ERROR_IMAGE_SUFFIX;
				if (!actionDescription.contains(Log.AUTOMATION_IMAGE_MARKER)) {
					if (Log.getEngine() == null || !Images.captureScreen(fileName, true)) {
						fileName = null;

						if (Log.getEngine() != null) {
							Log.logScriptInfo("Error in capturing of desktop/browser/device image",
									Log.LOGTYPE_ERROR_OUTPUT);
						}
					}
				} else {
					Matcher matcher = Pattern.compile(Log.AUTOMATION_IMAGE_MARKER + "(.*)").matcher(actionDescription);

					if (matcher.find()) {
						fileName = matcher.group(1).trim();
					}
				}
			}

			Section section = Section.CURRENT;

			if (isBefore() || isBeforeClass()) {
				section = Section.BEFORETEST;
			} else if (isAfter() || isAfterClass()) {
				section = Section.AFTERTEST;
			}

			if (txtReport != null) {
				txtReport.addStep(actionDescription);
			}

			if (testCase != null) {
				if (xmlReport != null) {
					xmlReport.addStep(testCase.getId(), actionDescription, fileName);
				}
				if (htmlReport != null) {
					htmlReport.addStep(testCase.getId(), actionDescription, fileName, section, Tags.TEST_CASE);
				}
				if (group != null) {
					group.getStatistic().incrementActionsCounter();
				} else if (script != null) {
					script.getStatistic().incrementActionsCounter();
				} else if (currentSuite != null) {
					currentSuite.getStatistic().incrementActionsCounter();
				}
			} else if (group != null) {
				if (xmlReport != null) {
					xmlReport.addStep(group.getId(), actionDescription, fileName);
				}
				if (htmlReport != null) {
					htmlReport.addStep(group.getId(), actionDescription, fileName, section, Tags.GROUP);
				}
			} else if (script != null) {
				if (xmlReport != null) {
					xmlReport.addStep(script.getId(), actionDescription, fileName);
				}
				if (htmlReport != null) {
					htmlReport.addStep(script.getId(), actionDescription, fileName, section, Tags.SCRIPT);
				}
			} else if (suite != null) {
				if (xmlReport != null) {
					xmlReport.addStep(currentSuite.getId(), actionDescription, fileName);
				}
				if (htmlReport != null) {
					htmlReport.addStep(currentSuite.getId(), actionDescription, fileName, section, Tags.SUITE);
				}
			}
		}
	}

	private boolean isBefore() {
		return isAnnotationPresent(BeforeTest.class);
	}

	private boolean isBeforeClass() {
		return isAnnotationPresent(BeforeClass.class);
	}

	private boolean isAfter() {
		return isAnnotationPresent(AfterTest.class);
	}

	private boolean isAfterClass() {
		return isAnnotationPresent(AfterClass.class);
	}

	@SuppressWarnings({"rawtypes" })
	private boolean isAnnotationPresent(Class cls) {
		
		return false;
		
	}

	/**
	 * Handles errors.
	 */
	public void handleError() {
		if (testCase != null && !testCase.isFailed()) {
			testCase.setFailed();
		}

		if (group != null) {
			group.getStatistic().incrementActionsCounter();
			group.getStatistic().incrementErrorsCounter();
		} else if (script != null) {
			script.getStatistic().incrementActionsCounter();
			script.getStatistic().incrementErrorsCounter();
		} else if (currentSuite != null) {
			currentSuite.getStatistic().incrementActionsCounter();
			currentSuite.getStatistic().incrementErrorsCounter();
		}
	}

	/**
	 * Handles errors.
	 *
	 * @param exception
	 *            error info
	 * @param description
	 *            The text you want to write out
	 */
	public void handleJUnitReportError(Exception exception, String description) {
		if (junitReport != null) {
			junitReport.setSetFail(true);
			if (exception != null) {
				junitReport.setException(exception);
			}
			junitReport.setErrorMessage(description);
		}
	}

	/**
	 * Sets current test case as skipped.
	 */
	public void skipTestCase() {
		if (testCase != null) {
			testCase.setSkipped();
		}
	}

	/**
	 * Checks if current suite is master suite(top suite that have child
	 * suites).
	 *
	 * @return true - if current suite have suites.
	 */
	public Boolean isMasterSuite() {
		return !currentSuite.hasParentSuite();
	}

	/**
	 * Getter for current suite.
	 *
	 * @return current suite
	 */
	public Suite getCurrentSuite() {
		return currentSuite;
	}

	/**
	 * Opens HTML report in default browser.
	 */
	public void openHTMLReport() {
		if (htmlReport != null) {
			if (Log.AUTOMATION_RESULT_VIEWER_APP != null) {
				try {
					final String[] lsViewer = new String[] { Log.AUTOMATION_RESULT_VIEWER_APP,
							htmlReport.getHTMLReportFile().getPath() };
					Runtime.getRuntime().exec(lsViewer);
				} catch (final Exception e) {
					System.err.println("Error loading alternative Result Viewer application");
					e.printStackTrace();

				}
			}

			if (Log.AUTOMATION_OPEN_RESULT_REPORT_AFTER_SCRIPT_COMPLETION) {
				try {
					Desktop.getDesktop().browse(htmlReport.getHTMLReportFile().toURI());
				} catch (final Exception e) {
					throw new SetupException("Error loading default browser.", e);
				}
			}
		}
	}
}
