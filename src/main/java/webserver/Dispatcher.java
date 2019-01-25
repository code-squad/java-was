package webserver;

import controller.MainController;
import controller.StyleSheetController;
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

        if (request.getUri().startsWith("/css")) {
            logger.info("StyleSheetController");
            goStyleSheetController(request, response);
        }
    }

    private static void goUserController(HttpRequest request, HttpResponse response) throws IOException {
        if (request.getUri().endsWith("/form.html"))
            UserController.createForm(request, response);

        if (request.getUri().endsWith("/create"))
            UserController.create(request, response);

        if (request.getUri().endsWith("/login.html"))
            UserController.loginForm(request, response);

        if (request.getUri().endsWith("/login"))
            UserController.login(request, response);

        if (request.getUri().endsWith("/login_failed.html"))
            UserController.loginForm_failed(request, response);

        if (request.getUri().endsWith("/list"))
            UserController.list(request, response);
    }

    private static void goMainController(HttpRequest request, HttpResponse response) throws IOException {
        MainController.index(request, response);
    }

    private static void goStyleSheetController(HttpRequest request, HttpResponse response) throws IOException {
        StyleSheetController.styleSheet(request, response);
    }
}
