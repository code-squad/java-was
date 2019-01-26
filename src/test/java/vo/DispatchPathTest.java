package vo;

import controller.Controller;
import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.*;

public class DispatchPathTest {
    private static final Logger logger = LoggerFactory.getLogger(DispatchPathTest.class);

    @Test
    public void findController() {
        String formPath = "/user/form";
        String createPath = "/user/create";
        String listPath = "/user/list";
        String loginFailedFormPath = "/user/login_failed.html";
        String loginFormPath = "/user/login.html";
        String loginPath = "/user/login";

        Controller form = DispatchPath.findController(formPath);
        logger.debug("formPath : {}", form);
        assertTrue(form instanceof CreateUserController);

        Controller create = DispatchPath.findController(createPath);
        logger.debug("createPath : {}", create);
        assertTrue(create instanceof CreateUserController);

        Controller list = DispatchPath.findController(listPath);
        logger.debug("listPath : {}", list);
        assertTrue(list instanceof ListUserController);

        Controller loginFailedForm = DispatchPath.findController(loginFailedFormPath);
        logger.debug("loginFailedFormPath : {}", loginFailedForm);
        assertTrue(loginFailedForm instanceof LoginController);

        Controller loginForm = DispatchPath.findController(loginFormPath);
        logger.debug("loginFormPath : {}", loginForm);
        assertTrue(loginForm instanceof LoginController);

        Controller login = DispatchPath.findController(loginPath);
        logger.debug("loginPath : {}", login);
        assertTrue(login instanceof LoginController);
    }


}