
package core.utilities.report.essences;

import core.utilities.Log;
import core.utilities.report.AutomationReport;
import core.utilities.report.LogTemplates;
import core.utilities.report.TestStatuses;
import core.utilities.report.Timer;
import core.utilities.report.XMLReport.Tags;
import core.utilities.report.essences.common.AbstractScenario;
import core.utilities.report.essences.statistic.ScriptStatistic;

/**
 * The class represents the essence of the script and contains all the necessary
 * methods for logging.
 */
public final class Script extends AbstractScenario {
	private final Suite suite;
	private final ScriptStatistic statistic;
	private boolean isFailed;

	/**
	 * Initializes a newly created {@link Script} object.
	 *
	 * @param suite
	 *            suite script
	 * @param scriptPath
	 *            path of script
	 */
	public Script(Suite suite, String scriptPath) {
		super(scriptPath);

		statistic = new ScriptStatistic();

		this.suite = suite;
		isFailed = false;

		if (Log.isSuite) {
			suite.getStatistic().incrementExecutedScriptsCounter();
		}

		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addElement(Log.isSuite ? suite.getId() : Tags.ROOT, Tags.SCRIPT, fileName,
					id);
		}
		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().setTitle(resultHeader);
			AutomationReport.getHtmlReport().addMenu(Log.isSuite ? suite.getId() : null, Tags.SCRIPT, fileName, id);
			AutomationReport.getHtmlReport().addStatisticContainer(id);
		}
	}

	/**
	 * Generates result message.
	 *
	 * @param statistic
	 *            {@link ScriptStatistic} object
	 * @param isSuiteRunning
	 *            true if suite is running
	 * @return result message
	 */
	public static String generateResultMessage(ScriptStatistic statistic, Boolean isSuiteRunning) {
		String scriptStatistic = "";

		if (statistic.getExecutedGroupsCounter() != 0) {
			final String typeMessage = "Groups";

			scriptStatistic = LogTemplates.generateExecutedTestNumber(typeMessage,
					statistic.getExecutedGroupsCounter());
			scriptStatistic += LogTemplates.generatePassedTestNumber(typeMessage, statistic.getPassedGroupsCounter());

			if (statistic.getWarningGroupsCounter() > 0) {
				scriptStatistic += LogTemplates.generateWarningTestNumber(typeMessage,
						statistic.getWarningGroupsCounter());
			}
			scriptStatistic += LogTemplates.generateFailedTestNumber(typeMessage, statistic.getFailedGroupsCounter());

			scriptStatistic += LogTemplates.generatePassedTestPercentage(typeMessage,
					statistic.calculatePassedGroupsPercentage());
			if (statistic.getWarningGroupsCounter() > 0) {
				scriptStatistic += LogTemplates.generateWarningTestPercentage(typeMessage,
						statistic.calculateWarningGroupsPercentage());
			}
			scriptStatistic += LogTemplates.generateFailedTestPercentage(typeMessage,
					statistic.calculateFailedGroupsPercentage());
		}

		return scriptStatistic += Group.generateResultMessage(statistic, isSuiteRunning);
	}

	/**
	 * Gets statistic.
	 *
	 * @return {@link ScriptStatistic} object
	 */
	public ScriptStatistic getStatistic() {
		return statistic;
	}

	/**
	 * Checks whether script is failed.
	 *
	 * @return true if it's failed
	 */
	public boolean isFailed() {
		return isFailed;
	}

	/**
	 * Sets script as failed.
	 */
	public void setFailed() {
		if (!isFailed) {
			isFailed = true;

			if (Log.isSuite) {
				suite.getStatistic().incrementFailedScriptsCounter();
			}
		}
	}

	@Override
	protected void complete() {
		if (suite != null) {
			suite.getStatistic().complete(statistic);
		}

		if (isFailed || (statistic.getExecutedGroupsCounter() == 0 && statistic.getExecutedTestCasesCounter() == 0)
				|| statistic.getFailedGroupsCounter() > 0) {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addFailStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.FAIL);
			}
		} else if (statistic.getFailedGroupsCounter() == 0
				&& (statistic.getWarningGroupsCounter() > 0 || statistic.getWarningTestCasesCounter() > 0)) {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addWarningStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.WARNING);
			}
		} else {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addPassStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.PASS);
			}
		}

		String banerStatistic = generateResultMessage(statistic, false) + LogTemplates.generateEndDateAndTime(timer)
				+ LogTemplates.generateElapsedTime(timer, false);
		if (AutomationReport.getTxtReport() != null) {
			AutomationReport.getTxtReport()
					.addStep("******************************************************************************");
			AutomationReport.getTxtReport().addStep(banerStatistic);
			AutomationReport.getTxtReport()
					.addStep("******************************************************************************");
		}
		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addStatistic(id, banerStatistic);
		}

		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().addResultStatistic(id, Tags.SCRIPT, banerStatistic);
		}
	}

	/**
	 * Getter for timer.
	 *
	 * @return {@link Timer} object
	 */
	public Timer getTimer() {
		return timer;
	}
}
