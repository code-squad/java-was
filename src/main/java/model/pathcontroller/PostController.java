package model.pathcontroller;

import model.response.Http302Response;
import model.response.HttpResponse;

public abstract class PostController implements PathController{
	public HttpResponse getResponse() {
		return Http302Response.create();
	}
}
