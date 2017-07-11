
package core.utilities.tools;

/**
 * This interface contains common methods responding to engine behavior.
 */
public interface Engine {
	/**
	 * Set automation engine.
	 */
	public void setEngine();

	/**
	 * Stops engine and closes it.
	 */
	public void stop();

	/**
	 * Takes tool capture.
	 *
	 * @param fileName name of file to save capture
	 * @param error set to true if error message should be printed
	 * @return true if capture was successful
	 */
	public boolean takeCapture(String fileName, boolean error);
}
