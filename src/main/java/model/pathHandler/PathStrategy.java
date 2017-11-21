package model.pathHandler;

import model.HttpRequest;
import model.response.HttpResponse;

public interface PathStrategy {
	public void handling(HttpRequest request, HttpResponse response);
}
