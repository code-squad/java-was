package post;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import request.PathHandler;
import util.HttpRequestParser;
import util.HttpRequestUtils;
import webserver.ResponseHandler;

public class PostLoginPathHandler implements PathHandler {
	
	private static final Logger log = LoggerFactory.getLogger(PostLoginPathHandler.class);
	
	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		// TODO Auto-generated method stub
		Map<String, String> bodyMap = HttpRequestUtils.parseQueryString(parser.getBody());
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
