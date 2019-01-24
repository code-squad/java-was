package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Map;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String USER_CREATE = "/user/create";
    private static final String GET_METHOD = "GET";
    private static final String SUFFIX_HTML = "html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String headerFirstLine = br.readLine();
            String[] tokens = headerFirstLine.split(" ");
            byte[] body = "Hello World".getBytes();

            if(tokens[0].equals(GET_METHOD)) {
                log.debug("tokens[1] : {}", tokens[1]);
                if(tokens[1].endsWith(SUFFIX_HTML)) {
                    log.debug("get html path : {}", tokens[1]);
                    body = Files.readAllBytes(new File("./webapp" + tokens[1]).toPath());
                }
                if(tokens[1].startsWith(USER_CREATE)){
                    body = "HelloCreate".getBytes();
                    String[] parts = tokens[1].split("\\?");
                    Map<String, String> parseValues = HttpRequestUtils.parseQueryString(parts[1]);
                    log.debug("map : {}", parseValues);
                    User newUser =
                            new User(parseValues.get("userId"), parseValues.get("password"), parseValues.get("name"), parseValues.get("email"));
                    log.debug("user : {}", newUser);
                    DataBase.addUser(newUser);
                    log.debug("db user : {}", DataBase.findUserById(parseValues.get("userId")));
                }
            }

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, body.length);
            responseBody(dos, body);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
