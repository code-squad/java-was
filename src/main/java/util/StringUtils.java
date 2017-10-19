package util;

import java.io.BufferedReader;

import db.DataBase;
import model.User;

public class StringUtils {
	public String generatePath(String[] tokens) {
		return tokens[1];
	}

	public boolean checkCreateUser(String url) {
		if (url.equals("/user/create")) {
			return true;
		}
		return false;
	}
	
	public boolean checkLogin(String url) {
		if (!url.equals("/user/login")) {
			return false;
		}
		return true;
	}
	public boolean confirmUser(User user,DataBase db) {
		
		if (db.findUserById(user.getUserId()) == null) {
			return false;
		}
		if (!db.findUserById(user.getUserId()).getPassword().equals(user.getPassword())) {
			return false;
		}
		return true;
	}
	public boolean checkPost(String url) {
		String[] parseUrl = url.split(" ");
		if (parseUrl[0].equals("POST")) {
			return true;
		}
		return false;
	}
	
	public String generatePostUrl(String url) {
		String[] parseUrl = url.split("\\?");
		return parseUrl[1];
	}
	public User generateUser(String url) {
		HttpRequestUtils hru = new HttpRequestUtils();
		
		String userId = hru.parseQueryString(url).get("userId");
		String password =hru.parseQueryString(url).get("password");
		String name = hru.parseQueryString(url).get("name");
		String email = hru.parseQueryString(url).get("email");
		return new User(userId,password,name,email);
	}
	public User loginUser(String url) {
		HttpRequestUtils hru = new HttpRequestUtils();
		
		String userId = hru.parseQueryString(url).get("userId");
		String password =hru.parseQueryString(url).get("password");
		return new User(userId,password);
	}



}
