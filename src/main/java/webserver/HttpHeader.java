package webserver;

public enum HttpHeader {
	URI("URI"), CONTENTLENGTH("Content-Length"), METHOD("Method"), 
	LOGINED("logined"), ACCEPT("Accept"), COOKIE("Cookie");
	
	private String header;
	
	public String getHeader() {
		return header;
	}
	
	private HttpHeader(String header) {
		this.header = header;
	}
}
