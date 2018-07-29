package webserver;

import exception.NoSuchMethodException;

import java.util.Arrays;

public enum HttpMethod {
    GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

    private final String method;

    HttpMethod(String method) {
        this.method = method;
    }

    public static HttpMethod ofValue(String method) {
        return Arrays.stream(values()).filter(m -> m.toString().equals(method))
                .findFirst()
                .orElseThrow(() -> new NoSuchMethodException("HTTP method: " + method + " is not a valid method."));
    }

    @Override
    public String toString() {
        return method;
    }
}
