package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

import javax.swing.text.html.Option;

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
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            List<String> request = HttpRequestUtils.readWholeRequest(br);
            log.debug("request: {}", request);
            DataOutputStream dos = new DataOutputStream(out);

            handleRequest(request, dos);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handleRequest(List<String> request, DataOutputStream dos) throws IOException {
        String line = request.get(0);
        String[] urlTokens = line.split(" ");
        String httpMethod = urlTokens[0];
        String address = urlTokens[1];
        String contentType = HttpRequestUtils.getContentType(request);
        boolean hasLoginUser = HttpRequestUtils.getLoginStatus(request);
        log.debug("loginUser {}", hasLoginUser);

        Map<String, String> queries = null;

        if (httpMethod.equals("POST")) {
            String requestBody = HttpRequestUtils.getRequestBody(request);
            queries = HttpRequestUtils.parseQueryString(requestBody);
        }

        if (httpMethod.equals("GET")) {
            String[] queryTokens = address.split("\\?");
            address = queryTokens[0];

            if(queryTokens.length > 1) {
                queries = HttpRequestUtils.parseQueryString(queryTokens[1]);
            }
        }

        handleQueries(address, queries, contentType, hasLoginUser, dos);
    }

    private void handleQueries(String address,
                                 Map<String, String> queries,
                                 String contentType,
                                 boolean hasLoginUser,
                                 DataOutputStream dos) throws IOException {
        if (address.equals("/user/create")) {
            DataBase.addUser(new User(queries.get("userId"),
                    queries.get("password"),
                    queries.get("name"),
                    queries.get("email")));

            response302Header(dos, "/index.html");
            return;
        }

        if (address.equals("/user/login")) {
            Optional<User> maybeUesr = Optional.ofNullable(DataBase.findUserById(queries.get("userId")));

            if(maybeUesr.isPresent()) {
                response302Header(dos, "/index.html", "logined=true; Path=/");
                return;
            }

            response302Header(dos, "/user/login_failed.html", "logined=false; Path=/");
            return;
        }

        if (address.equals("/user/list")) {
            if(hasLoginUser)  {
                Collection<User> users = DataBase.findAll();
                byte[] body = HttpRequestUtils.getUserListBody(users);
                response200Header(dos, body.length, contentType);
                responseBody(dos, body);

                return;
            } else {
                response302Header(dos, "/user/login.html");
                return;
            }
        }

        byte[] body = HttpRequestUtils.getResource(address);

        response200Header(dos, body.length, contentType);
        responseBody(dos, body);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType + ";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location, String cookie) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND\r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Set-Cookie: " + cookie + "\r\n");
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
