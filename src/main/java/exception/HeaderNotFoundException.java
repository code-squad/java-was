package exception;

public class HeaderNotFoundException extends RuntimeException {

    public HeaderNotFoundException() {
    }

    public HeaderNotFoundException(String message) {
        super(message);
    }
}
