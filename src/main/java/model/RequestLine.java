package model;

class RequestLine {
    private static final String SPLIT_REGEX = " ";

    private final HttpMethod method;
    private final String path;

    RequestLine(String requestLine) {
        String[] lines = requestLine.split(SPLIT_REGEX);
        method = HttpMethod.get(lines[0]);
        path = lines[1];
    }

    String getMethod() {
        return method.name();
    }

    String getPath() {
        return path;
    }
}
