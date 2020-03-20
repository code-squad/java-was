package http;


import model.User;
import util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {

    private Map<String, String> header;
    private DataOutputStream dos;

    public HttpResponse() {
        header = new HashMap<>();
    }

    public HttpResponse(DataOutputStream dos) {
        header = new HashMap<>();
        this.dos = dos;
    }

//    public void addHeader(String key, String value) {
//        header.put(key, value);
//    }

    public void forward(String path) throws IOException {
        byte[] body;
        if (HttpResponseUtils.isFileExist(path)) {
            body = HttpResponseUtils.readFile(path);
            response200Header(body.length);
            responseBody(body);
            return;
        }
        body = HttpResponseUtils.notExistPage();
        response404Header(body.length);
        responseBody(body);
    }

    public void forwardBody(String responseBody) {

    }

    public void response200Header(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    public void sendRedirect(String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        dos.writeBytes("\r\n");
    }

    public void sendRedirect(String location, String sessionId) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        dos.writeBytes("Set-Cookie: JSESSIONID=" + sessionId + "; Path=/" + "\r\n");
        dos.writeBytes("\r\n");
    }

    public void readUserList(List<User> users) throws IOException {
        byte[] body = HttpResponseUtils.readFile("/user/list.html");
        String userListHtml = HttpResponseUtils.getUserListHTML(body, users);
        response200Header(userListHtml.length());
        responseBody(userListHtml.getBytes());
    }

    public void readCss(String path) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
        responseCssHeader(body.length);
        responseBody(body);
    }

    public String processHeaders() {
        StringBuilder sb = new StringBuilder();
        header.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .forEach(header -> sb.append(header).append("\r\n"));

        sb.append("\r\n");
        return sb.toString();
    }

    private void responseCssHeader(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void response401Header(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 401 Unauthorized \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private void responseBody(byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private void response404Header(int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }
}
