package webserver;

import java.io.*;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

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

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line == null)
                return;

            String path = HttpRequestUtils.getURL(line);
            String queryString = HttpRequestUtils.getQueryString(path);
            queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8.toString());
            Map<String, String> map = HttpRequestUtils.parseQueryString(queryString);
            log.debug("path : {} ", path);
            User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
            log.debug("User : {}", user);
            log.debug("userId : {} , password : {}, name : {}, email : {} ", map.get("userId"), map.get("password"), map.get("name"), map.get("email"));

/*            while (!"".equals(line)) {
                log.debug("br : {} ", line);
                line = br.readLine();
                if (line == null)
                    return;
            }*/
            byte[] body = null;
            if (path != null) {
                body = Files.readAllBytes(new File("./webapp" + path).toPath());
                response200Header(dos, body.length);
                log.debug("body : {}", body.length);
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

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
