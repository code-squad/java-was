package htpp.api.pathcontroller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

public class LoginController extends PostController {

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		User user = DataBase.findUserById(request.getParameter("userId"));
		if (user != null && user.matchPassword(request.getParameter("password"))) {
			response.setCookieLogined(true);
			response.setUrl("/index.html");
		} else {
			response.setCookieLogined(false);
			response.setUrl("/user/login_failed.html");
		}
	}
}
