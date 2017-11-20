package model.response;

public class Http302Response extends HttpResponse {

	@Override
	public String createHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 302 Found \r\n");
		sb.append("Location: ").append(getHeader("url"));
		return sb.toString();
	}

	@Override
	public void responseUrl(String url) {
		setHeader("url", url);
	}

}
