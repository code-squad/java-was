package http.api.pathcontroller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public interface PathController {
	public void handling(HttpRequest request, HttpResponse response);
	public HttpResponse getResponse();
}
