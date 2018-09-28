package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.ModelUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String HOME = "http://localhost:8080/index.html";
    private static final String LOGIN_FAIL = "http://localhost:8080/user/login_failed.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = null;
            String path = null;
            String url = null;
            String cookie = null;
            int contentLength = 0;
            int httpStatusCode = 200;
            Map<String, String> cookieData = new HashMap<>();

            // for header
            while ((line = reader.readLine()) != null && !line.equals("")) {
                log.debug("line : {}", line);

                // request line
                if (!line.contains(":")) {
                    path = HttpRequestUtils.parsePath(line);
                    log.debug("path : {}", path);
                }

                //content-length
                if (line.contains(":") && HttpRequestUtils.parseHeader(line).getKey().equals("Content-Length")) {
                    if (HttpRequestUtils.parseHeader(line).getValue() != null) {
                        contentLength = Integer.parseInt(HttpRequestUtils.parseHeader(line).getValue());
                        log.debug("content-length : {}", contentLength);
                    }
                }

                // cookie
                if (line.contains(":") && HttpRequestUtils.parseHeader(line).getKey().equals("Cookie")) {
                    String cookieQueryString = HttpRequestUtils.parseHeader(line).getValue();
                    cookieData = HttpRequestUtils.parseCookies(cookieQueryString);
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = null;

            if (path.equals("/user/create")) {
                httpStatusCode = 302;
                User user = ModelUtils.createUser(IOUtils.readData(reader, contentLength));
                DataBase.addUser(user);
                log.debug("user : {}", user.toString());
                url = HOME;
            }

            if (path.equals("/user/login")) {
                httpStatusCode = 302;
                Map<String, String> loginData = HttpRequestUtils.parseQueryString(IOUtils.readData(reader, contentLength));
                User loginUser = DataBase.findUserById(loginData.get("userId"));
                log.debug("login user : {}", loginUser.toString());
                url = HOME;
                cookie = "logined=true;";

                if (loginUser == null || !loginUser.getPassword().equals(loginData.get("password"))) {
                    // redirect failed
                    httpStatusCode = 302;
                    cookie = "logined=false;";
                    url = LOGIN_FAIL;
                }
                //                dos.writeBytes("Cookie: logined=true\r\n");
            }

            StringBuilder sb = new StringBuilder();
            if (path.equals("/user/list")) {
                // 회원가입도 없이 그냥 접근할 때
                httpStatusCode = 302;
                url = HOME;

                if (!cookieData.isEmpty() && cookieData.get("logined").equals("false")) {
                    httpStatusCode = 302;
                    url = LOGIN_FAIL;
                }
                if (!cookieData.isEmpty() && cookieData.get("logined").equals("true")) {
                    httpStatusCode = 200;
                    sb.append("<html>").append("<head>").append("<body>");
                    List<User> userData = new ArrayList<>(DataBase.findAll());
                    for (User user : userData) {
                        sb.append("user name : ").append(user.getUserId()).append("\r\n");
                    }
                    sb.append("</body>").append("</head>").append("</html>");
                    body = sb.toString().getBytes();
                    path = "";
                }
            }

            log.debug("httpStatusCode : {}", httpStatusCode);

            if (httpStatusCode == 302) {
                response302Header(dos, url, cookie);
                responseBody(dos, new byte[0]);
            }

            if (httpStatusCode == 200) {
                if (!path.equals("")) {
                    body = HttpRequestUtils.readFile(path);
                }
                response200Header(dos, body.length, cookie);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            if (cookie != null) {
                dos.writeBytes("Set-Cookie: " + cookie + " Path=/" + "\r\n");
            }

            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String url, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            if (cookie != null) {
                dos.writeBytes("Set-Cookie: " + cookie + " Path=/" + "\r\n");
            }
            dos.writeBytes("Location: " + url + "\r\n");
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
