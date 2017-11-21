package model.pathhandler;

import model.HttpRequest;
import model.response.HttpResponse;

public interface PathController {
	public void handling(HttpRequest request, HttpResponse response);
}
