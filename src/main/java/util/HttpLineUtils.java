package util;

import webserver.http.request.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpLineUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpLineUtils.class);

    public static RequestLine parseRequestLine(BufferedReader br) throws IOException {
        String value = br.readLine();
        String[] tokens = value.split(" ");
        String[] uriAndQuery = tokens[1].split("\\?");

        logger.debug("Request Line : {}", value);

        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        String uri = uriAndQuery[0].trim();
        String version = tokens[2];
        Map<String, String> query = checkQuery(uriAndQuery);

        return new RequestLine(method, uri, version, query);
    }

    private static Map<String, String> checkQuery(String[] uriAndQuery) {
        if (uriAndQuery.length > 1)
            return HttpRequestUtils.parseQueryString(uriAndQuery[1]);
        return new HashMap<String, String>();
    }

}
