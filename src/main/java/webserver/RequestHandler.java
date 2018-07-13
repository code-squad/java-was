package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HeaderPathUtils;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    RequestHandler(Socket connectionSocket) {
        connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = null;
            br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            String line = "";
            String path = "";
            boolean isPost = false;

            try {
                line = br.readLine();
                path = HeaderPathUtils.extractPath(line);

                log.debug("path : {}", path);

                if (HeaderPathUtils.checkPost(line)) {
                    isPost = true;
                    String contentLength = "";

                    br.readLine();

                    while (!"".equals(line)) {
                        line = br.readLine();

                        if (line == null || line.equals("")) {
                            break;
                        }

                        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                        if (pair.getKey().equals("Content-Length")) {
                            contentLength = pair.getValue();
                        }
                    }

                    String data = IOUtils.readData(br, Integer.parseInt(contentLength));
                    log.debug("data : {}", data);

                    User user = new User(data);
                    log.debug("user : {}", user);

                    DataBase.addUser(user);
                    User foundUser = DataBase.findUserById(user.getUserId());
                    log.debug("found user : {}", foundUser);

                    path = "/user/list.html";
                } else {
                    if (path.startsWith("/user/create")) {
                        String queryPath = HeaderPathUtils.extractPath(line);
                        String query = HeaderPathUtils.extractQuery(queryPath);
                        User user = new User(query);
                        log.debug("user : {}", user);

                        DataBase.addUser(user);
                        User foundUser = DataBase.findUserById(user.getUserId());
                        log.debug("found user : {}", foundUser);

                        path = "/user/list.html";
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

            DataOutputStream dos = new DataOutputStream(out);
            if (isPost) {
                response302Header(dos, path);
            } else {
                response200Header(dos, body.length);
            }
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

    private void response302Header(DataOutputStream dos, String location) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
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
