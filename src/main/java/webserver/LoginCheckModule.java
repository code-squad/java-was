package webserver;

import java.io.DataOutputStream;

public class LoginCheckModule {
	public static void checkLogin(DataOutputStream dos, Boolean isLogin) {
		if(isLogin) {
			return;
		}
		ResponseHandler.response302Header(dos, "user/login.html");
	}
}
