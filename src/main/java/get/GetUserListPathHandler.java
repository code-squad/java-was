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
		log.debug("1");
		// TODO Auto-generated method stub
		byte[] body = DataBase.findAll().toString().getBytes();
		log.debug("2");
		ResponseHandler.response200Header(dos, body.length, url);
		log.debug("3");
		ResponseHandler.responseBody(dos, body);
		log.debug("4");
	}
}
