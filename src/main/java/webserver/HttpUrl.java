package webserver;

import java.io.IOException;

public class HttpUrl {

    private String url;

    public HttpUrl(String url) throws IOException {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
