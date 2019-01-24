package webserver.http.request;

import java.util.Map;

public class RequestBody {
    private Map<String, String> body;

    public RequestBody() {
    }

    public RequestBody(Map<String, String> body) {
        this.body = body;
    }

    public Map<String, String> getBody() {
        return body;
    }

    public boolean isEmpty() {
        return body.isEmpty();
    }

    public String getBodyValue(String key) {
        return this.body.get(key);
    }

    @Override
    public String toString() {
        return "RequestBody{" +
                "body=" + body +
                '}';
    }
}
