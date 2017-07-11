
package core.utilities.report.essences;

import java.util.List;

import core.utilities.Strings;
import core.utilities.report.AutomationReport;
import core.utilities.report.TestStatuses;
import core.utilities.report.Timer;
import core.utilities.report.XMLReport.Tags;
import core.utilities.report.essences.common.TestEssence;


/**
 * The class contains necessary test case methods for logging system.
 */
public final class TestCase extends TestEssence {
	private Group group = null;
	private Script script = null;
	private boolean isFailed;
	private boolean isWarning;
	private boolean isSkipped;


	/**
	 * Initializes a newly created {@link TestCase} object.
	 *
	 * @param testEssence
	 *            object
	 * @param description
	 *            test case description
	 * @param descriptionLink
	 *            link with description of test case
	 * @param comments
	 *            additional information of test case
	 */
	public TestCase(TestEssence testEssence, String description, String descriptionLink, String comments) {
		super();

		if (isGroup(testEssence)) {
			this.group = (Group) testEssence;
		} else {
			this.script = (Script) testEssence;
		}

		isFailed = false;
		isWarning = false;
		isSkipped = false;

		String parentId = isGroup(testEssence) ? this.group.getId() : this.script.getId();

		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addElement(parentId, Tags.TEST_CASE, description, id);
		}
		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().addMenu(parentId, Tags.TEST_CASE, description, id);
			AutomationReport.getHtmlReport().addStatisticContainer(id);
		}

		if (descriptionLink != null) {
			List<String> links = Strings.stringToList(descriptionLink);
			for (String link : links) {
				if (AutomationReport.getXmlReport() != null) {
					AutomationReport.getXmlReport().addTestCaseLink(id, link);
				}
				if (AutomationReport.getHtmlReport() != null) {
					AutomationReport.getHtmlReport().addTestCaseLink(id, link);
				}
			}
		}

		if (comments != null) {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addTestCaseFailComments(id, comments);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().addTestCaseFailComments(id, comments);
			}
			isWarning = true;
		}
	}

	/**
	 * Initializes a newly created {@link TestCase} object.
	 *
	 * @param testEssence
	 *            object
	 * @param description
	 *            test case description
	 */
	public TestCase(TestEssence testEssence, String description) {
		this(testEssence, description, null, null);
	}

	/**
	 * Checks whether it is failed.
	 *
	 * @return true if script is failed
	 */
	public boolean isFailed() {
		return isFailed;
	}

	/**
	 * Sets test case as failed and increments test case failed counter.
	 */
	public void setFailed() {
		isFailed = true;
	}

	/**
	 * Sets test case as skipped and increments test case skipped counter.
	 */
	public void setSkipped() {
		isSkipped = true;
		if (AutomationReport.getXmlReport() != null) {
			AutomationReport.getXmlReport().addSkipStatus(id);
		}
		if (AutomationReport.getHtmlReport() != null) {
			AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.SKIP);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected String generateHeader(String description) {
		return String.format("* Start of test case: %s", description);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void complete() {
		if (group != null) {
			group.getStatistic().incrementExecutedTestCasesCounter();
		} else {
			script.getStatistic().incrementExecutedTestCasesCounter();
		}

		if (isWarning && isFailed) {
			if (group != null) {
				group.getStatistic().incrementWarningTestCasesCounter();
			} else {
				script.getStatistic().incrementWarningTestCasesCounter();
			}
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addWarningStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.WARNING);
			}
		} else if (isFailed) {
			if (group != null) {
				group.getStatistic().incrementFailedTestCasesCounter();
			} else {
				script.getStatistic().incrementFailedTestCasesCounter();
			}
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addFailStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.FAIL);
			}
		} else if (isSkipped) {
			if (group != null) {
				group.getStatistic().incrementSkippedTestCasesCounter();
			} else {
				script.getStatistic().incrementSkippedTestCasesCounter();
			}
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addSkipStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.SKIP);
			}
		} else {
			if (AutomationReport.getXmlReport() != null) {
				AutomationReport.getXmlReport().addPassStatus(id);
			}
			if (AutomationReport.getHtmlReport() != null) {
				AutomationReport.getHtmlReport().updateStatus(id, TestStatuses.PASS);
			}
		}
	}

	private boolean isGroup(TestEssence testEssence) {
		return testEssence instanceof Group;
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
