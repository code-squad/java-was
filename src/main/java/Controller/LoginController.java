package Controller;

import java.io.IOException;
import java.util.Map;

import db.DataBase;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class LoginController extends AbstractController {

	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		if (loginCheck(httpRequest.getParam())) {
			httpResponse.sendRedirect("/index.html", true);
		}
		httpResponse.sendRedirect("/user/login_failed.html", false);
	}

	boolean loginCheck(Map<String, String> param) {
		return DataBase.findUserById(param.get("userId")).getPassword().equals(param.get("password"));
	}
}
