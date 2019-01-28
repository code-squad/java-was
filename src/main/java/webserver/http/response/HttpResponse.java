package webserver.http.response;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private DataOutputStream response;
    private Map<String, String> headers;

    public HttpResponse(DataOutputStream response) {
        this.headers = new HashMap<>();
        this.response = response;
    }

    public void addHeader(String header, String field) {
        if (this.headers.containsKey(header)) {
            this.headers.remove(header);
        }
        this.headers.put(header, field);
    }

    public void forward(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        response200Header(body.length);
        processHeaders();
        responseBody(body);
        this.response.flush();
    }

    public void forwardBody(String bodyValue) throws IOException {
        byte[] body = bodyValue.getBytes();
        response200Header(body.length);
        processHeaders();
        responseBody(body);
        this.response.flush();
    }

    private void responseBody(byte[] body) throws IOException {
        this.response.write(body, 0, body.length);
    }

    private void response200Header(int contentLength) throws IOException {
        this.response.writeBytes("HTTP/1.1 200 OK \r\n");
        this.response.writeBytes("Content-Length: " + contentLength + "\r\n");
    }

    public void sendRedirect(String location) throws IOException {
        this.response.writeBytes("HTTP/1.1 302 Found \r\n");
        this.response.writeBytes("Location: " + location + "\r\n");
        processHeaders();

        this.response.flush();
    }

    private void processHeaders() throws IOException {
        for (String header : headers.keySet()) {
            this.response.writeBytes(header + ": " + headers.get(header) + "\r\n");
        }
        response.writeBytes("\r\n");
    }
}
