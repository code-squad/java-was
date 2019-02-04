package util;

import dto.HttpRequest;
import exception.HttpParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParser {

    public static final String EMPTY_LINE = "";

    static public HttpRequest parse(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String[] startLines = parseStartLine(br);

        Map<String, String> headers = parseHeader(br);

        Map<String, String> queries = parseQueryString(br, startLines, headers);

        return new HttpRequest(startLines, headers, queries);
    }

    static private String[] parseStartLine(BufferedReader br) throws IOException {
        String startLine = br.readLine();
        if (startLine == null) {
            throw new HttpParseException("빈 HTTP");
        }
        return startLine.split(" ");
    }

    static private Map<String, String> parseHeader(BufferedReader br) throws IOException {
        String line;
        List<String> lines = new ArrayList<>();
        while (!EMPTY_LINE.equals(line = br.readLine())) {
            lines.add(line);
        }
        return HttpRequestUtils.parseHeader(lines);
    }

    static private Map<String, String> parseQueryString(BufferedReader br, String[] startLines, Map<String, String> headers) throws IOException {
        if (startLines[0].equals("GET") && startLines[1].contains("?")) {
            String[] url = startLines[1].split("\\?");
            startLines[1] = url[0];
            return HttpRequestUtils.parseQueryString(url[1]);
        } else if (startLines[0].equals("POST") && headers.containsKey("Content-Length")) {
            String queryString = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
            return HttpRequestUtils.parseQueryString(queryString);
        }
        return new HashMap<>();
    }
}
