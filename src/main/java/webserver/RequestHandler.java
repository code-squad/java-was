package webserver;

import domain.Cookies;
import domain.HttpRequest;
import domain.HttpResponse;
import domain.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpRequestUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String HOME = "http://localhost:8080/index.html";
    private static final String LOGIN_FAIL = "http://localhost:8080/user/login_failed.html";

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {

            UserService userService = new UserService();

            // Http Request 생성
            HttpRequest request = new HttpRequest(in);

            // Reponse 생성
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse(dos);

            // 분기
            if (request.matchPath("/user/create")) {
                userService.save(userService.create(String.valueOf(request.getBody())));
                response.sendRedirect(HOME);
            }

            if (request.matchPath("/user/login")) {
                response.addCookie("logined", "true");
                response.sendRedirect(HOME);
                if (!userService.login(String.valueOf(request.getBody()))) {
                    response.addCookie("logined", "false");
                    response.sendRedirect(LOGIN_FAIL);
                }
            }

            if (request.matchPath("/user/list")) {
                if (request.matchCookieValue("logined", "false")) {
                    response.sendRedirect(HOME);
                }

                if (request.matchCookieValue("logined", "true")) {
                    byte[] body = userService.list().getBytes();
                    response.response200Header(body.length).responseBody(body);
                }
            }

            // TODO path가 기준이 되어 컨트롤러가 만들어져야 한다
            // path를 split하는데, 구분자는 /(slash)이다
            if (request.matchPath("/index.html")) {
                byte[] body = HttpRequestUtils.readFile(request.getPath());
                response.response200Header(body.length).responseBody(body);
            }

            if (request.matchPath("/user/form.html")) {
                byte[] body = HttpRequestUtils.readFile(request.getPath());
                response.response200Header(body.length).responseBody(body);
            }

            if (request.matchPath("/user/login.html")) {
                byte[] body = HttpRequestUtils.readFile(request.getPath());
                response.response200Header(body.length).responseBody(body);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
