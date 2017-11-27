package http.api.readbodycontroller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.User;

public class UserListBodyController implements ReadBodyController {

	@Override
	public byte[] readBody(String path) throws IOException {
		byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
		StringBuilder sb = new StringBuilder();
		sb.append(new String(body));
		int offset = sb.indexOf("user-list");
		List<User> users = new ArrayList<>(DataBase.findAll());
		for (User user : users) {
			sb.insert(offset + 14, 
					"<tr>\r\n<th>#</th> <th>" + user.getUserId() 
					+ "</th> <th>" + user.getName()
					+ "</th> <th>" + user.getEmail() 
					+ "</th><th></th>\r\n</tr>");
		}
		return sb.toString().getBytes();
	}

}
