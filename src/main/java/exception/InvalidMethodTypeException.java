package exception;

public class InvalidMethodTypeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidMethodTypeException(String message) {
		super(message);
	}
}
