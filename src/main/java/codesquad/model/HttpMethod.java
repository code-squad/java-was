package codesquad.model;

import java.util.Arrays;

public enum HttpMethod {

    POST,
    GET,
    PUT,
    DELETE;

    public static HttpMethod of(String requestMethod) {
        return Arrays.stream(HttpMethod.values())
                .filter(request -> request.toString().equals(requestMethod))
                .findFirst().get();
    }
}
