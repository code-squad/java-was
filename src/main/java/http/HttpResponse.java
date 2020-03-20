package http;


import model.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        if (Files.exists(Paths.get(new File("./webapp") + path))) {
            body = Files.readAllBytes(new File("./webapp" + path).toPath());
            response200Header(body.length);
            responseBody(body);
            return;
        }
        body = "요청하신 페이지가 없습니다".getBytes();
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

    public void readLoginFailed() throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
        response401Header(body.length);
        responseBody(body);
    }

    public void readUserList(List<User> users) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
        StringBuilder list = new StringBuilder(new String(body));
        int index = list.indexOf("<table class=\"table table-hover\">");
        String temp = "<table class=\"table table-hover\">";
        StringBuilder sb = new StringBuilder();
        sb.append("<thead>");
        sb.append("<tr>");
        sb.append("<th>#</th> <th>사용자 아이디</th> <th>이름</th> <th>이메일</th><th></th>");
        sb.append("</tr>");
        sb.append("</thead>");
        sb.append("<tbody>");

        for (int i = 0; i < users.size(); i++) {
            sb.append("<tr>");
            sb.append("<th scope=\"row\">").append(i + 1).append("</th>");
            sb.append("<td>").append(users.get(i).getUserId()).append("</td>");
            sb.append("<td>").append(users.get(i).getName()).append("</td>");
            sb.append("<td>").append(users.get(i).getEmail()).append("</td>");
            sb.append("<td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>");
            sb.append("</tr>");
        }
        sb.append("</tbody>");

        list.insert(index + temp.length(), sb);
        String html = list.toString();
        response200Header(html.length());
        responseBody(html.getBytes());
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
