package Controller;

import java.io.IOException;

import model.HttpRequest;
import model.HttpResponse;

public interface Controller {
	void service(HttpRequest request, HttpResponse response) throws IOException;
}