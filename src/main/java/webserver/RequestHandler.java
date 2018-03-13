package webserver;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
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
            HttpResponse httpResponse = new HttpResponse(out);
            // response
            // create user(get, post)
            Map<String, AbstractController> controllers = new HashMap<>();
            controllers.put("/user/create", new CreateUserController());
            controllers.put("/user/login", new LoginController());
            controllers.put("/user/list", new UserListController());

            String contentType = "text/html";
            if(httpRequest.getContentType().contains("text/css")){
                contentType = "text/css";
            }

            controllers.forEach((k, v) -> {
                if(httpRequest.getURI().contains(k)){
                    v.service(httpRequest, httpResponse);
                    return;
                }
            });

            httpResponse.forward(contentType, httpResponse.readFileToByte(httpRequest.getURI()));
            return;

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
