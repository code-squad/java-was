package model.pathcontroller;

import model.HttpRequest;
import model.response.HttpResponse;

public interface PathController {
	public void handling(HttpRequest request, HttpResponse response);
	public HttpResponse getResponse();
}
