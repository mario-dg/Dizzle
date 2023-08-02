package logic.exceptions;

/**
 * Exceptions that gets thrown if an invalid Position is processed
 * @author Mario da Graca (cgt103579)
 */
public class IllegalCoordinatesException extends Exception {

    /**
     * Creates a new instance of <code>IllegalCoordinatesException</code>
     * without detail message.
     */
    public IllegalCoordinatesException() {
    }

    /**
     * Constructs an instance of <code>IllegalCoordinatesException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public IllegalCoordinatesException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of <code>IllegalCoordinatesException</code> with
     * the specified detail message and the cause.
     *
     * @param msg the detail message.
     * @param cause stackTrace
     */
    public IllegalCoordinatesException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
