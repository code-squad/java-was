package Controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
	void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
