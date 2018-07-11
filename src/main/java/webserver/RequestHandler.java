package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            HttpRequest request = new HttpRequest(in);
            HttpResponse response;
            if (request.getPath().startsWith("/user/create")) {
                Map<String, String> userParams = request.getQueryParameters();
                User user = new User(userParams.get("userId"), userParams.get("password"), userParams.get("name"), userParams.get("email"));
                log.debug("Created User: {}", user);
                response = new HttpResponse(new Resource("/index.html"));
            } else {
                response = new HttpResponse(new Resource(request.getPath()));
            }

            response.writeResponse(out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


}
