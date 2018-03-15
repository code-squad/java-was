package webserver;

import db.DataBase;
import model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserListController extends AbstractController {

    @Override
    public void doGet(HttpRequest request, HttpResponse response) {
        if(request.getCookieValue()){// in logined status
            // 사용자 목록 출력
            // Collection to List
            List<User> users = DataBase.findAll().stream().collect(Collectors.toList());
            byte[] body = response.createDynamicHTML("./webapp/user/list_static.html", users);
            response.forward("text/html", body);
            return;
        }
        // move to login page
        response.responseWithCookie(false, "/user/login.html");
        return;
    }
}
