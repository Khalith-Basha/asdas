package core.utilities.exceptions;

/**
 * The exception for defining assert errors in test scripts.
 */
public class AssertException extends RuntimeException {
	private static final long serialVersionUID = 5223163930813874744L;

	public AssertException(String message) {
		super(message);
	}

	public AssertException(Throwable cause) {
		super(cause);
	}

	public AssertException(String message, Throwable cause) {
		super(message, cause);
	}

	public AssertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
