package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Controller {

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private static final String METHOD = "method";
    private static final String URL = "url";

    public static String parseRequestUrl(BufferedReader request) throws IOException {
        String firstLine = request.readLine();
        String method = HttpRequestUtils.parseUrl(firstLine, METHOD);
        String url = HttpRequestUtils.parseUrl(firstLine, URL);
        String path = null;

        log.debug("Method : {}", method);

        if (method.equals("GET")) {
            path = getMethod(url);
        }

        if (url.equals("/user/create")) {
            log.debug("Start!!");
            String body = readBody(request);
            return createUser(url, body);
        }

        return path;
    }

    private static String getMethod(String url) {
        List<String> urlAndQueryString = Arrays.asList(url.split("\\?"));
        String getUrl = urlAndQueryString.get(0).trim();
        if (urlAndQueryString.size() != 2) {
            return getUrl;
        }

        String queryString = urlAndQueryString.get(1).trim();
        log.debug("Get Url : {}", getUrl);
        log.debug("QueryString : {}", queryString);
        return getUrl;
    }

    private static String createUser(String url, String queryStrings) {
        Map<String, String> queryString = HttpRequestUtils.parseQueryString(queryStrings);
        User user = new User(queryString.get("userId"), queryString.get("password"), queryString.get("name"), queryString.get("email"));
        log.debug("Create User : {}", user);
        return "/index.html";
    }

    private static String readBody(BufferedReader request) throws IOException {
        String line = request.readLine();
        int size = 0;

        while (!line.equals("")) {
            log.debug("header : {}", line);
            line = request.readLine();
            if (line.startsWith("Content-Length")) {
                size = Integer.parseInt(line.substring(line.indexOf(":") + 1).trim());
                log.debug("Size : {}", size);
            }
        }
        line = IOUtils.readData(request, size);
        log.debug("Body : {}", line);
        return line;
    }
}
