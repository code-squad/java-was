package controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import request.GeneralHeaderValue;
import request.RequestHeader;
import util.HttpRequestUtils;

public class UserLoginController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);
	private GeneralHeaderValue responseHeaderValues = new GeneralHeaderValue();
	
	@Override
	public String run(RequestHeader requestHeader) {
		return userLogin(requestHeader.getRequestBody());
	}
	
	private String userLogin(String requestBody) {
		Map<String, String> loginValue = HttpRequestUtils.parseQueryString(requestBody);
		// userId password
		String userId = loginValue.get("userId");
		String password = loginValue.get("password");
		User user = DataBase.findUserById(userId);
		// 로그인 성공
		if (password.equals(user.getPassword())) {
			log.debug(user.getName() + " 로그인 성공");
			responseHeaderValues.addGeneralHeaderValue("Set-Cookie", "logined=true; Path=/");
			return "redirect: " + HOMEPATH;
		}
		log.debug("로그인 실패");
		return "redirect: /user/login_failed.html";
	}

	@Override
	public GeneralHeaderValue getResponseHeaderValue() {
		return responseHeaderValues;
	}
}
