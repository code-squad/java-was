package Controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.HttpRequest;
import webserver.HttpResponse;

public class CreateUserController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(CreateUserController.class);

	@Override
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		createUser(httpRequest.getParam());
		httpResponse.sendRedirect("/index.html", false);
	}

	void createUser(Map<String, String> param) throws UnsupportedEncodingException {
		DataBase.addUser(new User(param.get("userId"), param.get("password"), param.get("name"), param.get("email")));
		log.debug("[ createUser method ] SIZE : " + DataBase.findAll().size());
	}
}
