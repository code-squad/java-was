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
                log.debug("Created User: {}", user);

                headers.addHeader(HttpHeader.CONTENT_TYPE, "text/html;charset=utf-8");
                headers.addHeader(HttpHeader.LOCATION, "/index.html");
                response = new HttpResponse(HttpStatus.FOUND, headers , Resource.ofEmpty());
            } else {
                response = new HttpResponse(HttpStatus.OK, headers, Resource.of(request.getPath()));
            }


            response.writeResponse(out);

        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
