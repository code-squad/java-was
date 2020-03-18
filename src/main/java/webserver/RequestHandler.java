package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import db.DataBase;
import db.SessionDataBase;
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

            if (DataBase.getSizeOfUsers() == 0) {
                DataBase.addUser(new User("jay", "1234", "김자윤", "jay@gmail.com"));
            }

            String line = br.readLine();

            String url = HttpRequestUtils.getURL(line);

            Map<String, String> headers = new HashMap<>();


            if (url.equals("/")) url = "/index.html";

            if (url.equals("/user/create")) {
                while (!(line = br.readLine()).equals("")) {
                    HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                    headers.put(pair.getKey(), pair.getValue());
                }
                log.debug("content-Length : {}", headers.get("Content-Length"));

                String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));

                body = HttpRequestUtils.decode(body);

                Map<String, String> userMap = HttpRequestUtils.parseQueryString(body);

                User user = new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
                DataBase.addUser(user);
                log.debug("Database User : {}", DataBase.findUserById(userMap.get("userId")));
                response302Header(dos, "/");

            } else if (url.equals("/user/login")) {

                while (!(line = br.readLine()).equals("")) {
                    HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                    headers.put(pair.getKey(), pair.getValue());
                }
                log.debug("content-Length : {}", headers.get("Content-Length"));

                String body = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));

                body = HttpRequestUtils.decode(body);

                Map<String, String> userMap = HttpRequestUtils.parseQueryString(body);
                User loginUser = new User(userMap.get("userId"), userMap.get("password"));
                User dbUser = Optional.ofNullable(DataBase.findUserById(userMap.get("userId")))
                        .orElseThrow(() -> new NoSuchElementException("로그인을 시도한 유저가 존재하지 않습니다"));
                log.debug("Database User : {}", dbUser);

                if (dbUser.isSameUser(loginUser)) {
                    log.debug("logined Success");
                    String sessionId = UUID.randomUUID().toString().replace("-", "");
                    SessionDataBase.addSession(sessionId, dbUser);

                    log.debug("Session : {} ", SessionDataBase.getSessionedUser(sessionId));
                    response302HeaderWithCookie(dos, sessionId);
                } else {
                    log.debug("logined Fail");
                    byte[] failedBody = Files.readAllBytes(new File("./webapp/user/login_failed.html").toPath());
                    response401Header(dos, failedBody.length);
                    responseBody(dos, failedBody);
                }

            } else if (url.equals("/user/list")) {
                while (!(line = br.readLine()).equals("")) {
                    HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
                    headers.put(pair.getKey(), pair.getValue());
                }

                String cookie = headers.get("Cookie");
//                log.debug("Cookie : {}", cookie);
                String sessionId = cookie.split("=")[1];
//                log.debug("SessionId : {}", sessionId);

                if (SessionDataBase.isSessionIdExist(sessionId)) {
                    log.debug("로그인된 유저입니다");
                    byte[] userList = Files.readAllBytes(new File("./webapp" + url+".html").toPath());
                    response200Header(dos, userList.length);
                    responseBody(dos, userList);

                } else {
                    log.debug("해당 세션을 찾을 수 없다.");
                    response302Header(dos,"/user/login.html");
                }

            } else {
                byte[] body;

                if (Files.exists(Paths.get(new File("./webapp") + url))) {
                    body = Files.readAllBytes(new File("./webapp" + url).toPath());
                    response200Header(dos, body.length);
                } else {
                    body = "요청하신 페이지가 없습니다".getBytes();
                    response404Header(dos, body.length);
                }
                responseBody(dos, body);
            }
        } catch (IOException | NoSuchElementException e) {
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

    private void response302Header(DataOutputStream dos, String path) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: "+path+"\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302HeaderWithCookie(DataOutputStream dos, String sessionId) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /\r\n");
            dos.writeBytes("Set-Cookie: JSESSIONID=" + sessionId + "; Path=/" + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response401Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 401 Unauthorized \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
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
