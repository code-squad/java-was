package webserver;

import java.util.Optional;

public class HttpBody {
    private final String body;

    public HttpBody(Optional<String> maybeBody) {
        if (!maybeBody.isPresent()) {
            this.body = maybeBody.get();
            return;
        }
        this.body = "";
    }

    public static HttpBody empty() {
        return new HttpBody(null);
    }

    public String get() {
        return body;
    }

    public int size() {
        return body.getBytes().length;
    }
}
