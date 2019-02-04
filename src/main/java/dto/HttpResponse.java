package dto;

import util.HttpCode;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpCode code;
    private Map<String, String> headers = new HashMap<>();
    private String body;

    public HttpResponse(HttpCode code) {
        this.code = code;
    }

    public HttpResponse() {

    }

    public String setHeader(String key, String value) {
        return headers.put(key, value);
    }

    public void forward() {
        this.code = HttpCode.OK;
        headers.put("Content-Type", "text/html;charset=utf-8");
    }

    public void setBody(String body) {
        if (body.length() > 0) {
            this.body = body;
            headers.put("Content-Length", String.valueOf(body.length()));
        }
    }

    public void stylesheet() {
        this.code = HttpCode.OK;
        headers.put("Content-Type", "text/css;charset=utf-8");
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb = writeStartLine(sb);
        sb = writeHeaders(sb);
        sb.append("\r\n");
        return sb.toString();
    }

    private StringBuilder writeHeaders(StringBuilder sb) {
        for (String key : headers.keySet()) {
            sb.append(key);
            sb.append(": ");
            sb.append(headers.get(key));
            sb.append("\r\n");
        }
        return sb;
    }

    private StringBuilder writeStartLine(StringBuilder sb) {
        sb.append("HTTP/1.1 ");
        sb.append(code.getStartLine());
        sb.append("\r\n");
        return sb;
    }

    public void redirect(String redirectUrl) {
        this.code = HttpCode.FOUND;
        headers.put("Location", redirectUrl);
    }
}
