package exception;

public class NotSupportedMethodException extends RuntimeException {

    public NotSupportedMethodException() {
    }

    public NotSupportedMethodException(String message) {
        super(message);
    }
}
