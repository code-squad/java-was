package get;

import java.io.DataOutputStream;
import java.io.IOException;

import db.DataBase;
import request.PathHandler;
import util.HttpRequestParser;
import webserver.LoginCheckModule;
import webserver.ResponseHandler;

public class GetUserListPathHandler implements PathHandler {
	
	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		LoginCheckModule.checkLogin(dos, parser.getLogin());
		String url = parser.getUrl();
		// TODO Auto-generated method stub
		byte[] body = DataBase.findAll().toString().getBytes();
		ResponseHandler.response200Header(dos, body.length, url);
		ResponseHandler.responseBody(dos, body);
	}
}
