package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class Request {

    private final Logger log = LoggerFactory.getLogger(Request.class);

    String httpMethod;
    String path;
    Map<String, String> params;

    public Request(BufferedReader br) throws IOException {

        String line = br.readLine();
        String[] firstLines = line.split(" ");
        log.debug("path and params : {}", firstLines[1]);
        String[] pathAndParams = firstLines[1].split("\\?");

        httpMethod = firstLines[0];
        path = pathAndParams[0];

        if (pathAndParams.length == 2) {
            params = HttpRequestUtils.parseQueryString(pathAndParams[1]);
        }
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
