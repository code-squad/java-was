package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }
    // 어떤 요청이 어떤 요청인지 구분해야 할 것 같음.
    // requestLine 으로 요청 구분하고 요청에 따라 response 생성 다르게 해주어야함.
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // request
            HttpRequest httpRequest = new HttpRequest(in);
            // response
            DataOutputStream dos = new DataOutputStream(out);
            String HTTPMethod = httpRequest.getHTTPMethod();
            String URI = httpRequest.getURI();
            List<String> requestHeader = httpRequest.getRequestHeader();
            log.debug("requestHeader : {}", requestHeader.toString());
            if (HTTPMethod.equals("GET")) {
                if (!URI.contains("?")) {
                    readFile(dos, URI);
                    return;
                }
                // create user
//                String queryString = httpRequest.getQueryString(URI);
////                createUser(httpRequest, queryString);
////                response302Header(dos);
////                return;
            }
            String requestBody = httpRequest.getRequestBody();
            createUser(httpRequest, requestBody);
            response302Header(dos);
            return;
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void createUser(HttpRequest httpRequest, String queryString) {
        Map<String, String> parameters = httpRequest.getRequestParameter(queryString);
        User user = new User(parameters.get("userId"), parameters.get("password"), parameters.get("name"), parameters.get("email"));
        DataBase db = new DataBase();
        db.addUser(user);
    }

    private void readFile(DataOutputStream dos, String URI) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + URI).toPath());
        response200Header(dos, body.length);
        responseBody(dos, body);
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


    private void response302Header(DataOutputStream dos) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html");
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
