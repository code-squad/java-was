package Controller;

import java.io.IOException;

import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
	void service(HttpRequest httpRequest, HttpResponse httpResponse);
}
