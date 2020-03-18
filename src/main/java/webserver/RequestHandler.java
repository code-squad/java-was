package webserver;

import constants.HttpMethod;
import db.DataBase;
import model.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
            String url = httpRequest.getPath();
            switch (httpRequest.getHttpMethod()) {
                case GET:
                    httpGetRequestHandler(out, br, url);
                    break;
                case POST:
                    httpPostRequestHandler(out, br, url);
                    break;
                case PUT:
                    break;
                case DELETE:
                    break;
                default:
                    log.error("405 Method Not Allowed");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void httpPostRequestHandler(OutputStream out, BufferedReader br, String url) throws IOException {
        Map<String, String> headers = readRequestHeader(br);
        int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
        String userParameter = IOUtils.readData(br, contentLength);
        Map<String, String> parameterMap = HttpRequestUtils.parseQueryString(userParameter);
        if (url.contains("create")) {
            userCreateRequestHandler(out, parameterMap);
        } else if (url.contains("login")) {
            loginRequestHandler(out, parameterMap);
        }
    }

    private void loginRequestHandler(OutputStream out, Map<String, String> parameterMap) {
        User loginUser = DataBase.findUserById(parameterMap.get("userId")).orElse(null);
        String inputPassword = parameterMap.get("password");
        boolean setCookie = false;
        if (loginUser != null && loginUser.getPassword().equals(inputPassword)) {
            setCookie = true;
        }
        DataOutputStream dos = new DataOutputStream(out);
        String redirectUrl = "/index.html";
        response302Header(dos, redirectUrl, setCookie);
    }

    private void userCreateRequestHandler(OutputStream out, Map<String, String> parameterMap) throws UnsupportedEncodingException {
        User newUser = new User(parameterMap);
        DataBase.addUser(newUser);
        log.debug("user : {}", newUser);
        DataOutputStream dos = new DataOutputStream(out);
        String redirectUrl = "/index.html";
        response302Header(dos, redirectUrl);
    }

    private void httpGetRequestHandler(OutputStream out, BufferedReader br, String url) throws IOException {
        if (url.contains("/user/list")) {
            showUserList(out, br, url);
            return;
        }
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        log.trace("body : {}", new String(body, "UTF-8"));
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private void showUserList(OutputStream out, BufferedReader br, String url) throws IOException {
        boolean isLogined;
        Map<String, String> requestHeaders = readRequestHeader(br);
        Map<String, String> cookies = HttpRequestUtils.parseCookies(requestHeaders.getOrDefault("Cookie", ""));
        isLogined = Boolean.parseBoolean(cookies.get("logined"));
        DataOutputStream dos = new DataOutputStream(out);
        if (!isLogined) {
            response302Header(dos, "/index.html", isLogined);
            return;
        }
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
        response200Header(dos, body.length);
        responseBody(dos, body);
    }

    private Map<String, String> readRequestHeader(BufferedReader br) throws IOException {
        Map<String, String> requestHeaders = new HashMap<>();

        while (true) {
            String line = br.readLine();

            if ("".equals(line)) {
                break;
            }
            String[] header = line.split(": ");
            requestHeaders.put(header[0], header[1]);
        }

        return requestHeaders;
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
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
            dos.writeBytes("Set-Cookie: logined=" + setCookie + "; Path=/\r\n");
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
