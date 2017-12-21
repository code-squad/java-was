package controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import request.RequestHeader;
import request.ResponseHeader;
import request.ResponseHeaderValues;
import util.HttpRequestUtils;

public class UserLoginController implements Controller {
	
	private static final Logger log = LoggerFactory.getLogger(UserLoginController.class);

	
	@Override
	public String run(RequestHeader requestHeader, ResponseHeaderValues responseHeaderValues) {
		return userLogin(requestHeader.getRequestBody(), responseHeaderValues);
	}
	
	private String userLogin(String requestBody, ResponseHeaderValues responseHeaderValues) {
		Map<String, String> loginValue = HttpRequestUtils.parseQueryString(requestBody);
		// userId password
		String userId = loginValue.get("userId");
		String password = loginValue.get("password");
		User user = DataBase.findUserById(userId);
		// 로그인 성공
		if (password.equals(user.getPassword())) {
			log.debug(user.getName() + " 로그인 성공");
			responseHeaderValues.addResponseHeaderValues(new ResponseHeader("Set-Cookie", "logined=true; Path=/"));
			return "redirect: " + HOMEPATH;
		}
		log.debug("로그인 실패");
		return "redirect: /user/login_failed.html";
	}
}
