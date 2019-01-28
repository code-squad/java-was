package util;

import model.HttpHeader;
import model.HttpRequest;
import model.MethodType;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

public class HttpRequestFactory {

    private static final Logger logger = getLogger(HttpRequestFactory.class);

    public static HttpRequest of(BufferedReader br) throws IOException {
        /* Header 읽기 */
        String headerLine = br.readLine();
        MethodType method = HttpRequest.obtainMethod(headerLine);
        String url = HttpRequest.obtainURL(headerLine);
        HttpHeader httpHeader = HttpHeaderFactory.of(br);

        /* Body 읽기 */
        String body = readBody(httpHeader.obtainContentLength(), br);

        return new HttpRequest(url, method, body, httpHeader);
    }

    public static String readBody(int contentLength, BufferedReader br) throws IOException {
        String body = "";
        if(contentLength > 0) {
            char[] buf = new char[contentLength + 2];
            br.read(buf);
            body = new String(buf);
        }
        return body;
    }
}