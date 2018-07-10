package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PathParams {

    private final Logger log = LoggerFactory.getLogger(PathParams.class);

    private final HttpMethod httpMethod;
    private final String httpVersion;
    private String path;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> params;

    public PathParams(BufferedReader br) throws IOException {
        String line = br.readLine();
        log.debug("first line of br : {}", line);
        String[] firstLines = line.split(" ");
        httpMethod = HttpMethod.valueOf(firstLines[0].toUpperCase());
        httpVersion = firstLines[2];

        switch (httpMethod) {
            case GET :
                log.debug("path and params : {}", firstLines[1]);
                String[] pathAndParams = firstLines[1].split("\\?");
                path = pathAndParams[0];
                if (pathAndParams.length == 2) params = HttpRequestUtils.parseQueryString(pathAndParams[1]);
                while(true) {
                    String temp = br.readLine();
                    if (temp.isEmpty()) break;
                    String[] headerParams = temp.split(": ");
                    headers.put(headerParams[0], headerParams[1].trim());
                }
                break;

            case POST :
                log.debug("path and params : {}", firstLines[1]);
                path = firstLines[1];
                while (true) {
                    String temp = br.readLine();
                    log.debug("each line : {}", temp);
                    if (temp.isEmpty()) break;
                    String[] headerParams = temp.split(": ");
                    headers.put(headerParams[0], headerParams[1].trim());
                }
                params = HttpRequestUtils.parseQueryString(IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length"))));
                break;
        }
    }

    public String parsePath() {
        return path;
    }

    public Map<String, String> parseParams() {
        return params;
    }

    public HttpMethod parseHttpMethod() {
        return httpMethod;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
