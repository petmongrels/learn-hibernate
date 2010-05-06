package database;

public class TimeoutException extends RuntimeException {
    public TimeoutException(Throwable cause) {
        super(cause);
    }
}
