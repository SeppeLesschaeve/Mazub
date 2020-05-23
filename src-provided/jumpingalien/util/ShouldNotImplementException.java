package jumpingalien.util;

public class ShouldNotImplementException extends Exception {

	/**
	 * Create a new should-not-implement exception involving the given cause.
	 * 
	 * @param  cause
	 *         The cause for this new should-not-implement exception.
	 * @effect ...
	 *       | super(cause)
	 */
	public ShouldNotImplementException(Throwable cause) {
		super(cause);
	}

	/**
	 * Create a new should-not-implement exception involving the given cause and the given message.
	 * 
	 * @param  message
	 *         The message for this new should-not-implement exception.
	 * @param  cause
	 *         The cause for this new should-not-implement exception.
	 * @effect ...
	 *       | super(message,cause)
	 */
	public ShouldNotImplementException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new should-not-implement exception involving the given message.
	 * 
	 * @param  message
	 *         The message for this new should-not-implement exception.
	 * @effect ...
	 *       | super(message)
	 */
	public ShouldNotImplementException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
