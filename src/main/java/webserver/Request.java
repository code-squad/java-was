package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class Request {
    private RequestLine line;
    private RequestHeaders headers;
    private RequestBody body;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        line = new RequestLine(br.readLine());
        headers = new RequestHeaders();

        while (true) {
            String data = br.readLine();
            if (data == null || data.equals("")) {
                break;
            }
            headers.add(data);
        }

        if (line.isIncludeBody()) {
            body = new RequestBody(br.readLine());
        }
    }

    public String getPath() {
        return line.getPath();
    }

    public String getMethod() {
        return line.getMethod();
    }

    public String getHeader(String key) {
        return headers.getHeader(key);
    }

    public String getBody() {
        if (Objects.isNull(body)) {
            return "";
        }
        return body.get();
    }
}
