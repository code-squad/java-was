package model;

class RequestLine {
    private static final String SPLIT_REGEX = " ";

    private final String method;
    private final String path;


    RequestLine(String requestLine) {
        String[] lines = requestLine.split(SPLIT_REGEX);
        method = lines[0];
        path = lines[1];
    }

    String getMethod() {
        return method;
    }

    String getPath() {
        return path;
    }
}
