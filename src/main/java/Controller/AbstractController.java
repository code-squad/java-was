package Controller;

import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller {

	@Override
	public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
		
	}
	
	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) {
		
	}
	
	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) {
		
	}
}
