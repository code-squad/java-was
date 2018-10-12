package controller;

import domain.HttpRequest;
import domain.HttpResponse;
import domain.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

public class CreateUserController extends AbstractController {
    private static final Logger log =  LoggerFactory.getLogger(CreateUserController.class);

    @Override
    public void service(HttpRequest request, HttpResponse response) {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        UserService userService = new UserService();
        userService.save(userService.create(String.valueOf(request.getBody())));
        log.debug("doPost url : {}", Url.HOME);
        response.sendRedirect(Url.HOME);
    }
}
