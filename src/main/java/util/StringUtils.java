package util;

import java.io.BufferedReader;

import db.DataBase;
import model.User;

public class StringUtils {


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


	public boolean checkCookieLogin(String cookieValue) {
		if(!cookieValue.equals("logined=true")) {
			return false;
		}
		return true;
	}

}
