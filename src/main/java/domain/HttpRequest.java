package domain;

import util.HttpRequestUtils;

public class HttpRequest {
    private String method;
    private String path;
    private String parameter;
    private Headers headers;

    public HttpRequest(String method, String path, String parameter, Headers headers) {
        this.method = method;
        this.path = path;
        this.parameter = parameter;
        this.headers = headers;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getParameter() {
        return parameter;
    }

    public String getValueOfParameter(String parameterName) {
        return HttpRequestUtils.parseQueryString(parameter).get(parameterName);
    }

    public String getValueOfHeaders(String headerName) {
        return this.headers.getValue(headerName);
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method='" + method + '\'' +
                ", path='" + path + '\'' +
                ", parameter='" + parameter + '\'' +
                ", headers=" + headers.toString() +
                '}';
    }
}
