package exception;

public class NotFoundRequestParameterException extends RuntimeException {

    public NotFoundRequestParameterException() {
    }

    public NotFoundRequestParameterException(String message) {
        super(message);
    }
}
