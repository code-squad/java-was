package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;

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
            String url = tokens[1];
            log.debug("url : {}", url);
            String queryString = url.substring(url.indexOf('?') + 1);
            log.debug("queryString : {}", queryString);
            Map<String, String> parmeterMap = HttpRequestUtils.parseQueryString(queryString);
            User newUser = new User(parmeterMap);
            log.debug("user : {}", newUser);
            byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());

            log.debug("body : {}", new String(body, "UTF-8"));
            while (true) {
                String line = br.readLine();
                if ("".equals(line)) {
                    break;
                }
                log.debug(line);
            }
            DataOutputStream dos = new DataOutputStream(out);
//            byte[] body = "Hello World".getBytes();
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
