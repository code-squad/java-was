package dto;

import util.HttpCode;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpCode code;
    private Map<String, String> headers = new HashMap<>();

    public String setHeader(String key, String value) {
        return headers.put(key, value);
    }

    public void forward() {
        this.code = HttpCode.OK;
        headers.put("Content-Type", "text/html;charset=utf-8");
    }

    public void stylesheet() {
        this.code = HttpCode.OK;
        headers.put("Content-Type", "text/css;charset=utf-8");
    }

    public void redirect(String redirectUrl) {
        this.code = HttpCode.FOUND;
        headers.put("Location", redirectUrl);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb = writeStartLine(sb);
        sb = writeHeaders(sb);
        sb.append("\r\n");
        return sb.toString();
    }

    private StringBuilder writeStartLine(StringBuilder sb) {
        sb.append("HTTP/1.1 ");
        sb.append(code.getStartLine());
        sb.append("\r\n");
        return sb;
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
}
