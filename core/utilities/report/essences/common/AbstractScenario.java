
package core.utilities.report.essences.common;

import java.net.UnknownHostException;

import core.utilities.Log;
import core.utilities.exceptions.SetupException;
import core.utilities.report.LogTemplates;
import core.utilities.report.essences.Script;
import core.utilities.report.essences.Suite;

/**
 * The class is super class for {@link Script} and {@link Suite}.
 */
public abstract class AbstractScenario extends TestEssence {
	protected final String fileName;
	protected final String resultHeader;

	/**
	 * Initializes a newly created {@link AbstractScenario} object.
	 *
	 * @param scenarioPath
	 *            always:
	 *            Thread.currentThread().getStackTrace()[1].getClassName()
	 */
	protected AbstractScenario(String scenarioPath) {
		super(scenarioPath);
		fileName = scenarioPath;
		resultHeader = LogTemplates.generateResultTitle(this.getClass().getSimpleName().toLowerCase(), fileName);
	}

	@Override
	protected String generateHeader(String description) {
		String result = String.format("* %s name: %s.%s", this.getClass().getSimpleName(), description,
				System.lineSeparator());

		result += String.format("* Result folder: %s.%s", Log.AUTOMATION_TEST_RESULTS_PATH, System.lineSeparator());
		result += generateTestingMachineMessage();

		return result += String.format("* Testing machine OS: %s.", System.getProperty("os.name", "unknown"));
	}

	/**
	 * Generates testing machine message.
	 *
	 * @return message about testing machine
	 */
	private static String generateTestingMachineMessage() {
		try {
			return String.format("* Testing machine: %s.%s", java.net.InetAddress.getLocalHost(),
					System.lineSeparator());
		} catch (UnknownHostException exception) {
			throw new SetupException("Could not get local host client name", exception);
		}
	}
}
