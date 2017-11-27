package http.api.pathcontroller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public class UserListController implements PathController {
	
	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		if (request.isLogined()) {
			response.forward("/user/list.html");
		} else {
			response.forward("/user/login.html");
		}
	}

}
