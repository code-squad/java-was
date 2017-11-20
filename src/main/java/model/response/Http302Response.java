package model.response;

public class Http302Response extends HttpResponse {

	private static final String status = "HTTP/1.1 302 Found \r\n";
	private Http302Response(String status) {
		super(status);
	}
	
	public static HttpResponse create() {
		return new Http302Response(status);
	}
	
	@Override
	public void setUrl(String url) {
		setHeaders("Location", url);
	}

}
