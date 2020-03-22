package webserver;

import db.DataBase;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

import static constants.CommonConstants.*;
import static constants.ContentTypeConstants.TEXT_CSS;
import static constants.ErrorConstants.METHOD_NOT_ALLOWED;
import static constants.RequestHeaderConstants.CONTENT_LENGTH;
import static constants.RequestHeaderConstants.COOKIE;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            HttpRequest httpRequest = new HttpRequest(br);
            if (httpRequest.isUrlNull()) {
                log.error("400 Bad Request");
                return;
            }
            switch (httpRequest.getHttpMethod()) {
                case GET:
                    httpGetRequestHandler(out, httpRequest);
                    break;
                case POST:
                    httpPostRequestHandler(out, httpRequest);
                    break;
                case PUT:
                    break;
                case DELETE:
                    break;
                default:
                    log.error(METHOD_NOT_ALLOWED);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void httpPostRequestHandler(OutputStream out, HttpRequest httpRequest) throws IOException {
        String url = httpRequest.getUrl();
        if (url.contains("create")) {
            userCreateRequestHandler(out, httpRequest);
        } else if (url.contains("login")) {
            loginRequestHandler(out, httpRequest);
        }
    }

    private void loginRequestHandler(OutputStream out, HttpRequest httpRequest) {
        User loginUser = DataBase.findUserById(httpRequest.getParams().get(USER_ID)).orElse(null);
        String inputPassword = httpRequest.getParams().get(PASSWORD);
        boolean setCookie = false;
        if (loginUser != null && loginUser.getPassword().equals(inputPassword)) {
            setCookie = true;
        }
        DataOutputStream dos = new DataOutputStream(out);
        String redirectUrl = "/index.html";
        response302Header(dos, redirectUrl, setCookie);
    }

    private void userCreateRequestHandler(OutputStream out, HttpRequest httpRequest) throws UnsupportedEncodingException {
        User newUser = new User(httpRequest.getParams());
        DataBase.addUser(newUser);
        log.debug("user : {}", newUser);
        DataOutputStream dos = new DataOutputStream(out);
        String redirectUrl = "/index.html";
        response302Header(dos, redirectUrl);
    }

    private void httpGetRequestHandler(OutputStream out, HttpRequest httpRequest) throws IOException {
        String url = httpRequest.getUrl();
        if (url.contains("/user/list")) {
            showUserList(out, httpRequest);
            return;
        }
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        log.trace("body : {}", new String(body, UTF_8));
        DataOutputStream dos = new DataOutputStream(out);
        if (url.endsWith(".css")) {
            response200Header(dos, body.length, TEXT_CSS);
        } else {
            response200Header(dos, body.length);
        }
        responseBody(dos, body);
    }

    private void showUserList(OutputStream out, HttpRequest httpRequest) throws IOException {
        boolean isLogined;
        String url = httpRequest.getUrl();
        Map<String, String> cookies = httpRequest.getCookies();
        isLogined = Boolean.parseBoolean(cookies.get(LOGIN_COOKIE_ID));
        DataOutputStream dos = new DataOutputStream(out);
        if (!isLogined) {
            response302Header(dos, "/index.html", isLogined);
            return;
        }
        byte[] body = hardBar(url);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private byte[] hardBar(String url) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<tbody>");
        Collection<User> users = DataBase.findAll();
        int i = 1;
        for (User user : users) {
            sb.append("<tr>").append("<th scope=\"row\">")
                    .append(i).append("</th> <td>").append(user.getUserId())
                    .append("</td> <td>").append(user.getName())
                    .append("</td> <td>").append(user.getEmail())
                    .append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>").append("</tr>");
            i++;
        }
        sb.append("</tbody>");
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        body = new String(body, StandardCharsets.UTF_8).replaceAll("\\{\\{users}}", sb.toString()).getBytes();
        return body;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes(CONTENT_LENGTH + ": " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + "\r\n");
            dos.writeBytes(CONTENT_LENGTH + ": " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url, boolean setCookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + url + " \r\n");
            dos.writeBytes("Set-Cookie: " + LOGIN_COOKIE_ID + "=" + setCookie + "; Path=/\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
