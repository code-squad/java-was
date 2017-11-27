package http.api.pathcontroller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public class StaticGetController implements PathController {

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		String url = request.getPath();
		response.setContentType(request.getHeader("Accept"));
		response.forward(url);
	}
}
