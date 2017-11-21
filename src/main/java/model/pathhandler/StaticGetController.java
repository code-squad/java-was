package model.pathhandler;

import model.HttpRequest;
import model.response.HttpResponse;

public class StaticGetController implements PathController {

	@Override
	public void handling(HttpRequest request, HttpResponse response) {
		String url = request.getHeader("url");
		response.setUrl(url);
		response.setContentType(request.getHeader("Accept"));
	}
}
