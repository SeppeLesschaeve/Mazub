package jumpingalien.util;

/**
 * A class of model exceptions involving a cause and a message.
 *
 */
public class ModelException extends RuntimeException {

	/**
	 * Create a new model exception involving the given cause.
	 * 
	 * @param  cause
	 *         The cause for this new model exception.
	 * @effect ...
	 *       | super(cause)
	 */
	public ModelException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Create a new model exception involving the given cause and the given message.
	 * 
	 * @param  message
	 *         The message for this new model exception.
	 * @param  cause
	 *         The cause for this new model exception.
	 * @effect ...
	 *       | super(message,cause)
	 */
	public ModelException(String message, Throwable cause) {
		super(message, cause);
	}
	
	/**
	 * Create a new model exception involving the given message.
	 * 
	 * @param  message
	 *         The message for this new model exception.
	 * @effect ...
	 *       | super(message)
	 */
	public ModelException(String message) {
		super(message);
	}

	private static final long serialVersionUID = 1L;

}
