package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestParser;
import util.HttpRequestUtils;

public class PostHandler {
	private static final Logger log = LoggerFactory.getLogger(PostHandler.class);
	public void postHandling(HttpRequestParser parser, DataOutputStream dos) {
		try {
			urlHandler(parser, dos);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void urlHandler(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		Map<String, String> bodyMap = HttpRequestUtils.parseQueryString(parser.getBody());
		String url = parser.getUrl();
		if(url.equals("/user/login")) {
			userLoginPostMapping(bodyMap, dos);
		}else if(url.equals("/user/create")) {
			userCreatePostMapping(bodyMap, dos);
		}
	}
	
	public void userCreatePostMapping(Map<String, String> bodyMap, DataOutputStream dos) throws IOException {
		User user = new User(bodyMap.get("userId"), bodyMap.get("password"), bodyMap.get("name"), bodyMap.get("email"));
		DataBase.addUser(user);
		log.debug("회원가입이 성공했습니다 : " + user.toString());
		ResponseHandler.response302Header(dos, "index.html");
	}
	
	public void userLoginPostMapping(Map<String, String> bodyMap, DataOutputStream dos) {
		try {
			User user = DataBase.findUserById(bodyMap.get("userId"));
			Boolean isLogin = user.isLogin(bodyMap.get("password"));
			if(isLogin) {
				ResponseHandler.responseLoginSucess302Header(dos, "index.html");
			}else {
				log.debug("login failed : 비밀번호가 틀렸습니다.");
				ResponseHandler.responseLoginFailed302Header(dos, "user/login_failed.html");
			}
			
		} catch(Exception e) {
			log.debug("login failed : 아이디가 틀렸습니다.");
			ResponseHandler.responseLoginFailed302Header(dos, "user/login_failed.html");
		}
	}
}
