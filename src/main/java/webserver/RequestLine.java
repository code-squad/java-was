package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class RequestLine {
    private static final Logger logger = LoggerFactory.getLogger(RequestLine.class);
    private HttpMethod method;
    private String url;

    public RequestLine(String line) {
        String[] requestLine = HttpRequestUtils.parseRequestLine(line);
        this.method = HttpMethod.ofValue(requestLine[0]);
        this.url = requestLine[1];
    }

    String[] parseUrl() {
        return url.split("\\?");
    }

    String getQueryString() {
        return parseUrl()[1];
    }

    public Resource getResource() {
        String uri = parseUrl()[0];
        return new Resource(uri);
    }

    public User getUser() {
        try {
            Map<String, String> userInfo = HttpRequestUtils.parseQueryString(getQueryString());
            String userId = userInfo.get("userId");
            String password = userInfo.get("password");
            String name = HttpRequestUtils.decode(userInfo.get("name"));
            String email = HttpRequestUtils.decode(userInfo.get("email"));

            return new User(userId, password, name, email);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }


}