package Controller;

import java.io.IOException;

import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListUserController extends AbstractController {

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		String logined = httpRequest.getLogined();

		if (logined.contains("logined=true;")) {
			httpResponse.forward("loginList");
		}
		if (logined.contains("logined=false;")) {
			httpResponse.sendRedirect("/user/login.html", false);
		}
	}
}
