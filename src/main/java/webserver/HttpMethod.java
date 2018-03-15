package webserver;

public enum HttpMethod {
	GET("GET"), POST("POST");
	
	private String method;
	
	private HttpMethod(String method) {
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}
	
	public boolean whichMethod(String method) {
		return this.getMethod().equals(method);
	}
}
