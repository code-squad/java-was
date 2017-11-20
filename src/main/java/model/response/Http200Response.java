package model.response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Http200Response extends HttpResponse {
	
	@Override
	public String createHeader() {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 200 OK \r\n");
		sb.append("Content-Type: ").append(getHeader("Content-Type")).append("\r\n");
		sb.append("Content-Length: ").append(getHeader("Content-Length")).append("\r\n");
		sb.append("\r\n");
		return sb.toString();
	}

}
