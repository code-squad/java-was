package webserver;

public enum HttpHeader {
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length"),
    HOST("Host"),
    CONNECTION("Connection"),
    ACCEPT("Accept"),
    AUTHORIZATION("Authorization"),
    LOCATION("Location"),
    SET_COOKIE("Set-Cookie"),
    COOKIE("Cookie");


    private final String header;

    HttpHeader(String header) {
        this.header = header;
    }

    private String getValue() {
        return header;
    }

    public static HttpHeader getByValue(String value) {
        String fixed = value.replace("-","_").toUpperCase();
        return valueOf(fixed);
    }

    @Override
    public String toString() {
        return header;
    }
}
