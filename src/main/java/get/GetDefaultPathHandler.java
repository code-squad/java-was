package get;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import request.PathHandler;
import util.HttpRequestParser;
import webserver.ResponseHandler;

public class GetDefaultPathHandler implements PathHandler {

	@Override
	public void run(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		String url = parser.getUrl();
		byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
		ResponseHandler.response200Header(dos, body.length, url);
		ResponseHandler.responseBody(dos, body);
	}
	
}
