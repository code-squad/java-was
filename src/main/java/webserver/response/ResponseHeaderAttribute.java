package webserver.response;

public enum ResponseHeaderAttribute {
    LOCATION("Location"),
    CONTENT_LENGTH("Content-Length"),
    CONTENT_TYPE("Content-Type");

    private final String attribute;

    ResponseHeaderAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String get() {
        return attribute;
    }
}
