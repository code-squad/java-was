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

            if (url.equals("/user/create")) {
                header = parseHeader(br);

//                log.debug("content-Length : {}", header.get("Content-Length"));

                String body = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));

                body = HttpRequestUtils.decode(body);

                Map<String, String> userMap = HttpRequestUtils.parseQueryString(body);

                User user = new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email"));
                DataBase.addUser(user);

                log.debug("Database User : {}", DataBase.findUserById(userMap.get("userId")));
                HttpResponseUtils.redirect(dos, "/");

            } else if (url.equals("/user/login")) {
                header = parseHeader(br);
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

            } else if (url.contains("list.html")) {
                header = parseHeader(br);
                String cookie = header.get("Cookie");
                String sessionId = cookie.split("=")[1];

                if (SessionDataBase.isSessionIdExist(sessionId)) {
                    log.debug("로그인된 유저입니다");
                    List<User> users = new ArrayList<>(DataBase.findAll());
                    HttpResponseUtils.readUserList(dos, users);
                    return;
                }
                log.debug("해당 세션을 찾을 수 없다.");
                HttpResponseUtils.redirect(dos, "/user/login.html");

            } else if (url.contains(".css")) {
                HttpResponseUtils.readCss(dos, url);
            } else {
                HttpResponseUtils.readStaticFile(dos, url);
            }
        } catch (IOException | NoSuchElementException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> parseHeader(BufferedReader br) throws IOException {
        Map<String, String> header = new HashMap<>();
        String requestLine;

        while (!(requestLine = br.readLine()).equals("")) {
            HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(requestLine);
            header.put(pair.getKey(), pair.getValue());
        }
        return header;
    }
}
