package webserver;

public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod ofValue(String method) {
        switch (method) {
            case "GET":
                return GET;
            case "POST":
                return POST;
            case "PUT":
                return PUT;
            case "DELETE":
                return DELETE;
        }
        return null;
    }

    @Override
    public String toString() {
        return method;
    }
}
