package domain;

public enum HttpStatusCode {
    OK(200),
    FOUND(302);

    private int code;

    private HttpStatusCode(int code) {
        this.code = code;
    }

    public int getStatus() {
        return code;
    }
}
