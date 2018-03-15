package Controller;

import java.io.IOException;

import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller {
	@Override
	public void service(HttpRequest httpRequest, HttpResponse httpResponse) {
		String method = httpRequest.getMethod();

		try {
			if (HttpMethod.GET.whichMethod(method)) {
				doGet(httpRequest, httpResponse);
			}

			if (HttpMethod.POST.whichMethod(method)) {
				doPost(httpRequest, httpResponse);
			}
		} catch (Exception e) {
			//에러 페이지를 띄워 줘야 한다.
			e.printStackTrace();
		}
	}

	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
	}

	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
	}
}
