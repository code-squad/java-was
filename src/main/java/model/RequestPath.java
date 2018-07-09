package model;

public class RequestPath {
    private static final String SPLIT_REGEX = "";
    private static final int SPLIT_INDEX = 2;

    private String uri;

    public RequestPath(String requestLine) {
        uri = requestLine.split(SPLIT_REGEX)[SPLIT_INDEX];
    }
}
