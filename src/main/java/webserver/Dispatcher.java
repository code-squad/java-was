package webserver;

import controller.*;
import webserver.http.request.HttpRequest;
import webserver.http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Dispatcher {
    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);

    private static MainController mainController = new MainController();
    private static CreateUserController createUserController = new CreateUserController();
    private static StyleSheetController styleSheetController = new StyleSheetController();
    private static LoginController loginController = new LoginController();

    public static void dispatch(HttpRequest request, HttpResponse response) throws IOException {
        if (request.getPath().startsWith("/user")) {
            logger.info("UserController");
            goUserController(request, response);
        }

        if (request.getPath().equals("/") || request.getPath().equals("/index.html")) {
            logger.info("MainController");
            goMainController(request, response);
        }

        if (request.getPath().startsWith("/css")) {
            logger.info("StyleSheetController");
            goStyleSheetController(request, response);
        }
    }

    private static void goUserController(HttpRequest request, HttpResponse response) throws IOException {
        String path = request.getPath();

        if (path.endsWith("/form.html"))
            createUserController.service(request, response);

        if (path.endsWith("/create"))
            createUserController.service(request, response);

        if (path.endsWith("/login.html"))
            loginController.service(request, response);

        if (path.endsWith("/login"))
            loginController.service(request, response);

        if (path.endsWith("/login_failed.html"))
            loginController.service(request, response);

        if (path.endsWith("/list"))
            UserController.list(request, response);
    }

    private static void goMainController(HttpRequest request, HttpResponse response) throws IOException {
        mainController.service(request, response);
    }

    private static void goStyleSheetController(HttpRequest request, HttpResponse response) throws IOException {
        styleSheetController.service(request, response);
    }
}
