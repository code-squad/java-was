package webserver;

import db.DataBase;

import java.util.Map;

public class LoginController extends AbstractController {

    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        String requestBody = request.getRequestBody();
        Map<String, String> p = request.getRequestParameter(requestBody);
        String userId = p.get("userId");
        response.responseUserSignIn(userId);
        return;
    }

//    public void responseUserSignIn(String userId) {
//        DataBase db = new DataBase();
//        if(db.findUserById(userId) != null){// 로그인 성공시
//            responseWithCookie(true, "/index.html");
//            return;
//        }
//        responseWithCookie(false, "/user/login_failed.html");
//        return;
//    }
}
