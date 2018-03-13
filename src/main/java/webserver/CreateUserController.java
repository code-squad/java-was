package webserver;

import java.util.Map;

public class CreateUserController extends AbstractController {

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String requestBody = request.getRequestBody();
        Map<String, String> p = request.getRequestParameter(requestBody);
        response.createUser(p);
        response.sendRedirect("/index.html");
        return;
    }

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        String queryString = request.getQueryString(request.getPath());
        Map<String, String> p = request.getRequestParameter(queryString);
        response.createUser(p);
        response.sendRedirect("/index.html");
        return;
    }
}
