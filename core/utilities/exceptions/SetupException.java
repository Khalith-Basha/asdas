package core.utilities.exceptions;

/**
 * The exception for defining setup errors in test scripts.
 */
public class SetupException extends RuntimeException {
	private static final long serialVersionUID = 2819441120656906507L;

	public SetupException(String message) {
		super(message);
	}

	public SetupException(Throwable cause) {
		super(cause);
	}

	public SetupException(String message, Throwable cause) {
		super(message, cause);
	}

	public SetupException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
