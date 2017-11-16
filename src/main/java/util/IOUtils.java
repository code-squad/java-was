package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import db.DataBase;
import model.User;

public class IOUtils {
    /**
     * @param BufferedReader는
     *            Request Body를 시작하는 시점이어야
     * @param contentLength는
     *            Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
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
