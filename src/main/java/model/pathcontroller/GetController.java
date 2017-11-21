package model.pathcontroller;

import model.response.Http200Response;
import model.response.HttpResponse;

public abstract class GetController implements PathController {
	public HttpResponse getResponse() {
		return Http200Response.create();
	}
}
