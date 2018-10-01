package domain;

public class HttpRequest {
    private String method;
    private String host;
    private String path;
    private String parameter;
    private String header;

    public HttpRequest(String method, String host, String path, String parameter, String header) {
        this.method = method;
        this.host = host;
        this.path = path;
        this.parameter = parameter;
        this.header = header;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
