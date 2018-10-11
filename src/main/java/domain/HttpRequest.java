package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);

    //    private InputStream in;
    private BufferedReader br;
    private Map<String, String> requestLineData = new HashMap<>();

    //    private String method;
//    private String path = "";
//    private String parameter;
    private Headers headers = new Headers();
    private char[] body;

//    public HttpRequest(String method, String path, String parameter, Headers headers, String body) {
//        this.method = method;
//        this.path = path;
//        this.parameter = parameter;
//        this.headers = headers;
//        this.body = body;
//    }

    public HttpRequest(InputStream in) throws IOException {
        this.br = new BufferedReader(new InputStreamReader(in));

        // parse request line
        parseRequestLine(br);

        // parse headers
        parseHeader(br);

        // parse body
        parseBody(br);
    }

    // 테스트 용이성 혹은 코드 재사용성을 위해 parameter와 return value를 명시
    private Map<String, String> parseRequestLine(BufferedReader br) throws IOException {
        String line = br.readLine();
        String[] tokens = line.split(" ");
        log.debug("tokens : {}", Arrays.toString(tokens));
        requestLineData.put("method", tokens[0]); // GET
        requestLineData.put("path", tokens[1].split("\\?")[0]); // /index.html?name=learner
        if (tokens[1].contains("?")) {
            requestLineData.put("parameter", tokens[1].split("\\?")[1]); // name=learner
        }
        requestLineData.put("version", tokens[2]); // HTTP/1.1

        return requestLineData;
    }

    private Headers parseHeader(BufferedReader br) throws IOException {
        String line;
        while ((line = br.readLine()).contains(":")) {
            log.debug("header line : {}", line);
            headers.add(HttpRequestUtils.parseHeader(line));
        }
        log.debug("line : {}", line);
        return headers;
    }

    private char[] parseBody(BufferedReader br) throws IOException {
        String value;
        char[] buffer = new char[0];
        if (!(value = headers.getValue("Content-Length")).equals("")) {
            log.debug("buffer size : {}", Integer.valueOf(value));
            buffer = new char[Integer.valueOf(value)];
            // WHY : bufferedReader의 read는 char array로 읽어올 수 있게 만들어졌다.
            // 만약 byte array로 받고 싶다면 InputStream, BufferedInputStream을 사용해야 한다.
            br.read(buffer, 0, Integer.valueOf(value));
        }

        return body = buffer;
    }

    public Map<String, String> getRequestLineData() {
        return requestLineData;
    }

    public String getMethod() {
        return requestLineData.get("method");
    }

    public String getPath() {
//        return path;
        return requestLineData.get("path");
    }

    public String getParameter() {
        return requestLineData.get("parameter");
    }

    public char[] getBody() {
        return body;
    }

    public String getParameter(String name) {
        return HttpRequestUtils.parseQueryString(requestLineData.get("parameter")).get(name);
    }

    public String getHeader(String name) {
        return this.headers.getValue(name);
    }

//    @Override
//    public String toString() {
//        return "HttpRequest{" +
//                "method='" + method + '\'' +
//                ", path='" + path + '\'' +
//                ", parameter='" + parameter + '\'' +
//                ", headers=" + headers.toString() +
//                '}';
//    }

    public Cookies getCookies() {
        return headers.getCookies();
    }

    public boolean matchPath(String path) {
        return this.requestLineData.get("path").equals(path);
    }

    public boolean matchCookieValue(String key, String value) {
        return headers.getValue(key).equals(value);
    }
}
