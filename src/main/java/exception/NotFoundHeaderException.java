package exception;

public class NotFoundHeaderException extends RuntimeException {

    public NotFoundHeaderException() {
    }

    public NotFoundHeaderException(String message) {
        super(message);
    }
}
