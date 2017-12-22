package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.RequestHeader;
import util.IOUtils;

public class RequestHeaderHandler {
	private static final Logger log = LoggerFactory.getLogger(RequestHeaderHandler.class);	
	private RequestHeader request;
	
	public RequestHeaderHandler(InputStream in) throws IOException {
		BufferedReader bufferedReader = convertToBufferedReader(in);
		String line = bufferedReader.readLine();
		log.debug("Request Line: {}", line);
		request = new RequestHeader(line);
		while(!"".equals(line) && line != null) {
			line = bufferedReader.readLine();
			request.addLine(line);
			log.debug("Header: {}", line);
		}
		log.debug("Path: {}", request.getPathValue());
		int contentLength = request.getContentLength();
		if(contentLength > 0) {
			request.setRequestBody(String.valueOf(IOUtils.readData(bufferedReader, contentLength)));
			log.debug("Body: {}", request.getRequestBody());
		}
	}
	
	public RequestHeader getRequestHeader() {
		return request;
	}
	
	private BufferedReader convertToBufferedReader(InputStream inputStream) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	}
}