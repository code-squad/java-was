package Controller;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class ListUserController extends AbstractController {

	@Override
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		String logined = httpRequest.getLogined();

		if (isLogin(httpRequest.getCookie())) {
			Collection<User> users = DataBase.findAll();
			StringBuilder sb = new StringBuilder();
			sb.append("<table>");
			for (User user : users) {
				sb.append("<tr>");
				sb.append("<td>" + user.getUserId() + "</td>");
				sb.append("<td>" + user.getName() + "</td>");
				sb.append("<td>" + user.getEmail() + "</td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			httpResponse.forwardDynamic(sb.toString().getBytes());
		}
		if (!isLogin(httpRequest.getCookie())) {
			httpResponse.sendRedirect("/user/login.html");
		}
	}
	
    private boolean isLogin(String cookie) {
    	if (cookie.contains("true")) {
    		return true;
    	}
    	return false;
    }
}
