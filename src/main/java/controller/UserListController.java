package controller;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import request.GeneralHeaderValue;
import request.RequestHeader;
import util.HttpRequestUtils;
import util.SplitUtils;

public class UserListController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(UserListController.class);

	@Override
	public String run(RequestHeader requestHeader) {
		if (isLogin(requestHeader)) {
			return "dataValue: " + userList();
		}
		log.debug("로그인이 되어 있지 않습니다.");
		return "redirect: /user/login.html";
	}
	
	private boolean isLogin(RequestHeader request) {
		String cookiesValue = request.getRequestHeaderValues().getTextFindByField("Cookie");
		Map<String, String> cookies = HttpRequestUtils.parseCookies(cookiesValue);
		return Boolean.valueOf(SplitUtils.valueToStringOrEmpty(cookies, "logined"));
	}

	private String userList() {
		StringBuilder stringBuilder = new StringBuilder();
		ArrayList<User> users = new ArrayList<User>();
		users.addAll(DataBase.findAll());

		stringBuilder.append("<!doctype html>");
		stringBuilder.append("<html>");
		stringBuilder.append("<body>");
		for (User user : users) {
			stringBuilder.append(user.toString() + "<br>");
		}
		stringBuilder.append("</body>");
		stringBuilder.append("</html>");
		return stringBuilder.toString();
	}

	@Override
	public GeneralHeaderValue getResponseHeaderValue() {
		return null;
	}
}
