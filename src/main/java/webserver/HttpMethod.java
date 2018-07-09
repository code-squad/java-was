package webserver;

import exception.NotSupportedMethod;

import java.util.Arrays;

public enum HttpMethod {
    GET(false),
    POST(true),
    PUT(true),
    DELETE(false);

    private boolean isIncludeBody;

    HttpMethod(boolean isIncludeBody) {
        this.isIncludeBody = isIncludeBody;
    }

    public static HttpMethod get(String value) {
        return Arrays.stream(HttpMethod.values()).filter(method -> method.name().equals(value)).findFirst().orElseThrow(NotSupportedMethod::new);
    }

    public boolean isIncludeBody() {
        return isIncludeBody;
    }
}
