
package core.utilities.report.essences;

import core.utilities.report.AutomationReport;
import core.utilities.report.LogTemplates;
import core.utilities.report.TestStatuses;
import core.utilities.report.Timer;
import core.utilities.report.XMLReport.Tags;
import core.utilities.report.essences.common.AbstractScenario;
import core.utilities.report.essences.statistic.SuiteStatistic;

/**
 * The class represents the essence of the suite and contains all the necessary
 * methods for logging.
 */
public final class Suite extends AbstractScenario {
	private final SuiteStatistic statistic;
	private Suite parentSuite = null;
	public Boolean isParentSuite = false;

	/**
	 * Initializes a newly created {@link Suite} object.
	 *
	 * @param suitePath
	 *            name of suite
	 */
	public Suite(String suitePath) {
		super(suitePath);

		statistic = new SuiteStatistic(suitePath);

		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addElement(Tags.ROOT, Tags.SUITE, fileName, id);
		}
		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().setTitle(resultHeader);
			AutomationReport.getHtmlReport().addMenu(null, Tags.SUITE, fileName, id);
			AutomationReport.getHtmlReport().addStatisticContainer(id);
		}

	}

	/**
	 * Initializes a newly created {@link Suite} object.
	 *
	 * @param suitePath
	 *            name of suite
	 * @param parentSuite
	 *            parent {@link Suite} object
	 */
	public Suite(String suitePath, Suite parentSuite) {
		super(suitePath);

		this.parentSuite = parentSuite;
		parentSuite.isParentSuite = true;
		statistic = new SuiteStatistic(suitePath);

		parentSuite.getStatistic().incrementExecutedSuitesCounter();

		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addElement(parentSuite.getId(), Tags.SUITE, fileName, id);
		}
		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().setTitle(resultHeader);
			AutomationReport.getHtmlReport().addMenu(parentSuite.getId(), Tags.SUITE, fileName, id);
			AutomationReport.getHtmlReport().addStatisticContainer(id);
		}

	}

	/**
	 * Gets statistic.
	 *
	 * @return {@link SuiteStatistic} object
	 */
	public SuiteStatistic getStatistic() {
		return statistic;
	}

	@Override
	public void complete() {
		if (hasParentSuite()) {
			parentSuite.getStatistic().complete(statistic);
		}

		if (statistic.getExecutedScriptsCounter() == 0 || statistic.getFailedScriptsCounter() > 0
				|| statistic.getFailedSuitesCounter() > 0) {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addFailStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.FAIL);
			}
		} else {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addPassStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.PASS);
			}
		}

		String banerStatistic = generateResultMessage() + LogTemplates.generateEndDateAndTime(timer)
				+ LogTemplates.generateElapsedTime(timer, true);
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
			AutomationReport.getHtmlReport().addResultStatistic(id, Tags.SUITE, banerStatistic);
		}
	}

	public Boolean hasParentSuite() {
		return (parentSuite != null);
	}

	/**
	 * Generates result message.
	 *
	 * @return result message
	 */
	public String generateResultMessage() {
		final String scriptMessage = "Scripts";
		final String suiteMessage = "Suites";

		String suiteStatistic = "";

		if (isParentSuite) {
			suiteStatistic += LogTemplates.generateExecutedTestNumber(suiteMessage,
					statistic.getExecutedSuitesCounter());
			suiteStatistic += LogTemplates.generatePassedTestNumber(suiteMessage, statistic.getPassedSuitesCounter());

			if (statistic.getWarningSuitesCounter() > 0) {
				suiteStatistic += LogTemplates.generateWarningTestNumber(suiteMessage,
						statistic.getWarningSuitesCounter());
			}
			suiteStatistic += LogTemplates.generateFailedTestNumber(suiteMessage, statistic.getFailedSuitesCounter());

			suiteStatistic += LogTemplates.generatePassedTestPercentage(suiteMessage,
					statistic.calculatePassedSuitesPercentage());
			if (statistic.getWarningScriptsCounter() > 0) {
				suiteStatistic += LogTemplates.generateWarningTestPercentage(suiteMessage,
						statistic.calculateWarningSuitesPercentage());
			}
			suiteStatistic += LogTemplates.generateFailedTestPercentage(suiteMessage,
					statistic.calculateFailedSuitesPercentage());
		}

		suiteStatistic += LogTemplates.generateExecutedTestNumber(scriptMessage, statistic.getExecutedScriptsCounter());
		suiteStatistic += LogTemplates.generatePassedTestNumber(scriptMessage, statistic.getPassedScriptsCounter());
		if (statistic.getWarningScriptsCounter() > 0) {
			suiteStatistic += LogTemplates.generateWarningTestNumber(scriptMessage,
					statistic.getWarningScriptsCounter());
		}
		suiteStatistic += LogTemplates.generateFailedTestNumber(scriptMessage, statistic.getFailedScriptsCounter());

		suiteStatistic += LogTemplates.generatePassedTestPercentage(scriptMessage,
				statistic.calculatePassedScriptsPercentage());
		if (statistic.getWarningScriptsCounter() > 0) {
			suiteStatistic += LogTemplates.generateWarningTestPercentage(scriptMessage,
					statistic.calculateWarningScriptsPercentage());
		}
		suiteStatistic += LogTemplates.generateFailedTestPercentage(scriptMessage,
				statistic.calculateFailedScriptsPercentage());

		return suiteStatistic += Script.generateResultMessage(statistic, true);
	}

	public Suite getParentSuite() {
		return parentSuite;
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
