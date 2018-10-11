package webserver;

import domain.Cookies;
import domain.HttpRequest;
import domain.HttpResponse;
import domain.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import util.HttpRequestUtils;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private static final String HOME = "http://localhost:8080/index.html";
    private static final String LOGIN_FAIL = "http://localhost:8080/user/login_failed.html";

    private static final byte[] EMPTY_BODY = new byte[0];

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            UserService userService = new UserService();

            // Http Request 생성
//            HttpRequest request = HttpRequestUtils.getHttpRequest(reader);
            HttpRequest request = new HttpRequest(in);

            // Path
            // String path = request.getPath();

            DataOutputStream dos = new DataOutputStream(out);
            String url = null;
            byte[] body = EMPTY_BODY;

//            Cookies requestCookies = request.getCookies();
            Cookies responseCookies = new Cookies(new HashMap<>());
            log.debug("httpRequest : {}", request.toString());

            // Reponse 생성
            HttpResponse response = new HttpResponse(dos, HttpStatusCode.OK);

            if (request.matchPath("/user/create")) {
                response = new HttpResponse(dos, HttpStatusCode.FOUND);
                userService.save(userService.create(String.valueOf(request.getBody())));
                response.response302Header(HOME, responseCookies);
            }

            if (request.matchPath("/user/login")) {
                response = new HttpResponse(dos, HttpStatusCode.FOUND);
                url = HOME;
                responseCookies.add("logined", "true");
                if (!userService.login(String.valueOf(request.getBody()))) {
                    url = LOGIN_FAIL;
                    responseCookies.add("logined", "false");
                }
            }

            if (request.matchPath("/user/list")) {
                response = new HttpResponse(dos, HttpStatusCode.FOUND);
                url = HOME;

                // TODO isLogined()로 체크할 수 있게
                if (request.matchCookieValue("logined", "false")) {
                    response = new HttpResponse(dos, HttpStatusCode.FOUND);
                    url = LOGIN_FAIL;
                }

                if (request.matchCookieValue("logined", "true")) {
                    response = new HttpResponse(dos, HttpStatusCode.OK);
                    body = userService.list().getBytes();
                }
            }

            // response 완성
            if (response.matchStatusCode(HttpStatusCode.OK)) {
//                if (!path.equals("")) {
//                    body = HttpRequestUtils.readFile(request.getPath());
//                }
                body = HttpRequestUtils.readFile(request.getPath());
                response.response200Header(body.length, responseCookies).responseBody(body);
            }

            if (response.matchStatusCode(HttpStatusCode.FOUND)) {
                response.response302Header(url, responseCookies).responseBody(body);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
