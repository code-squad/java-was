package http.api.pathcontroller;

import http.response.Http302Response;
import http.response.HttpResponse;

public abstract class PostController implements PathController{
	public HttpResponse getResponse() {
		return Http302Response.create();
	}
}
