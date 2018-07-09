package webserver;

import com.google.common.base.Strings;

class RequestBody {
    private final String body;

    RequestBody(String body) {
        if (Strings.isNullOrEmpty(body)) {
            this.body = "";
            return;
        }
        this.body = body;
    }

    String get() {
        return body;
    }
}
