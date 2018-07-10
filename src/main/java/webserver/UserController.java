package webserver;

@RequestMapping("/user")
public class UserController implements Controller {
    private static final UserController CONTROLLER = new UserController();

    private UserController() {
    }

    public static UserController of() {
        return CONTROLLER;
    }
}
