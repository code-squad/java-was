package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HttpFactory {

    private static final Logger log = LoggerFactory.getLogger(HttpFactory.class);

    public static HttpRequest init(InputStream in) throws IOException {
        InputStreamReader input = new InputStreamReader(in);
        BufferedReader request = new BufferedReader(input);
        List<String> methodAndUrl = Arrays.asList(request.readLine().split(" "));
        HttpMethod method = new HttpMethod(methodAndUrl.get(0));
        Map<String, String> headers = createHeader(request);
        HttpHeader httpHeader = new HttpHeader(headers);
        String url = methodAndUrl.get(1);
        return new HttpRequest(url, method, httpHeader, request);
    }

    public String getMethod(String url) {
        List<String> urlAndQueryString = Arrays.asList(url.split("\\?"));
        String getUrl = urlAndQueryString.get(0).trim();
        log.debug("Get Url : {}", getUrl);
        if (urlAndQueryString.size() != 2) {
            return getUrl;
        }

        String queryString = urlAndQueryString.get(1).trim();
        log.debug("QueryString : {}", queryString);
        return getUrl;
    }


    private static HttpParameter parseBody(BufferedReader request, String length) throws IOException {
        int size = Integer.parseInt(length);
        String line = IOUtils.readData(request, size);
        log.debug("Body : {}", line);
        return new HttpParameter(line);
    }

    public static Map<String, String> createHeader(BufferedReader request) throws IOException {
        String line = request.readLine();
        String[] parse;
        Map<String, String> header = new HashMap<>();
        while (!line.equals("")) {
            log.debug("header : {}", line);
            parse = parseHeader(line);
            header.put(parse[0].trim(), parse[1].trim());
            line = request.readLine();
        }
        return header;
    }

    public static String[] parseHeader(String line) {
        return line.split(":");
    }
}
