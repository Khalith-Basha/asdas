
package core.utilities.report.essences.common;

import core.utilities.XML;
import core.utilities.report.Timer;
import core.utilities.report.essences.Group;
import core.utilities.report.essences.TestCase;

/**
 * The class is super class for {@link TestCase}, {@link Group} and
 * {@link AbstractScenario}.
 */
public abstract class TestEssence {
	protected final String id;
	protected final Timer timer;

	/**
	 * Initializes a newly created {@link TestEssence} object.
	 *
	 * @param header
	 *            header for this essence. For example: test case description or
	 *            full path to script file depending on essence type.
	 */
	protected TestEssence(String header) {
		timer = new Timer();

		id = XML.generateUUID();
	}

	/**
	 * Initializes a newly created {@link TestEssence} object.
	 */
	protected TestEssence() {
		timer = new Timer();

		id = XML.generateUUID();
	}

	/**
	 * Completes an executing and it updates statistic.
	 */
	public void finish() {
		complete();
	}

	/**
	 * Gets {@link #id}.
	 *
	 * @return {@link #id}
	 */
	public String getId() {
		return id;
	}

	/**
	 * Generates header for logging.
	 *
	 * @param description
	 *            description of this essence. For example: test case
	 *            description or full path to script file depending on essence
	 *            type.
	 * @return necessary header
	 */
	protected abstract String generateHeader(String description);

	/**
	 * Executes necessary actions for completes.
	 */
	protected abstract void complete();
}
