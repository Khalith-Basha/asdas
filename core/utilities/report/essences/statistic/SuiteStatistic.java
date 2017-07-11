
package core.utilities.report.essences.statistic;

/**
 * The class represents methods for calculate suite metrics.
 */
public class SuiteStatistic extends ScriptStatistic {
	private int executedSuitesCounter;
	private int failedSuitesCounter;
	private int warningSuitesCounter;

	private int executedScriptsCounter;
	private int failedScriptsCounter;
	private int warningScriptsCounter;

	/**
	 * Initializes a newly created {@link SuiteStatistic} object.
	 *
	 * @param suiteName
	 *            suite name
	 */
	public SuiteStatistic(String suiteName) {
		super();

		executedSuitesCounter = 0;
		failedSuitesCounter = 0;
		warningSuitesCounter = 0;

		executedScriptsCounter = 0;
		failedScriptsCounter = 0;
		warningScriptsCounter = 0;
	}

	/**
	 * Gets suites counter.
	 *
	 * @return suites counter
	 */
	public int getExecutedSuitesCounter() {
		return executedSuitesCounter;
	}

	/**
	 * Increments Suites counter.
	 */
	public void incrementExecutedSuitesCounter() {
		executedSuitesCounter++;
	}

	/**
	 * Increments failed Suites counter.
	 */
	public void incrementFailedSuitesCounter() {
		failedSuitesCounter++;
	}

	/**
	 * Increments warning scripts counter.
	 */
	public void incrementWarningSuitesCounter() {
		warningSuitesCounter++;
	}

	/**
	 * Gets failed suites counter.
	 *
	 * @return failed suites counter
	 */
	public int getFailedSuitesCounter() {
		return failedSuitesCounter;
	}

	/**
	 * Gets warning suites counter.
	 *
	 * @return warning suites counter
	 */
	public int getWarningSuitesCounter() {
		return warningSuitesCounter;
	}

	/**
	 * Gets scripts counter.
	 *
	 * @return scripts counter
	 */
	public int getExecutedScriptsCounter() {
		return executedScriptsCounter;
	}

	/**
	 * Increments script counter.
	 */
	public void incrementExecutedScriptsCounter() {
		executedScriptsCounter++;
	}

	/**
	 * Increments failed scripts counter.
	 */
	public void incrementFailedScriptsCounter() {
		failedScriptsCounter++;
	}

	/**
	 * Increments warning scripts counter.
	 */
	public void incrementWarningScriptsCounter() {
		warningScriptsCounter++;
	}

	/**
	 * Gets warning scripts counters
	 *
	 * @return warning scripts counters
	 */
	public int getWarningScriptsCounter() {
		return warningScriptsCounter;
	}

	/**
	 * Gets failed scripts counters
	 *
	 * @return failed scripts counters
	 */
	public int getFailedScriptsCounter() {
		return failedScriptsCounter;
	}

	/**
	 * Gets passed suites counter.
	 *
	 * @return passed suites counter
	 */
	public int getPassedSuitesCounter() {
		return executedSuitesCounter - failedSuitesCounter - warningSuitesCounter;
	}

	/**
	 * Gets passed scripts counter.
	 *
	 * @return passed scripts counter
	 */
	public int getPassedScriptsCounter() {
		return executedScriptsCounter - failedScriptsCounter - warningScriptsCounter;
	}

	/**
	 * Calculates passed scripts percentage.
	 *
	 * @return passed scripts percentage
	 */
	public String calculatePassedScriptsPercentage() {
		return calculatePercentage(getPassedScriptsCounter(), executedScriptsCounter);
	}

	/**
	 * Calculates passed suites percentage.
	 *
	 * @return passed suites percentage
	 */
	public String calculatePassedSuitesPercentage() {
		return calculatePercentage(getPassedSuitesCounter(), executedSuitesCounter);
	}

	/**
	 * Calculates warning scripts percentage.
	 *
	 * @return warning scripts percentage
	 */
	public String calculateWarningScriptsPercentage() {
		return calculatePercentage(getWarningScriptsCounter(), executedScriptsCounter);
	}

	/**
	 * Calculates warning suites percentage.
	 *
	 * @return warning suites percentage
	 */
	public String calculateWarningSuitesPercentage() {
		return calculatePercentage(getWarningSuitesCounter(), executedSuitesCounter);
	}

	/**
	 * Calculates failed suites percentage.
	 *
	 * @return failed suites percentage
	 */
	public String calculateFailedSuitesPercentage() {
		return calculatePercentage(failedSuitesCounter, executedSuitesCounter);
	}

	/**
	 * Calculates failed scripts percentage.
	 *
	 * @return failed scripts percentage
	 */
	public String calculateFailedScriptsPercentage() {
		return calculatePercentage(failedScriptsCounter, executedScriptsCounter);
	}

	/**
	 * Calculates a suite metrics.
	 *
	 * @param scriptStatistic
	 *            {@link ScriptStatistic} object
	 */
	public void complete(ScriptStatistic scriptStatistic) {

		if ((failedGroupsCounter > 0) || (scriptStatistic.executedTestCasesCounter == 0 && executedGroupsCounter == 0
				&& getPassedScriptsCounter() == 0)) {
			failedScriptsCounter++;
		} else if (failedGroupsCounter == 0 && warningGroupsCounter > 0) {
			warningScriptsCounter++;
		}

		if (scriptStatistic instanceof SuiteStatistic) {
			if ((((SuiteStatistic) scriptStatistic).getFailedScriptsCounter() > 0)
					|| ((SuiteStatistic) scriptStatistic).getExecutedScriptsCounter() == 0) {
				failedSuitesCounter++;
			} else if (failedScriptsCounter == 0 && warningScriptsCounter > 0) {
				warningSuitesCounter++;
			}

			initializeSuitesCounters((SuiteStatistic) scriptStatistic);
		}

		initializeTestCasesCounters(scriptStatistic);

		executedGroupsCounter += scriptStatistic.getExecutedGroupsCounter();
		failedGroupsCounter += scriptStatistic.getFailedGroupsCounter();
		warningGroupsCounter += scriptStatistic.getWarningGroupsCounter();

		if (scriptStatistic instanceof SuiteStatistic) {
			executedScriptsCounter += ((SuiteStatistic) scriptStatistic).getExecutedScriptsCounter();
			failedScriptsCounter += ((SuiteStatistic) scriptStatistic).getFailedScriptsCounter();
			warningScriptsCounter += ((SuiteStatistic) scriptStatistic).getWarningScriptsCounter();
		}
	}

	public void initializeSuitesCounters(SuiteStatistic statistic) {
		executedSuitesCounter += statistic.getExecutedSuitesCounter();
		failedSuitesCounter += statistic.getFailedSuitesCounter();
		warningSuitesCounter += statistic.getWarningSuitesCounter();
	}

}
