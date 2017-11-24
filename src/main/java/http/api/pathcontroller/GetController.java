package http.api.pathcontroller;

import http.response.Http200Response;
import http.response.HttpResponse;

public abstract class GetController implements PathController {
	public HttpResponse getResponse() {
		return Http200Response.create();
	}
}
