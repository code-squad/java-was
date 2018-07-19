package webserver;

public enum HttpStatus {
    OK(200, "OK"), FOUND(302, "Found"), UNAUTHORIZED(401, "Unauthorized"), FORBIDDEN(403, "Forbidden");

    private final int statusCode;
    private final String phrase;

    HttpStatus(int statusCode, String phrase) {
        this.statusCode = statusCode;
        this.phrase = phrase;
    }


    @Override
    public String toString() {
        return statusCode + " " + phrase;
    }
}
