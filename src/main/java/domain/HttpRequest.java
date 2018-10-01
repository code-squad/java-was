package domain;

public class HttpRequest {
    String method;
    String host;
    String path;
    String parameter;
    String header;

    public HttpRequest(String method, String host, String path, String parameter, String header) {
        this.method = method;
        this.host = host;
        this.path = path;
        this.parameter = parameter;
        this.header = header;
    }
}
