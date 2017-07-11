
package core.utilities.report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * The class represents timer and suitable methods.
 */
public final class Timer {
	private final long startTime;

	/**
	 * Initializes a newly created {@link Timer} object.
	 */
	public Timer() {
		startTime = System.currentTimeMillis();
	}

	/**
	 * Gets elapsed time from the specified start time.
	 *
	 * @return elapsed time
	 */
	public String getElapsedTime() {
		long stopTime = System.currentTimeMillis() - startTime;

		return new SimpleDateFormat("KK:mm:ss:SSS")
				.format(new Date(stopTime - TimeZone.getDefault().getOffset(stopTime)));
	}

	/**
	 * Gets elapsed time from the specified start time.
	 *
	 * @return elapsed time
	 */
	public String getElapsedTimeInSeconds() {
		long stopTime = System.currentTimeMillis() - startTime;

		return String.valueOf(stopTime / 1000.0);
	}

	/**
	 * Gets current date and time. For example: 28.04.2014 13:20:45.
	 *
	 * @return current date and time
	 */
	public String getDatestamp() {
		return DateFormat.getDateInstance().format(new Date()) + " " + DateFormat.getTimeInstance().format(new Date());
	}

	/**
	 * Generates TimeStamp.
	 *
	 * @return TimeStamp
	 */
	public String getTimestamp() {
		return new SimpleDateFormat("KK:mm:ss aaa").format(new Date());
	}
}
