package webserver;

import exception.NotSupportedMethodException;

import java.util.Arrays;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    public static HttpMethod get(String value) {
        return Arrays.stream(HttpMethod.values()).filter(method -> method.name().equals(value)).findFirst().orElseThrow(NotSupportedMethodException::new);
    }
}
