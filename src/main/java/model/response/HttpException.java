package model.response;

public class HttpException extends RuntimeException {
	public HttpException(String message) {
		super(message);
	}
}
