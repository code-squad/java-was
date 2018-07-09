package exception;

public class NotSupportedMethod extends RuntimeException {

    public NotSupportedMethod() {
    }

    public NotSupportedMethod(String message) {
        super(message);
    }
}
