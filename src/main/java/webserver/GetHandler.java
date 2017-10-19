package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import db.DataBase;
import util.HttpRequestParser;

public class GetHandler {
	public void getHandling(HttpRequestParser parser, DataOutputStream dos) {
		try {
			urlHandler(parser, dos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void normalPageMapping(String url, DataOutputStream dos) throws IOException {
		byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
		ResponseHandler.response200Header(dos, body.length, url);
		ResponseHandler.responseBody(dos, body);
	}
	
	public void listPageMapping(String url, DataOutputStream dos) {
		byte[] body = DataBase.findAll().toString().getBytes();
		ResponseHandler.response200Header(dos, body.length, url);
		ResponseHandler.responseBody(dos, body);
	}
	
	public void urlHandler(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		String url = parser.getUrl();
		Boolean isLogin = parser.getLogin();
		if(url.equals("/user/list")) {
			LoginCheckModule.isLogin(dos, isLogin);
			listPageMapping(url, dos);
		}else {
			normalPageMapping(url, dos);
		}
	}
}
