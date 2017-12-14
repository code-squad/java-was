package controller;

import java.util.Map;

import model.User;

public class UserController {
	public User saveUser(Map<String, String> inputValue) {
		User user = new User(inputValue.get("userId"), inputValue.get("password"), inputValue.get("name"),
				inputValue.get("email"));
		return user;
	}
}