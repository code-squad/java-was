package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.HttpRequest;
import model.HttpResponse;
import model.User;

public class ListUserController extends AbstractController {
	private static final Logger log = LoggerFactory.getLogger(ListUserController.class);
	private static final int TEN = 10;

	@Override
	void doPost(HttpRequest request, HttpResponse response) {
	}

	@Override
	void doGet(HttpRequest request, HttpResponse response) throws IOException {

		if (!request.getHeader("Cookie").contains("true")) {
			response.sendRedirect("/user/login.html");
			return;
		}
		log.debug("headers: Set-Cookie : {}", request.getHeader("Cookie"));
		log.debug("login ok {}", request.getUrl());

		byte[] body = Files.readAllBytes(new File("./webapp" + "/user/list.html").toPath());
		String userList = new String(body);
		response.forwardBody(userList.substring(0, userList.indexOf("{{#users}}"))+makeUserList()+userList.substring(userList.indexOf("{{/users}}") + TEN));
	}
	
	public String makeUserList() {
		Collection<User> users = DataBase.findAll();

		StringBuilder sb = new StringBuilder();
		sb.append("<table border='1' class='table table-hover\'>");
		for (User user : users) {
			sb.append("<tr>");
			sb.append("<td>" + user.getUserId() + "</td>");
			sb.append("<td>" + user.getName() + "</td>");
			sb.append("<td>" + user.getEmail() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
	

}
