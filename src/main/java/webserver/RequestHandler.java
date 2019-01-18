package webserver;

import model.Url;
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
    public static final String EMPTY = "";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.

            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = br.readLine();
            Url url = Url.of(line);

            int contentLength = 0;
            while (!EMPTY.equals(line)) {
                line = br.readLine();
                if (line.contains("Content-Length")) {
                    contentLength = Integer.parseInt(HttpRequestUtils.parseHeader(line).getValue());
                }
                log.debug(line);
            }

            String bodyText = IOUtils.readData(br, contentLength);
            if(url.getAccessPath().equals("/user/create")) {
                Map<String, String> bodyVal = HttpRequestUtils.parseQueryString(bodyText);
                User user = new User(bodyVal.get("userId"),
                        bodyVal.get("password"),
                        bodyVal.get("name"),
                        bodyVal.get("email"));
                log.debug(user.toString());
            }
            byte[] body = Files.readAllBytes(new File(url.generate()).toPath());

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
