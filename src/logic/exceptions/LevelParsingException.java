package logic.exceptions;

/**
 * Exception that combines all types of exception that are getting thrown
 * when an error occurs while starting a new game from a level file
 * @author Mario da Graca (cgt103579)
 */
public class LevelParsingException extends Exception {

    /**
     * Creates a new instance of <code>LevelParsingException</code> without
     * detail message.
     */
    public LevelParsingException() {
    }

    /**
     * Constructs an instance of <code>LevelParsingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LevelParsingException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>LevelParsingException</code> with the
     * specified detail message and the cause.
     *
     * @param msg the detail message.
     * @param cause
     */
    public LevelParsingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
