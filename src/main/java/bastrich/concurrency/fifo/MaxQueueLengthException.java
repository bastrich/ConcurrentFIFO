package bastrich.concurrency.fifo;

/**
 * @author bastrich on 14.10.2017.
 */
public class MaxQueueLengthException extends Exception {
    public MaxQueueLengthException(Throwable cause) {
        super(cause);
    }
}
