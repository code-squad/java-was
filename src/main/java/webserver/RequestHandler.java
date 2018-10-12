package webserver;

import controller.*;
import domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    private Map<String, Controller> controllers = new HashMap<>();

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        initControllers();

    }

    private void initControllers() {
        controllers.put("/user/create", new CreateUserController());
        controllers.put("/user/login", new LoginController());
        controllers.put("/user/list", new ListUserController());
        controllers.put("/user/form.html", new ResourceController());
        controllers.put("/user/login.html", new ResourceController());
        controllers.put("/user/login_failed.html", new ResourceController());
        controllers.put("/css/styles.css", new ResourceController());
        controllers.put("/css/bootstrap.min.css", new ResourceController());
        controllers.put("/index.html", new ResourceController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            HttpResponse response = new HttpResponse(out);
            controllers.get(request.getPath()).service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
