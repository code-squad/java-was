package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

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

    @Override
    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response;
            HttpHeaders headers = new HttpHeaders();

            if (request.getPath().startsWith("/user/create") && request.isMethod(HttpMethod.POST)) {
                Map<String, String> userParams = request.getParameters();
                User user = new User(userParams.get("userId"), userParams.get("password"), userParams.get("name"), userParams.get("email"));
                DataBase.addUser(user);
                log.debug("Created User: {}", user);

                headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8");
                headers.addHeader(HttpHeader.LOCATION, "/index.html");
                response = new HttpResponse(HttpStatus.FOUND, headers , Resource.ofEmpty());
            } else {
                response = new HttpResponse(HttpStatus.OK, headers, Resource.of(request.getPath()));
            }

            // GET login page
            if (request.getPath().equals("/user/login.html") && request.isMethod(HttpMethod.GET)) {
                headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8");
                response = new HttpResponse(HttpStatus.OK, headers, Resource.of("/login.html"));
            }

            // POST login
            if (request.getPath().startsWith("/user/login") && request.isMethod(HttpMethod.POST)) {
                Map<String, String> params = request.getParameters();

                User dbUser = DataBase.findUserById(params.get("userId"));
                if (dbUser.isMatch(params.get("password"))) {
                    //TODO: add cookie header & redirect location
                    headers.addHeader(HttpHeader.SET_COOKIE, "logined=true; Path=/");
                    headers.addHeader(HttpHeader.LOCATION, "/index.html");
                    response = new HttpResponse(HttpStatus.FOUND, headers, Resource.ofEmpty());
                } else {
                    //TODO: return login fail page
                    response = new HttpResponse(HttpStatus.FORBIDDEN, headers, Resource.of("/login_failed.html"));
                }
            }

            // GET user list
            if (request.getPath().equals("/user/list") && request.isMethod(HttpMethod.GET)) {
                String cookie = request.getCookie();
                if (cookie.equals("logined=true")) {
                    response = new HttpResponse(HttpStatus.OK, headers, Resource.of("/list.html"));
                } else {
                    response = new HttpResponse(HttpStatus.OK, headers, Resource.of("/login.html"));
                }
            }
            /*
            TODO: Ask about threading.
            Each handler is a tread and so they share heap memory. This means that every instance that is being
            instantiated in the process of creating a request/response object, can be accessed by multiple threads. So,
            in order to avoid collisions, it is best not to use singleton objects.
            */
            response.writeResponse(out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
