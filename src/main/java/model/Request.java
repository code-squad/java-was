package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {
    private RequestLine line;
    private RequestHeaders headers;
    private RequestBody body;

    /* TODO : 깔끔하게 만들 수 없나? */
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
    }

    public String getPath() {
        return line.getPath();
    }

    public String getHeader(String key) {
        return headers.getHeader(key);
    }
}
