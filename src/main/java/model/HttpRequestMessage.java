package model;

public class HttpRequestMessage implements HttpMessage {

    private final int contentLength;
    private String path;

    public HttpRequestMessage(String path, int contentLength) {
        this.path = path;
        this.contentLength = contentLength;
    }

    public String getPath() {
        return path;
    }

    public int getContentLength() {
        return contentLength;
    }

    @Override
    public String toString() {
        return "HttpRequestMessage{" +
                "contentLength=" + contentLength +
                ", path='" + path + '\'' +
                '}';
    }
}
