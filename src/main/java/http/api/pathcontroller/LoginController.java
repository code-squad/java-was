package http.api.pathcontroller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

public class LoginController implements PathController {

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		User user = DataBase.findUserById(request.getParameter("userId"));
		if (user != null && user.matchPassword(request.getParameter("password"))) {
			response.setCookieLogined(true);
			response.sendRedirect("/index.html");
		} else {
			response.setCookieLogined(false);
			response.sendRedirect("/user/login_failed.html");
		}
	}
}
