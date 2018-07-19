package Controller;

import java.io.IOException;

import model.HttpRequest;
import model.HttpResponse;

abstract class AbstractController implements Controller{

	@Override
	public void service(HttpRequest request, HttpResponse response) throws IOException {
		String method = request.getMethod();
		if(method.equals("POST")) {
			doPost(request, response);
		}else {
			doGet(request, response);
		}
	}
	
	abstract  void doPost(HttpRequest request, HttpResponse response) throws IOException;
	abstract  void doGet(HttpRequest request, HttpResponse response) throws IOException;

}
