package webserver;

import java.util.Map;

public class CreateUserController extends AbstractController {

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String requestBody = request.getRequestBody();
        Map<String, String> p = request.getRequestParameter(requestBody);
        response.createUser(p);
        response.sendRedirect("/index.html");
    }
}
