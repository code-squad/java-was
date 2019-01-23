package codesquad.model;

import java.util.Arrays;

public enum RequestMethod {
    POST,
    GET,
    PUT,
    DELETE;

    public static RequestMethod of(String requestMethod) {
        return Arrays.stream(RequestMethod.values())
                .filter(request -> request.toString().equals(requestMethod))
                .findFirst().get();
    }
}
