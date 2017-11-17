package db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
    
    public static byte[] addUserList(byte[] body) {
		StringBuilder sb = new StringBuilder();
		sb.append(new String(body));
		int offset = sb.indexOf("user-list");
		List<User> users = new ArrayList<>(DataBase.findAll());
		for (User user : users) {
			sb.insert(offset + 14, "<tr>\r\n<th>#</th> <th>" + user.getUserId() + "</th> <th>" + user.getName() + "</th> <th>" + user.getEmail() + "</th><th></th>\r\n</tr>");
		}
    	return sb.toString().getBytes();
    }
}
