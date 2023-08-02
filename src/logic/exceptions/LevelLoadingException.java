package logic.exceptions;

/**
 * Exception that combines all types of exception that are getting thrown
 * when an error occurs while loading a level from a file
 * @author Mario da Graca (cgt103579)
 */
public class LevelLoadingException extends Exception {

    /**
     * Creates a new instance of <code>LevelLoadingException</code> without
     * detail message.
     */
    public LevelLoadingException() {
    }

    /**
     * Constructs an instance of <code>LevelLoadingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LevelLoadingException(String msg) {
        super(msg);
    }
}
