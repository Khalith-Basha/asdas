
package core.utilities.report;

import core.utilities.report.essences.Group;
import core.utilities.report.essences.Script;
import core.utilities.report.essences.Suite;

/**
 * The class represents collection of log messages templates.
 */
public class LogTemplates {
	/**
	 * Generates number message for {@link TestStatuses#EXECUTED} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testCounter
	 *            number of corresponding test
	 * @return result message
	 */
	public static String generateExecutedTestNumber(String testType, int testCounter) {
		return generateTestNumber(TestStatuses.EXECUTED, testType, testCounter);
	}

	/**
	 * Generates number message for {@link TestStatuses#PASSED} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testCounter
	 *            number of corresponding test
	 * @return result message
	 */
	public static String generatePassedTestNumber(String testType, int testCounter) {
		return generateTestNumber(TestStatuses.PASSED, testType, testCounter);
	}

	/**
	 * Generates number message for {@link TestStatuses#WARNING} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testCounter
	 *            number of corresponding test
	 * @return result message
	 */
	public static String generateWarningTestNumber(String testType, int testCounter) {
		return generateTestNumber(TestStatuses.WARNING, testType, testCounter);
	}

	/**
	 * Generates number message for {@link TestStatuses#SKIPPED} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testCounter
	 *            number of corresponding test
	 * @return result message
	 */
	public static String generateSkippedTestNumber(String testType, int testCounter) {
		return generateTestNumber(TestStatuses.SKIPPED, testType, testCounter);
	}

	/**
	 * Generates number message for {@link TestStatuses#FAILED} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testCounter
	 *            number of corresponding test
	 * @return result message
	 */
	public static String generateFailedTestNumber(String testType, int testCounter) {
		return generateTestNumber(TestStatuses.FAILED, testType, testCounter);
	}

	/**
	 * Generates number message.
	 *
	 * @param testStatus
	 *            status of test. For example: executed, passed, failed.
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testCounter
	 *            number of corresponding test
	 * @return result message
	 */
	private static String generateTestNumber(String testStatus, String testType, int testCounter) {
		String resultMessage = "";

		if (testType.equals("Scripts") || testType.equals("Suites") || testType.equals("Groups")) {
			resultMessage = String.format("* Total %s %s:", testType, testStatus);
		} else {
			resultMessage = String.format("* %s %s:", testType, testStatus);
		}

		return String.format("%-41s %s%s", resultMessage, testCounter, System.lineSeparator());
	}

	/**
	 * Generates percentage message for {@link TestStatuses#PASSED} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testPercentage
	 *            percentage of passed tests
	 * @return result message
	 */
	public static String generatePassedTestPercentage(String testType, String testPercentage) {
		return generateTestPercentage(TestStatuses.PASSED, testType, testPercentage);
	}

	/**
	 * Generates percentage message for {@link TestStatuses#WARNING} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testPercentage
	 *            percentage of warning tests
	 * @return result message
	 */
	public static String generateWarningTestPercentage(String testType, String testPercentage) {
		return generateTestPercentage(TestStatuses.WARNING, testType, testPercentage);
	}

	/**
	 * Generates percentage message for {@link TestStatuses#FAILED} test.
	 *
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testPercentage
	 *            percentage of failed test
	 * @return result message
	 */
	public static String generateFailedTestPercentage(String testType, String testPercentage) {
		return generateTestPercentage(TestStatuses.FAILED, testType, testPercentage);
	}

	/**
	 * Generates test percentage message.
	 *
	 * @param testStatus
	 *            status of test. For example: executed, passed, failed.
	 * @param testType
	 *            type of test. For example: test cases or scripts.
	 * @param testPercentage
	 *            percentage of corresponding test
	 * @return test percentage message
	 */
	private static String generateTestPercentage(String testStatus, String testType, String testPercentage) {
		String percentageTamplate = "";

		if (testType.equals("Scripts") || testType.equals("Suites")) {
			percentageTamplate = String.format("* Total Percent %s %s:", testType, testStatus);
		} else if (testType.equals("Total Testcases")) {
			percentageTamplate = String.format("* Total Percent Testcases %s:", testStatus);
		} else {
			percentageTamplate = String.format("* Percent %s %s:", testType, testStatus);
		}

		return String.format("%-41s %s%%%s", percentageTamplate, testPercentage, System.lineSeparator());
	}

	/**
	 * Generates message actions count.
	 *
	 * @param actionsCounter
	 *            number of actions
	 * @param isSuiteRunning
	 *            true if suite is running
	 * @return result message
	 */
	public static String generateActionsNumber(int actionsCounter, Boolean isSuiteRunning) {
		String text = String.format("* %sNumber of Test Actions Executed:", isSuiteRunning ? "Total " : "");
		return String.format("%-41s %s%s", text, actionsCounter, System.lineSeparator());
	}

	/**
	 * Generates message errors count.
	 *
	 * @param errorsCounter
	 *            number of errors
	 * @param isSuiteRunning
	 *            true if suite is running
	 * @return result message
	 */
	public static String generateErrorsNumber(int errorsCounter, Boolean isSuiteRunning) {
		String text = String.format("* %sNumber of Errors found:", isSuiteRunning ? "Total " : "");
		return String.format("%-41s %s%s", text, errorsCounter, System.lineSeparator());
	}

	/**
	 * Generates message for end date and time.
	 *
	 * @param timer
	 *            {@link Timer} object
	 * @return ready end date and time message
	 */
	public static String generateEndDateAndTime(Timer timer) {
		String text = "* End Date and Time:";
		return String.format("%-41s %s%s", text, timer.getDatestamp(), System.lineSeparator());
	}

	/**
	 * Generates message for elapsed time.
	 *
	 * @param timer
	 *            {@link Timer} object
	 * @param total
	 *            if true add "Total" word before string
	 * @return ready elapsed time message
	 */
	public static String generateElapsedTime(Timer timer, Boolean total) {
		String text = String.format("* %sElapsed Time:", (total) ? "Total " : "");
		return String.format("%-41s %s", text, timer.getElapsedTime());
	}

	/**
	 * Generates result title for {@link Script}, {@link Suite}, or
	 * {@link Group}.
	 *
	 * @param testEssenceType
	 *            For example: "test case group" or "suite"
	 * @param testEssenceName
	 *            For example: Login or PositivePays
	 * @return result title
	 */
	public static String generateResultTitle(String testEssenceType, String testEssenceName) {
		return String.format("%s: %s", testEssenceType, testEssenceName);
	}
}
