package util;

import http.RequestLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vo.HttpMethod;

import java.util.HashMap;
import java.util.Map;

public class HttpLineUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpLineUtils.class);

    public static RequestLine parseRequestLine(String value) {
        logger.debug("Request Line : {}", value);

        String[] tokens = value.split(" ");
        String[] uriAndQuery = tokens[1].split("\\?");

        HttpMethod method = HttpMethod.valueOf(tokens[0]);
        String uri = uriAndQuery[0];
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
