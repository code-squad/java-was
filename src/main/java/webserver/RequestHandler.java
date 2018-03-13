package webserver;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }
    // 어떤 요청이 어떤 요청인지 구분해야 할 것 같음.
    // requestLine 으로 요청 구분하고 요청에 따라 response 생성 다르게 해주어야함.
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            // request
            HttpRequest httpRequest = new HttpRequest(in);
            String HTTPMethod = httpRequest.getMethod();
            String URI = httpRequest.getRequestLine();

            // response
            HttpResponse httpResponse = new HttpResponse(out);
            String contentType = "text/html";
            if (HTTPMethod.equals("GET")) {
                handleGetRequest(httpRequest, URI, httpResponse, contentType);
                return;
            }
            handlePostRequest(httpRequest, URI, httpResponse);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void handlePostRequest(HttpRequest httpRequest, String URI, HttpResponse httpResponse) {
        if(URI.contains("/user/create")) {
            httpResponse.responseUserSignUp(httpRequest, "/index.html");
            return;
        }
        // signIn
        if(URI.contains("/user/login")) {
            String userId = httpRequest.getParameter("userId");
            httpResponse.responseUserSignIn(userId);
            return;
        }
    }

    private void handleGetRequest(HttpRequest httpRequest, String URI, HttpResponse httpResponse, String contentType) throws IOException {
        if(httpRequest.getContentType().contains("text/css")){
            contentType = "text/css";
        }
        if(URI.contains("/user/create")) {
            // create user(get)
            String queryString = httpRequest.getQueryString(URI);
            httpResponse.createUser(httpRequest, queryString);
            httpResponse.sendRedirect("/index.html");
            return;
        }
        // user list
        if(URI.contains("/user/list")) {
            if(httpRequest.getCookieValue()){// in logined status
                // 사용자 목록 출력
                DataBase db = new DataBase();
                // Collection to List
                List<User> users = db.findAll().stream().collect(Collectors.toList());
                byte[] body = httpResponse.createDynamicHTML("./webapp/user/list_static.html", users);
                httpResponse.forward(URI, contentType, body);
                return;
            }
            // move to login page
            httpResponse.responseWithCookie(false, "/user/login.html");
            return;
        }
        // read file
        httpResponse.forward(URI, contentType, httpResponse.readFileToByte(URI));
        return;
    }

}
