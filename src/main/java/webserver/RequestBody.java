package webserver;

import util.HttpRequestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestBody {
    private final String body;

    private RequestBody(String body) {
        try {
            this.body = HttpRequestUtils.decodeUrlEncoding(body);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public static RequestBody of(String body) {
        return new RequestBody(body);
    }

    public static RequestBody ofEmpty() {
        return new RequestBody("");
    }

    public Map<String, String> getParameters() {
        return HttpRequestUtils.parseQueryString(body);
    }

    @Override
    public String toString() {
        return body;
    }

    public String getParameter(String name) {
        Map<String, String> parameters = getParameters();
        return parameters.get(name);
    }
}
