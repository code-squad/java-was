package model;

public class HttpRequestMessage implements HttpMessage {

    private String url;

    public HttpRequestMessage(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
