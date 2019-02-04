package dto;

import util.HttpCode;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private HttpCode code;
    private Map<String, String> headers = new HashMap<>();
    private byte[] body;

    public HttpResponse(HttpCode code) {
        this.code = code;
    }

    public HttpResponse() {

    }

    public String setHeader(String key, String value) {
        return headers.put(key, value);
    }

    public void forward(String resource) throws IOException {
        headers.put("Content-Type", "text/html;charset=utf-8");
    }

    public void setBody(byte[] view) {
        if (view.length > 0) {
            this.body = view;
            headers.put("Content-Length", String.valueOf(view.length));
        }
    }
}
