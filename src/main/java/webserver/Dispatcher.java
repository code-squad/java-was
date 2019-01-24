package webserver;

import controller.MainController;
import controller.UserController;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    public static void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        if (request.getUri().startsWith("/user")) {
            logger.info("UserController");
            goUserController(request, response);
        }

        if (request.getUri().equals("/") || request.getUri().equals("/index.html")) {
            logger.info("MainController");
            goMainController(request, response);
        }
    }

    private static void goUserController(HttpRequest request, HttpResponse response) throws IOException {
        if (request.getUri().endsWith("/form"))
            UserController.createForm(request, response);

        if (request.getUri().endsWith("/create"))
            UserController.create(request, response);

        if (request.getUri().endsWith("/login.html"))
            UserController.loginForm(request, response);

        if (request.getUri().endsWith("/login"))
            UserController.login(request, response);

    }

    private static void goMainController(HttpRequest request, HttpResponse response) throws IOException {
        MainController.index(request, response);
    }
}
