package exception;

public class NoSuchMethodException extends RuntimeException {

    public NoSuchMethodException() {
        super();
    }

    public NoSuchMethodException(String message) {
        super(message);
    }
}

