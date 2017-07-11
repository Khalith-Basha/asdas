package core.utilities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * Utility methods for asynchronous tasks.
 */
public class Timers {
	/**
	 * Waits while particular condition become true. It will perform several
	 * checks with overall time limit set to {@code timeout}.
	 *
	 * @param condition
	 *            condition that will be checked
	 * @param timeout
	 *            overall time limit in milliseconds
	 * @return true, if condition finally became true, false if timeout limit
	 *         has been reached
	 */
	public static boolean waitFor(final Condition condition, int timeout) {
		final CancellableTask conditionCheckingTask = new CancellableTask() {
			@Override
			public void run() {
				while (!isCancelled() && !condition.check()) {
				}
			}
		};

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				conditionCheckingTask.cancel();
			}
		}, timeout);

		conditionCheckingTask.run();

		timer.cancel();

		// If task hasn't been cancelled, then condition finally became true,
		// otherwise, we have reached time limit and not awaited for condition
		return !conditionCheckingTask.isCancelled();
	}

	/**
	 * Interface for anonymous condition classes.
	 */
	public interface Condition {
		boolean check();
	}

	/**
	 * Simple abstract class helper for cancellable task. Used for
	 * {@link Timers#waitFor(Condition, int)}.
	 */
	private static abstract class CancellableTask implements Runnable {
		private volatile boolean isCancelled = false;

		public void cancel() {
			isCancelled = true;
		}

		public boolean isCancelled() {
			return isCancelled;
		}
	}

	/**
	 * global variable to start and store Timer
	 */
	public static long startTime;

	/**
	 * global variable to stop and store Timer
	 */
	public static long stopTime;

	/**
	 * returns formatted String version of the supplied long Time
	 * <p>
	 *
	 * @param elapsedTime
	 *            date and time in long data type of the elapsed time
	 * @return String containing the formatted date time of the given long
	 *         datetime elapsed.
	 */
	public static String getFormattedDateTimeForElapsedTime(long elapsedTime) {
		long hr = TimeUnit.MILLISECONDS.toHours(elapsedTime);
		long min = TimeUnit.MILLISECONDS.toMinutes(elapsedTime - TimeUnit.HOURS.toMillis(hr));
		long sec = TimeUnit.MILLISECONDS
				.toSeconds(elapsedTime - TimeUnit.HOURS.toMillis(hr) - TimeUnit.MINUTES.toMillis(min));
		long ms = TimeUnit.MILLISECONDS.toMillis(elapsedTime - TimeUnit.HOURS.toMillis(hr)
				- TimeUnit.MINUTES.toMillis(min) - TimeUnit.SECONDS.toMillis(sec));

		return String.format("%02d:%02d:%02d.%03d", hr, min, sec, ms);
	}

	/**
	 * Starts the timer
	 *
	 * @return current time as a long
	 */
	public static long startTimer() {
		startTime = System.currentTimeMillis(); // set the global start time
		return startTime;
	}

	/**
	 * Stops the timer
	 *
	 * @return current stop time as long
	 */
	public static long stopTimer() {
		stopTime = System.currentTimeMillis();
		return stopTime;
	}

	/**
	 * Gets elapsed time from the specified start time. Returns string in
	 * "HH:mm:ss:SSS" format.
	 * <p>
	 *
	 * @param startTime
	 *            the starting time
	 * @return A string containing the time difference between the script start
	 *         time and the script elapsed time.
	 */
	public static String getElapsedTime(long startTime) {
		return getFormattedDateTimeForElapsedTime(System.currentTimeMillis() - startTime);
	}

	/**
	 * Gets elapsed time from the specified start and stop times. Returns string
	 * in "HH:mm:ss:SSS" format.
	 * <p>
	 *
	 * @param startTime
	 *            the starting time
	 * @param stopTime
	 *            the stop time
	 * @return A string containing the time difference between the start time
	 *         and the stop time.
	 */
	public static String getElapsedTime(long startTime, long stopTime) {
		return getFormattedDateTimeForElapsedTime(stopTime - startTime);
	}

	/**
	 * returns long data type of elapsed time
	 * <p>
	 *
	 * @param time
	 *            start time
	 * @return Long containing the elapsed time between the start time specified
	 *         and the end time.
	 */
	public static long getElapsedTimeAsLong(long time) {
		return System.currentTimeMillis() - time;
	}

	/**
	 * returns long data type of elapsed time
	 * <p>
	 *
	 * @param startTime
	 *            start time as long
	 * @param stopTime
	 *            stop time as long
	 * @return Long containing the elapsed time between the start time specified
	 *         and the stop time.
	 */
	public static long getElapsedTimeAsLong(long startTime, long stopTime) {
		return stopTime - startTime;
	}
}
