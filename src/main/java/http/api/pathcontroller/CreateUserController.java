package http.api.pathcontroller;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

public class CreateUserController implements PathController {
	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		DataBase.addUser(
				new User(
						request.getParameter("userId"), 
						request.getParameter("password"),
						request.getParameter("name"), 
						request.getParameter("email")));
		response.sendRedirect("/index.html");
	}
}
