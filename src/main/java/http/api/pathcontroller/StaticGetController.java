package http.api.pathcontroller;

import http.request.HttpRequest;
import http.response.HttpResponse;

public class StaticGetController extends GetController {

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		String url = request.getPath();
		response.setUrl(url);
		response.setContentType(request.getHeader("Accept"));
	}
}
