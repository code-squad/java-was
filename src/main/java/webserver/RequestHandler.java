package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;


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
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line = br.readLine();

            String url = HttpRequestUtils.getURL(line); // index.html ? /users/create

            Map<String, String> headers = new HashMap<>();
            byte[] body = null;

            if (url.equals("/")) url = "/index.html";

            if (url.equals("/user/create")) {
                while (!(line = br.readLine()).equals("")) {
                    log.debug("br : {} ", line);
                    String[] tokens = line.split(": ");
                    headers.put(tokens[0], tokens[1]);
                }
                log.debug("content-Length : {}", headers.get("Content-Length"));

                String bodyString = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
                log.debug("Body : {}", bodyString);
                bodyString = URLDecoder.decode(bodyString, StandardCharsets.UTF_8.toString());
                Map<String, String> map = HttpRequestUtils.parseQueryString(bodyString);

                User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));

                log.debug("User : {}", user);
            }
            body = Files.readAllBytes(new File("./webapp" + url).toPath());
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
