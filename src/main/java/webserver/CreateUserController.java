package webserver;

import model.User;

public class CreateUserController extends AbstractController {
    // controller 에서 db 관련 처리한다. (질문하기)
    @Override
    public void doPost(HttpRequest request, HttpResponse response) {
        super.doPost(request, response);
        User user = new User(request.getParameter("userId"),
                request.getParameter("password"),
                request.getParameter("name"),
                request.getParameter("email"));
        db.addUser(user);
        response.sendRedirect("/index.html");
    }

}
