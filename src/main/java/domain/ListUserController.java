package domain;

import service.UserService;

public class ListUserController extends AbstractController {
    @Override
    public void service(HttpRequest request, HttpResponse response) {
        doGet(request, response);
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if (!isLogined(request)) {
            response.sendRedirect(Url.LOGIN_FAIL);
            return;
        }
        UserService userService = new UserService();
        byte[] body = userService.list().getBytes();
        response.response200Header(body.length).responseBody(body);
    }

    private boolean isLogined(HttpRequest request) {
        return request.matchCookieValue("logined", "true");
    }
}
