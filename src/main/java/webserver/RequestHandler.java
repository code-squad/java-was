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
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String HOME = "http://localhost:8080/index.html";

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
            String line;
            String path = null;
            String url = null;
            int contentLength = 0;
            int httpStatusCode = 200;

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
            }

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
                url = HOME;

                if(loginUser == null || !loginUser.getPassword().equals(loginData.get("password"))) {
                    // redirect failed
                    url = "http://localhost:8080/user/login_failed.html";
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            byte[] body;

            log.debug("httpStatusCode : {}", httpStatusCode);

            if (httpStatusCode == 302) {
                response302Header(dos, url);
                responseBody(dos, new byte[0]);
            }

            if (httpStatusCode == 200) {
                body = HttpRequestUtils.readFile(path);
                response200Header(dos, body.length);
                responseBody(dos, body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
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
            dos.writeBytes("Location: " + url);
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
