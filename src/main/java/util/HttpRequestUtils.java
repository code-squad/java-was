package util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import domain.Headers;
import domain.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequestUtils {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestUtils.class);

    private static String ROOT_LOCATION = "./webapp";
    private static String EMPTY = "";

    /**
     * @param queryString은 URL에서 ? 이후에 전달되는 field1=value1&field2=value2 형식임
     * @return
     */
    public static Map<String, String> parseQueryString(String queryString) {
        return parseValues(queryString, "&");
    }

    /**
     * @param 쿠키 값은 name1=value1; name2=value2 형식임
     * @return
     */
    public static Map<String, String> parseCookies(String cookies) {
        return parseValues(cookies, ";");
    }

    private static Map<String, String> parseValues(String values, String separator) {
        if (Strings.isNullOrEmpty(values)) {
            return Maps.newHashMap();
        }

        String[] tokens = values.split(separator);
        return Arrays.stream(tokens).map(t -> getKeyValue(t, "=")).filter(p -> p != null)
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }

    static Pair getKeyValue(String keyValue, String regex) {
        if (Strings.isNullOrEmpty(keyValue)) {
            return null;
        }

        String[] tokens = keyValue.split(regex);
        if (tokens.length != 2) {
            return null;
        }

        return new Pair(tokens[0], tokens[1]);
    }

    public static Pair parseHeader(String header) {
        return getKeyValue(header, ": ");
    }

    public static String parseUrl(String line) {
        log.debug("parseUrl : {}", line);
        return line.split(" ")[1];
    }

    public static byte[] readFile(String path) throws IOException {
        return Files.readAllBytes(Paths.get(ROOT_LOCATION + path));
    }

    public static String parsePath(String line) {
        log.debug("parsePath : {}", line);
        return parseUrl(line).split("\\?")[0];
    }

    public static String[] parseAccept(String line) {
        return line.split(",");
    }

//    public static HttpRequest getHttpRequest(BufferedReader reader) throws IOException {
//        String line = null; // GET /user/create?userId=javajigi&password=password HTTP1.1
//        String method = null; // GET
//        String path = null; // user/create
//        String parameter = null; // userId=javajigi&password=password
//        String body = null;
//
//        // request line
//        line = reader.readLine();
//        String[] tokens = line.split(" ");
//        method = tokens[0];
//
//        // TODO request line으로부터 이미 쪼개진 tokens에서 url부분을 받아올 수 있게 한다.
//        path = HttpRequestUtils.parsePath(line);
//        parameter = HttpRequestUtils.parseParameter(line);
//
//        // headers
//        Headers headers = new Headers();
//        while ((line = reader.readLine()).contains(":")) {
//            log.debug("header line : {}", line);
//            headers.add(HttpRequestUtils.parseHeader(line));
//        }
//
//        // read body
//        String value = "";
//        char[] buffer;
//        if (!(value = headers.getValue("Content-Length")).equals("")) {
//            log.debug("buffer size : {}", Integer.valueOf(value));
//            buffer = new char[Integer.valueOf(value)];
//            reader.read(buffer, 0, Integer.valueOf(value));
//            body = String.valueOf(buffer);
//        }
//
//        return new HttpRequest(method, path, parameter, headers, body);
//    }

    public static String parseParameter(String line) {
//        return line.split(" ")[1].split("\\?")[1];
        String[] tokens = line.split(" ")[1].split("\\?");
        if (tokens.length == 0) {
            throw new IllegalArgumentException("wrong parameter line");
        }

        if (tokens.length == 1) {
            return EMPTY;
        }

        return tokens[1];
    }

    public static class Pair {
        String key;
        String value;

        Pair(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((key == null) ? 0 : key.hashCode());
            result = prime * result + ((value == null) ? 0 : value.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (key == null) {
                if (other.key != null)
                    return false;
            } else if (!key.equals(other.key))
                return false;
            if (value == null) {
                if (other.value != null)
                    return false;
            } else if (!value.equals(other.value))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "Pair [key=" + key + ", value=" + value + "]";
        }
    }
}
