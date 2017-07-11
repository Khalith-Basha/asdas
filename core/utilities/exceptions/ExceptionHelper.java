package core.utilities.exceptions;

/**
 * Util methods to process exceptions.
 */
public class ExceptionHelper {
	/**
	 * Converts any checked exception to unchecked by wrapping one with
	 * {@link RuntimeException}.
	 * 
	 * @param t
	 *            {@link Throwable} object
	 * @return new {@link RuntimeException} if {@code t} is instance of checked
	 *         exception
	 */
	public static RuntimeException unchecked(Throwable t) {
		if (t instanceof Error) {
			throw (Error) t;
		} else if (t instanceof RuntimeException) {
			return (RuntimeException) t;
		} else {
			return new RuntimeException(t);
		}
	}
}
