package webserver;

import domain.*;
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
        controllers.put("/user/form.html", new UserController());
        controllers.put("/user/login.html", new UserController());
        controllers.put("/user/login_failed.html", new UserController());
        controllers.put("/index.html", new UserController());
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest request = new HttpRequest(in);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse response = new HttpResponse(dos);
            controllers.get(request.getPath()).service(request, response);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
