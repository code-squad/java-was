package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.ModelUtils;

import java.io.*;
import java.net.Socket;

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

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            String path = null;
            String contentLength = null;
            String url = null;
            while ((line = reader.readLine()) != null && !"".equals(line)) {
                if (!line.contains(":")) {
                    path = HttpRequestUtils.parseHeaderPath(line);
                    log.debug("path : {}", path);
                }

                if (HttpRequestUtils.parsePathFromUrl(line).equals("/user/create")) {
                    url = "/user/create";
                }

                if (url.equals("/user/create") && line.equals("\n")) {
                    User user = ModelUtils.createUser(IOUtils.readData(reader, Integer.parseInt(contentLength)));
                    log.debug("user : {}", user.toString());
                }

                if (line.contains(":") && HttpRequestUtils.parseHeader(line).getKey().equals("Content-Length")) {
                    contentLength = HttpRequestUtils.parseHeader(line).getValue();
                    log.debug("content length : {}", contentLength);
                }


                log.debug("HTTP HEADER : {}", line);
                log.debug("PATH IN HEADER : {}", path);
            }

            DataOutputStream dos = new DataOutputStream(out);

            byte[] body = HttpRequestUtils.readFile(path);
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
