package codesquad.util;

import codesquad.model.HttpRequestKey;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.slf4j.Logger;

import java.io.*;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

public class IOUtils {
    private static final Logger log = getLogger(IOUtils.class);
    public static final String EMPTY = "";

    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }

    public static Map<HttpRequestKey, String> readHeader(InputStream in) throws IOException {
        Map<HttpRequestKey, String> headers = Maps.newHashMap();
        BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        readFirstLine(br, headers);
        readRestLines(br, headers);
        readBody(br, headers);

        for (HttpRequestKey httpRequestKey : headers.keySet()) {
            log.debug("httpRequestKey : " + headers.get(httpRequestKey));
        }

        return headers;
    }

    private static void readBody(BufferedReader br, Map<HttpRequestKey, String> headers) throws IOException {
        String bodyText = IOUtils.readData(br, Integer.parseInt(headers.getOrDefault(HttpRequestKey.CONTENT_LENGTH, "0")));
        if (!Strings.isNullOrEmpty(bodyText)) headers.put(HttpRequestKey.BODY_VALUE, bodyText);
    }

    private static void readRestLines(BufferedReader br, Map<HttpRequestKey, String> headers) throws IOException {
        String line;
        while (!EMPTY.equals(line = br.readLine())) {
            log.debug(line);
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
            headers.put(HttpRequestKey.of(pair.getKey()), pair.getValue());
            log.debug(line);
        }
    }

    private static void readFirstLine(BufferedReader br, Map<HttpRequestKey, String> headers) throws IOException {
        String firstLine = br.readLine();
        headers.putAll(HttpRequestUtils.parseFirstLine(firstLine));
    }
}
