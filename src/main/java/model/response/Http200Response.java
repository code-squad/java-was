package model.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Http200Response extends HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(Http200Response.class);
	private static final String status = "HTTP/1.1 200 OK \r\n";

	private Http200Response(String status) {
		super(status);
	}

	public static HttpResponse create() {
		return new Http200Response(status);
	}

	@Override
	public void setUrl(String url) {
		try {
			byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
			putBody(body);
		} catch (IOException e) {
			log.error("body를 읽어오면서 에러가 났다!!");
		}
	}
}
