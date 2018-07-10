package webserver;

@RequestMapping("/")
public class HomeController implements Controller {
    private static final HomeController CONTROLLER = new HomeController();

    private HomeController() {
    }

    public static HomeController of() {
        return CONTROLLER;
    }
}
