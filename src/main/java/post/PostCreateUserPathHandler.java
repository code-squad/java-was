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

public class PostCreateUserPathHandler implements PathHandler {
	private static final Logger log = LoggerFactory.getLogger(PostCreateUserPathHandler.class);
	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		Map<String, String> bodyMap = HttpRequestUtils.parseQueryString(parser.getBody());
		// TODO Auto-generated method stub
		User user = new User(bodyMap.get("userId"), bodyMap.get("password"), bodyMap.get("name"), bodyMap.get("email"));
		DataBase.addUser(user);
		log.debug("회원가입이 성공했습니다 : " + user.toString());
		ResponseHandler.response302Header(dos, "index.html");
	}

}
