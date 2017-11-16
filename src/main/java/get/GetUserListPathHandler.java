package get;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import request.PathHandler;
import util.HttpRequestParser;
import webserver.LoginCheckModule;
import webserver.RequestHandler;
import webserver.ResponseHandler;

public class GetUserListPathHandler implements PathHandler {
	private static final Logger log = LoggerFactory.getLogger(GetUserListPathHandler.class);
	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		log.debug("User List를 탐색합니다. 로그인 체크");
		LoginCheckModule.checkLogin(dos, parser.getLogin());
		log.debug("User List를 탐색합니다. 로그인 체크 성공");
		String url = parser.getUrl();
		// TODO Auto-generated method stub
		byte[] body = DataBase.findAll().toString().getBytes();
		ResponseHandler.response200Header(dos, body.length, url);
		ResponseHandler.responseBody(dos, body);
	}
}
