package util;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class HttpResponseUtils {
    private static final Logger log = LoggerFactory.getLogger(HttpResponseUtils.class);

    public static void readUserList(DataOutputStream dos, List<User> users) throws IOException {
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
        response200Header(dos, html.length());
        responseBody(dos, html.getBytes());
    }

    public static void readLoginFailed(DataOutputStream dos) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
        response401Header(dos, body.length);
        responseBody(dos, body);
    }

    public static void redirect(DataOutputStream dos, String location) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: " + location + "\r\n");
        dos.writeBytes("\r\n");
    }

    public static void redirectWithCookie(DataOutputStream dos, String sessionId) throws IOException {
        dos.writeBytes("HTTP/1.1 302 Found \r\n");
        dos.writeBytes("Location: /\r\n");
        dos.writeBytes("Set-Cookie: JSESSIONID=" + sessionId + "; Path=/" + "\r\n");
        dos.writeBytes("\r\n");
    }

    public static void readCss(DataOutputStream dos, String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        responseCssHeader(dos, body.length);
        responseBody(dos, body);
    }

    public static void readStaticFile(DataOutputStream dos, String url) throws IOException {
        byte[] body;
        if (Files.exists(Paths.get(new File("./webapp") + url))) {
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
            response200Header(dos, body.length);
            responseBody(dos, body);
            return;
        }
        body = "요청하신 페이지가 없습니다".getBytes();
        response404Header(dos, body.length);
        responseBody(dos, body);
    }

    private static void responseCssHeader(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private static void response200Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 200 OK \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }


    private static void response401Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 401 Unauthorized \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }

    private static void responseBody(DataOutputStream dos, byte[] body) throws IOException {
        dos.write(body, 0, body.length);
        dos.flush();
    }

    private static void response404Header(DataOutputStream dos, int lengthOfBodyContent) throws IOException {
        dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
        dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
        dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
        dos.writeBytes("\r\n");
    }
}
