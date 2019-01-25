package exception;

public class UnAuthenticationException extends Exception {
    public UnAuthenticationException() {
    }

    public UnAuthenticationException(String message) {
        super(message);
    }
}
