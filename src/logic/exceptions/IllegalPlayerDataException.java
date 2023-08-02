package logic.exceptions;

/**
 * Exception that gets thrown if any type of invalid Data regarding a Player
 * is processed
 * @author Mario da Graca (cgt103579)
 */
public class IllegalPlayerDataException extends Exception {

    /**
     * Creates a new instance of <code>IllegalPlayerDataException</code> without
     * detail message.
     */
    public IllegalPlayerDataException() {
    }

    /**
     * Constructs an instance of <code>IllegalPlayerDataException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalPlayerDataException(String msg) {
        super(msg);
    }
}
