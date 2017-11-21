package model;

public enum Method {
	POST("POST"), GET("GET"), PUT("PUT"), HEAD("HEAD"), PATCH("PATCH"), TRACE("TRACE"), OPTIONS("OPTIONS"), DELETE("DELETE");
	private String method;

	Method(String method) {
		this.method = method;
	}

	public boolean equals(String method) {
		return this.method.equals(method);
	}
	
}
