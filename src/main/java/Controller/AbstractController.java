package Controller;

import java.io.IOException;

import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller {
	@Override
	public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
		String method = httpRequest.getMethod();

		try {
			if (method.equals("GET")) {
				doGet(httpRequest, httpResponse);
			}

			if (method.equals("POST")) {
				doPost(httpRequest, httpResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		httpResponse.forward(httpRequest.getURI());
	}

	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
	}
}
