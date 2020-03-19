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
import util.HttpResponseUtils;
import util.IOUtils;
import util.SessionUtils;


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

            // For.Test
            if (DataBase.getSizeOfUsers() == 0) {
                DataBase.addUser(new User("jay", "1234", "김자윤", "jay@gmail.com"));
            }

            String requestLine = br.readLine();

            String url = HttpRequestUtils.getURL(requestLine);

            Map<String, String> header;

            if (url.equals("/")) url = "/index.html";

            switch (url) {
                case "/user/create": {
                    header = HttpRequestUtils.extractHeader(br);

//                log.debug("content-Length : {}", header.get("Content-Length"));

                    String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));

                    body = HttpRequestUtils.decode(body);

                    Map<String, String> userMap = HttpRequestUtils.parseQueryString(body);

                    User user = new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
                    DataBase.addUser(user);

                    log.debug("Database User : {}", DataBase.findUserById(userMap.get("userId")));
                    HttpResponseUtils.redirect(dos, "/");

                    break;
                }
                case "/user/login": {
                    header = HttpRequestUtils.extractHeader(br);
                    String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
                    body = HttpRequestUtils.decode(body);
                    Map<String, String> userMap = HttpRequestUtils.parseQueryString(body);
                    User loginUser = new User(userMap.get("userId"), userMap.get("password"));
                    User dbUser = Optional.ofNullable(DataBase.findUserById(userMap.get("userId")))
                            .orElseThrow(() -> new NoSuchElementException("로그인을 시도한 유저가 존재하지 않습니다"));

                    if (dbUser.isSameUser(loginUser)) {
                        log.debug("로그인 성송");
                        String sessionId = SessionUtils.createSessionId();
                        SessionDataBase.addSession(sessionId, dbUser);
                        HttpResponseUtils.redirectWithCookie(dos, sessionId);
                        return;
                    }
                    log.debug("로그인 실패");
                    HttpResponseUtils.readLoginFailed(dos);

                    break;
                }
                case "/user/list.html":
                    header = HttpRequestUtils.extractHeader(br);
                    String cookie = header.get("Cookie");
                    String sessionId = HttpRequestUtils.parseCookies(cookie).get("JSESSIONID");

                    if (SessionDataBase.isSessionIdExist(sessionId)) {
                        log.debug("로그인된 유저입니다");
                        List<User> users = new ArrayList<>(DataBase.findAll());
                        HttpResponseUtils.readUserList(dos, users);
                        return;
                    }
                    log.debug("해당 세션을 찾을 수 없다.");
                    HttpResponseUtils.redirect(dos, "/user/login.html");

                    break;
                default:
                    if (url.contains(".css")) {
                        HttpResponseUtils.readCss(dos, url);
                        return;
                    }
                    HttpResponseUtils.readStaticFile(dos, url);
                    break;
            }
        } catch (IOException | NoSuchElementException e) {
            log.error(e.getMessage());
        }
    }
}
