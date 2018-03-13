package webserver;

import db.DataBase;

import java.util.Map;

public class LoginController extends AbstractController {

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String requestBody = request.getRequestBody();
        Map<String, String> p = request.getRequestParameter(requestBody);
        String userId = p.get("userId");
        response.loginUser(userId);
        return;
    }
}
