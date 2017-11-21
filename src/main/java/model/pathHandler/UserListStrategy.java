package model.pathHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.response.HttpResponse;

public class UserListStrategy implements PathStrategy {
	private static final Logger log = LoggerFactory.getLogger(UserListStrategy.class);

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		if (request.isLogined()) {
			String url = "/user/list.html";
			response.setUrl(url);
			try {
				byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
				response.putBody(DataBase.addUserList(body));
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		} else {
			response.setUrl("/user/login.html");
		}
	}

}
