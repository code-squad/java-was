package model.response;

public class HttpResponseException extends RuntimeException{
	public HttpResponseException(String message) {
		super(message);
	}
}
