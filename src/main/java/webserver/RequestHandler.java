package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

import db.DataBase;
import db.SessionDataBase;
import http.HttpRequest;
import http.HttpResponse;
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
            HttpRequest httpRequest = new HttpRequest(br);
            HttpResponse httpResponse = new HttpResponse(dos);

            DataBase.addUser(new User("jay", "1234", "제이", "jay@gmail.com"));

            String url = httpRequest.getPath();
            Map<String, String> header;

            log.error("url : {}", url);

            switch (url) {
                case "/user/create": {
                    String userId = httpRequest.getParameter("userId");
                    String password = httpRequest.getParameter("password");
                    String name = httpRequest.getParameter("name");
                    String email = httpRequest.getParameter("email");

                    User user = new User(userId, password, name, email);
                    DataBase.addUser(user);
                    log.debug("Database User : {}", DataBase.findUserById(userId));
                    httpResponse.sendRedirect("/");
                    break;
                }

                case "/user/login": {
                    String userId = httpRequest.getParameter("userId");
                    String password = httpRequest.getParameter("password");
                    User loginUser = new User(userId, password);

                    try {
                        User dbUser = DataBase.findUserById(userId);
                        if (dbUser.isSameUser(loginUser)) {
                            log.debug("로그인 성공");
                            String sessionId = SessionUtils.createSessionId();
                            SessionDataBase.addSession(sessionId, dbUser);
                            httpResponse.sendRedirect("/", sessionId);
                            return;
                        }
                    } catch (NoSuchElementException e) {
                        log.error("존재하지 않는 사용자입니다.");
                    }

                    log.debug("로그인 실패");
                    httpResponse.forward(HttpResponseUtils.PAGE_LOGIN_FAILED);
                    break;
                }

                case "/user/list.html": {
                    String cookie = httpRequest.getHeader("Cookie");
                    String sessionId = HttpRequestUtils.parseCookies(cookie).get("JSESSIONID");

                    if (SessionDataBase.isSessionIdExist(sessionId)) {
                        log.debug("로그인된 유저입니다");
                        List<User> users = new ArrayList<>(DataBase.findAll());
                        httpResponse.readUserList(users);
                        return;
                    }

                    log.debug("해당 세션을 찾을 수 없다.");
                    httpResponse.sendRedirect("/user/login.html");
                    break;
                }

                default:
                    if (url.contains(".css")) {
                        httpResponse.readCss(url);
                        return;
                    }
                    httpResponse.forward(url);
                    break;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void getDefaultPath(String url) {

    }
}
