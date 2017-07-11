package core.utilities.exceptions;

import java.util.concurrent.Callable;

/**
 * The class contains common methods for handling of exceptions.
 */
public class ExceptionHandler {
	/**
	 * Performs command. Should be used for procedures.
	 * 
	 * @param command
	 *            the executable command
	 */
	public static void execute(Executable command) {
		try {
			command.execute();
		} catch (Exception exception) {
			throw new SetupException(exception);
		}
	}

	/**
	 * Performs command. Should be used for procedures.
	 * 
	 * @param command
	 *            the executable command
	 * @param errorMessage
	 *            message about error
	 */
	public static void execute(Executable command, String errorMessage) {
		try {
			command.execute();
		} catch (Exception exception) {
			throw new SetupException(errorMessage, exception);
		}
	}

	/**
	 * Returns result of executed command. Should be used for functions.
	 * 
	 * @param command
	 *            the callable command
	 * @param <T>
	 *            generic type
	 * @return command result
	 */
	public static <T> T execute(Callable<T> command) {
		try {
			return command.call();
		} catch (Exception exception) {
			throw new SetupException(exception);
		}
	}

	/**
	 * Returns result of executed command. Should be used for functions.
	 * 
	 * @param command
	 *            the callable command
	 * @param errorMessage
	 *            message about error
	 * @param <T>
	 *            generic type
	 * @return command result
	 */
	public static <T> T execute(Callable<T> command, String errorMessage) {
		try {
			return command.call();
		} catch (Exception exception) {
			throw new SetupException(errorMessage, exception);
		}
	}

	/**
	 * Interface for anonymous executing commands.
	 */
	public interface Executable {
		/**
		 * It should be implement execution of some commands.
		 * 
		 * @throws Exception
		 *             error
		 */
		public void execute() throws Exception;
	}
}
