
package core.utilities.report.essences.statistic;

/**
 * The class contains metrics for logging system like errorCounter and
 * testCaseCounter and appropriate methods for working with them.
 */
public class GroupStatistic {
	protected int executedTestCasesCounter;
	protected int failedTestCasesCounter;
	protected int warningTestCasesCounter;
	protected int skippedTestCasesCounter;
	protected int actionsCounter;
	protected int errorsCounter;

	/**
	 * Initializes a newly created {@link GroupStatistic} object.
	 */
	public GroupStatistic() {
		executedTestCasesCounter = 0;
		failedTestCasesCounter = 0;
		warningTestCasesCounter = 0;
		skippedTestCasesCounter = 0;
		actionsCounter = 0;
		errorsCounter = 0;
	}

	/**
	 * Gets failed test cases counter.
	 *
	 * @return failed test cases counter
	 */
	public int getFailedTestCasesCounter() {
		return failedTestCasesCounter;
	}

	/**
	 * Gets warning test cases counter.
	 *
	 * @return warning test cases counter
	 */
	public int getWarningTestCasesCounter() {
		return warningTestCasesCounter;
	}

	/**
	 * Gets skipped test cases counter.
	 *
	 * @return skipped test cases counter
	 */
	public int getSkippedTestCasesCounter() {
		return skippedTestCasesCounter;
	}

	/**
	 * Gets actions counter.
	 *
	 * @return actions counter
	 */
	public int getActionsCounter() {
		return actionsCounter;
	}

	/**
	 * Gets errors counter.
	 *
	 * @return errors counter
	 */
	public int getErrorsCounter() {
		return errorsCounter;
	}

	/**
	 * Increments failed test cases counter.
	 */
	public void incrementFailedTestCasesCounter() {
		failedTestCasesCounter++;
	}

	/**
	 * Increments warning test cases counter.
	 */
	public void incrementWarningTestCasesCounter() {
		warningTestCasesCounter++;
	}

	/**
	 * Increments skipped test cases counter.
	 */
	public void incrementSkippedTestCasesCounter() {
		skippedTestCasesCounter++;
	}

	/**
	 * Increments actions counter.
	 */
	public void incrementActionsCounter() {
		actionsCounter++;
	}

	/**
	 * Increments errors counter.
	 */
	public void incrementErrorsCounter() {
		errorsCounter++;
	}

	/**
	 * Calculates failed test cases percentage.
	 *
	 * @return failed test cases percentage
	 */
	public String calculateFailedTestCasesPercentage() {
		return calculatePercentage(failedTestCasesCounter, executedTestCasesCounter - skippedTestCasesCounter);
	}

	/**
	 * Calculates warning test cases percentage.
	 *
	 * @return failed test cases percentage
	 */
	public String calculateWarningTestCasesPercentage() {
		return calculatePercentage(warningTestCasesCounter, executedTestCasesCounter - skippedTestCasesCounter);
	}

	/**
	 * Gets passed test cases counter.
	 *
	 * @return passed test cases counter
	 */
	public int getPassedTestCasesCounter() {
		return executedTestCasesCounter > failedTestCasesCounter
				? executedTestCasesCounter - skippedTestCasesCounter - failedTestCasesCounter - warningTestCasesCounter
				: 0;
	}

	/**
	 * Calculates passed test cases percentage.
	 *
	 * @return passed test cases percentage
	 */
	public String calculatePassedTestCasesPercentage() {
		return calculatePercentage(getPassedTestCasesCounter(), executedTestCasesCounter - skippedTestCasesCounter);
	}

	/**
	 * Gets test cases counter.
	 *
	 * @return test cases counter
	 */
	public int getExecutedTestCasesCounter() {
		return executedTestCasesCounter;
	}

	/**
	 * Increments test cases counter.
	 */
	public void incrementExecutedTestCasesCounter() {
		executedTestCasesCounter++;
	}

	/**
	 * Calculates specific percentage.
	 *
	 * @param specificCounter
	 *            counter of specific tests
	 * @param totalCounter
	 *            counter of executed tests
	 * @return percentage value
	 */
	protected String calculatePercentage(int specificCounter, int totalCounter) {
		return String.format("%.2f", totalCounter == 0 ? 0 : (float) specificCounter / totalCounter * 100);
	}

}
