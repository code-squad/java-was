package htpp.api.pathcontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;

public class UserListController extends GetController {
	private static final Logger log = LoggerFactory.getLogger(UserListController.class);

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		if (request.isLogined()) {
			String url = "/user/list.html";
			response.setUrl(url);
			try {
				byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
				response.putBody(addUserList(body));
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		} else {
			response.setUrl("/user/login.html");
		}
	}

	private byte[] addUserList(byte[] body) {
		StringBuilder sb = new StringBuilder();
		sb.append(new String(body));
		int offset = sb.indexOf("user-list");
		List<User> users = new ArrayList<>(DataBase.findAll());
		for (User user : users) {
			sb.insert(offset + 14, "<tr>\r\n<th>#</th> <th>" + user.getUserId() + "</th> <th>" + user.getName()
					+ "</th> <th>" + user.getEmail() + "</th><th></th>\r\n</tr>");
		}
		return sb.toString().getBytes();
	}

}
