package exception;

public class HandlerMappingException extends RuntimeException {

    public HandlerMappingException() {
    }

    public HandlerMappingException(String message) {
        super(message);
    }
}
