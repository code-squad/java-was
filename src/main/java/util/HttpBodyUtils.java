package util;

import webserver.http.request.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

public class HttpBodyUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpBodyUtils.class);

    public static RequestBody parseRequestBody(BufferedReader br, int contentLength) throws IOException {
        String value = IOUtils.readData(br, contentLength);
        logger.debug("Request Body : {}", value);

        Map<String, String> body = HttpRequestUtils.parseQueryString(value);

        return new RequestBody(body);
    }
}
