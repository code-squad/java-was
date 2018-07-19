package controller;

import java.io.IOException;

import model.HttpRequest;
import model.HttpResponse;

public interface FrontController {
	void service(HttpRequest request, HttpResponse response) throws IOException;
}