package model;

public enum Method {
	Post("POST"), Get("GET"), Put("PUT"), Head("HEAD"), Patch("PATCH"), Trace("TRACE"), Options("OPTIONS"), Delete("DELETE");
	private String method;

	Method(String method) {
		this.method = method;
	}

	public boolean equals(String method) {
		return this.method.equals(method);
	}
	
}
