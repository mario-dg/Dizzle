package logic.exceptions;

/**
 * Exception that combines all types of exception that are getting thrown
 * when an error occurs while saving a game to a file
 * @author Mario da Graca (cgt103579)
 */
public class LevelSavingException extends Exception {

    /**
     * Creates a new instance of <code>LevelSavingException</code> without
     * detail message.
     */
    public LevelSavingException() {
    }

    /**
     * Constructs an instance of <code>LevelSavingException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public LevelSavingException(String msg) {
        super(msg);
    }
}
