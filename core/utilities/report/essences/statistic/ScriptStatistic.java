
package core.utilities.report.essences.statistic;

/**
 * The class represents methods for calculate script metrics.
 */
public class ScriptStatistic extends GroupStatistic {
	protected int executedGroupsCounter;
	protected int failedGroupsCounter;
	protected int warningGroupsCounter;

	/**
	 * Initializes a newly created {@link ScriptStatistic} object.
	 */
	public ScriptStatistic() {
		super();

		executedGroupsCounter = 0;
		failedGroupsCounter = 0;
		warningGroupsCounter = 0;
		actionsCounter = 0;
		errorsCounter = 0;
	}

	/**
	 * Increments test case groups counter.
	 */
	public void incrementExecutedGroupsCounter() {
		executedGroupsCounter++;
	}

	/**
	 * Gets executed groups counter.
	 *
	 * @return executed groups counter
	 */
	public int getExecutedGroupsCounter() {
		return executedGroupsCounter;
	}

	/**
	 * Gets failed groups counter.
	 *
	 * @return failed groups counter
	 */
	public int getFailedGroupsCounter() {
		return failedGroupsCounter;
	}

	/**
	 * Gets warning groups counter.
	 *
	 * @return warning groups counter
	 */
	public int getWarningGroupsCounter() {
		return warningGroupsCounter;
	}

	/**
	 * Gets passed groups counter.
	 *
	 * @return passed groups counter
	 */
	public int getPassedGroupsCounter() {
		return executedGroupsCounter > failedGroupsCounter
				? executedGroupsCounter - failedGroupsCounter - warningGroupsCounter : 0;
	}

	/**
	 * Calculates a script metrics.
	 *
	 * @param statistic
	 *            {@link GroupStatistic} object
	 */
	public void complete(GroupStatistic statistic) {
		if (statistic.getFailedTestCasesCounter() > 0
				|| (statistic.getExecutedTestCasesCounter() == 0 && statistic.getSkippedTestCasesCounter() == 0)) {
			failedGroupsCounter++;
		} else if (statistic.getFailedTestCasesCounter() == 0 && statistic.getWarningTestCasesCounter() > 0) {
			warningGroupsCounter++;
		}

		initializeTestCasesCounters(statistic);
	}

	protected void initializeTestCasesCounters(GroupStatistic statistic) {
		executedTestCasesCounter += statistic.getExecutedTestCasesCounter();
		failedTestCasesCounter += statistic.getFailedTestCasesCounter();
		warningTestCasesCounter += statistic.getWarningTestCasesCounter();
		skippedTestCasesCounter += statistic.getSkippedTestCasesCounter();
		actionsCounter += statistic.getActionsCounter();
		errorsCounter += statistic.getErrorsCounter();
	}

	/**
	 * Calculates passed groups percentage.
	 *
	 * @return passed groups percentage
	 */
	public String calculatePassedGroupsPercentage() {
		return calculatePercentage(getPassedGroupsCounter(), executedGroupsCounter);
	}

	/**
	 * Calculates warning groups percentage.
	 *
	 * @return warning groups percentage
	 */
	public String calculateWarningGroupsPercentage() {
		return calculatePercentage(warningGroupsCounter, executedGroupsCounter);
	}

	/**
	 * Calculates failed groups percentage.
	 *
	 * @return failed groups percentage
	 */
	public String calculateFailedGroupsPercentage() {
		return calculatePercentage(failedGroupsCounter, executedGroupsCounter);
	}
}
