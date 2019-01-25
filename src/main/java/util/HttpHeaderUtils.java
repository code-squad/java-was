package util;

import webserver.http.Pair;
import webserver.http.request.RequestHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HttpHeaderUtils {
    private static final Logger logger = LoggerFactory.getLogger(HttpHeaderUtils.class);

    public static RequestHeaders parseRequestHeaders(BufferedReader br) throws IOException {
        String header;
        List<Pair> headers = new ArrayList<>();

        while (!(header = br.readLine()).equals("")) {
            headers.add(HttpRequestUtils.parseHeader(header));
            logger.debug("header : {}", header);
        }

        return new RequestHeaders(headers);
    }
}
