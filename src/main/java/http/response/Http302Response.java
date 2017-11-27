package http.response;

public class Http302Response extends HttpResponse {

	private static final String REQUESTFIRSTLINE = "HTTP/1.1 302 Found \r\n";
	private Http302Response(String requestFirstLine) {
		super(requestFirstLine);
	}
	
	public static HttpResponse create() {
		return new Http302Response(REQUESTFIRSTLINE);
	}
	
	@Override
	public void setUrl(String url) {
		setHeaders("Location", url);
	}

}
