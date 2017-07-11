
package core.utilities.report.essences;

import core.utilities.report.AutomationReport;
import core.utilities.report.LogTemplates;
import core.utilities.report.TestStatuses;
import core.utilities.report.XMLReport.Tags;
import core.utilities.report.essences.common.TestEssence;
import core.utilities.report.essences.statistic.GroupStatistic;

/**
 * The class represents the essence of the group and contains all the necessary
 * methods for logging.
 */
public final class Group extends TestEssence {
	private final GroupStatistic statistic;
	private final Script script;

	/**
	 * Initializes a newly created {@link Group} object.
	 *
	 * @param script
	 *            current {@link Script} object
	 * @param description
	 *            test case group description
	 */
	public Group(Script script, String description) {
		super(description == null ? "Untitled" : description);

		statistic = new GroupStatistic();
		this.script = script;
		script.getStatistic().incrementExecutedGroupsCounter();

		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addElement(script.getId(), Tags.GROUP, description, id);
		}
		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().addMenu(script.getId(), Tags.SUITE, description, id);
			AutomationReport.getHtmlReport().addStatisticContainer(id);
		}
	}

	/**
	 * Generates result message.
	 *
	 * @param statistic
	 *            {@link GroupStatistic} object
	 * @param isSuiteRunning
	 *            true if suite is running
	 * @return result message
	 */
	public static String generateResultMessage(GroupStatistic statistic, Boolean isSuiteRunning) {

		final String typeMessage = isSuiteRunning ? "Total Testcases" : "Testcases";

		String groupStatistic = LogTemplates.generateExecutedTestNumber(typeMessage,
				statistic.getExecutedTestCasesCounter() - statistic.getSkippedTestCasesCounter());

		groupStatistic += LogTemplates.generatePassedTestNumber(typeMessage, statistic.getPassedTestCasesCounter());
		if (statistic.getSkippedTestCasesCounter() > 0) {
			groupStatistic += LogTemplates.generateSkippedTestNumber(typeMessage,
					statistic.getSkippedTestCasesCounter());
		}
		if (statistic.getWarningTestCasesCounter() > 0) {
			groupStatistic += LogTemplates.generateWarningTestNumber(typeMessage,
					statistic.getWarningTestCasesCounter());
		}
		groupStatistic += LogTemplates.generateFailedTestNumber(typeMessage, statistic.getFailedTestCasesCounter());

		groupStatistic += LogTemplates.generatePassedTestPercentage(typeMessage,
				statistic.calculatePassedTestCasesPercentage());
		if (statistic.getWarningTestCasesCounter() > 0) {
			groupStatistic += LogTemplates.generateWarningTestPercentage(typeMessage,
					statistic.calculateWarningTestCasesPercentage());
		}

		groupStatistic += LogTemplates.generateFailedTestPercentage(typeMessage,
				statistic.calculateFailedTestCasesPercentage());
		groupStatistic += LogTemplates.generateActionsNumber(statistic.getActionsCounter(), isSuiteRunning);

		return groupStatistic += LogTemplates.generateErrorsNumber(statistic.getErrorsCounter(), isSuiteRunning);
	}

	/**
	 * Gets statistic.
	 *
	 * @return {@link GroupStatistic} object
	 */
	public GroupStatistic getStatistic() {
		return statistic;
	}

	@Override
	protected String generateHeader(String testCaseGroupName) {
		return String.format("* Start of test case group: %s", testCaseGroupName);
	}

	@Override
	protected void complete() {
		script.getStatistic().complete(statistic);

		if (((statistic.getExecutedTestCasesCounter() - statistic.getSkippedTestCasesCounter() == 0)
				&& (statistic.getSkippedTestCasesCounter() == 0)) || statistic.getFailedTestCasesCounter() > 0) {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addFailStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.FAIL);
			}
		} else if (statistic.getExecutedTestCasesCounter() - statistic.getSkippedTestCasesCounter() == 0
				&& statistic.getSkippedTestCasesCounter() > 0 && statistic.getFailedTestCasesCounter() == 0
				&& statistic.getWarningTestCasesCounter() == 0) {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addSkipStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.SKIP);
			}
		} else if (statistic.getFailedTestCasesCounter() == 0 && statistic.getWarningTestCasesCounter() > 0) {
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
				+ LogTemplates.generateElapsedTime(timer, false) + System.lineSeparator();

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
			AutomationReport.getHtmlReport().addResultStatistic(id, Tags.GROUP, banerStatistic);
		}
	}
}
