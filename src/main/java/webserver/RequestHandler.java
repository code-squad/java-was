package webserver;

import constants.HttpMethod;
import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
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
            String requestHeader = br.readLine();
            String[] tokens = requestHeader.split(" ");
            HttpMethod httpMethod = HttpMethod.valueOf(tokens[0]);
            log.debug("httpMethod : {}", httpMethod);
            String url = tokens[1];
            log.debug("url : {}", url);

            switch (httpMethod) {
                case GET:
                    httpGetRequestHandler(out, url);
                    break;
                case POST:
                    httpPostRequestHandler(out, br, url);
                    break;
                case PUT:
                    break;
                case DELETE:
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void httpPostRequestHandler(OutputStream out, BufferedReader br, String url) throws IOException {
        int contentLength = 0;

        while (true) {
            String line = br.readLine();
            if ("".equals(line)) {
                break;
            }
            if(line.contains("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }
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
        String setCookie = "logined=false;";
        if (loginUser != null && loginUser.getPassword().equals(inputPassword)) {
            setCookie = "logined=true;";
        }
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, setCookie);
    }

    private void userCreateRequestHandler(OutputStream out, Map<String, String> parameterMap) {
        User newUser = new User(parameterMap);
        DataBase.addUser(newUser);
        log.debug("user : {}", newUser);
        DataOutputStream dos = new DataOutputStream(out);
        String redirectUrl = "/index.html";
        response302Header(dos, redirectUrl);
    }

    private void httpGetRequestHandler(OutputStream out, String url) throws IOException {
        byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
        log.trace("body : {}", new String(body, "UTF-8"));
        DataOutputStream dos = new DataOutputStream(out);
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
    private void response200Header(DataOutputStream dos, String setCookie) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: " + setCookie + " Path=/\r\n");
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
