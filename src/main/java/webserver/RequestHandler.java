package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

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
            HttpRequest request = HttpFactory.init(in);
            HttpResponse response = new HttpResponse(out);
            if (request.getUrl().equals("/user/create")) {
                Controller.createUser(request, response);
                return;
            }
            if (request.getUrl().equals("/user/login")) {
                Controller.login(request, response);
                log.debug("Login finish");
                return;
            }

            if (request.getUrl().equals("/user/list")) {
                Controller.showUser(request, response);
                log.debug("User List");
                return;
            }
            Controller.forward(request, response);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
