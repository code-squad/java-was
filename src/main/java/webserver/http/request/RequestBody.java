package webserver.http.request;

import java.util.Map;

public class RequestBody {
    private Map<String, String> body;

    public RequestBody(Map<String, String> body) {
        this.body = body;
    }

    public String getParameter(String key) {
        return this.body.get(key);
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "body=" + body +
                '}';
    }
}
