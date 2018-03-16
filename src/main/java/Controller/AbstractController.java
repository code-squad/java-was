package Controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.HttpMethod;
import webserver.HttpRequest;
import webserver.HttpResponse;

public abstract class AbstractController implements Controller {
	private static final Logger log = LoggerFactory.getLogger(AbstractController.class);
	
	@Override
	public void service(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
		String method = httpRequest.getMethod();
			try {
				if (HttpMethod.GET.whichMethod(method)) {
					doGet(httpRequest, httpResponse);
				}
				
				if (HttpMethod.POST.whichMethod(method)) {
					doPost(httpRequest, httpResponse);
				}
			} catch (Exception e) {
				log.debug(e.getMessage());
				httpResponse.forward("/error");
			}
	}

	public void doGet(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
	}

	public void doPost(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
	}
}