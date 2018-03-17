package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	
	public static HttpMethod getEnum(String method) {
		
		for (HttpMethod httpMethod : HttpMethod.values()) {
			if (httpMethod.getMethod().equals(method)) {
				return httpMethod;	
			}
		}
		return HttpMethod.GET;
	}
	
	public boolean isGet() {
		return this == GET;
	}
	
	public boolean isPost() {
		return this == POST;
	}
}
