package webserver.request;

import util.IOUtils;
import webserver.HttpBody;
import webserver.HttpHeader;
import webserver.HttpMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static util.IOUtils.CONTENT_LENGTH;

public class Request {
    private RequestLine line;
    private HttpHeader header;
    private HttpBody body;

    public Request(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        line = new RequestLine(br.readLine());
        header = new HttpHeader();

        while (true) {
            String data = br.readLine();
            if (data == null || data.equals("")) {
                break;
            }
            header.add(data);
        }

        if (header.isExistHeader(CONTENT_LENGTH)) {
            body = new HttpBody(IOUtils.readData(br, header.getHeader(CONTENT_LENGTH)));
        }
    }

    public String getPath() {
        return line.getPath();
    }

    public String getMethod() {
        return line.getMethod();
    }

    public String getHeader(String key) {
        return header.getHeader(key);
    }

    public String getBody() {
        return body.get();
    }

    public String getParam(String key) {
        return line.getParam(key);
    }

    public RequestParameter getParameters() {
        return line.getParameters();
    }

    public String getFirstUri() {
        return line.getFirstPath();
    }

    public String getUriExcludeParams() {
        return line.getUriExcludeParams();
    }

    public boolean isMatchMethod(HttpMethod method) {
        return line.isMatchMethod(method);
    }
}
