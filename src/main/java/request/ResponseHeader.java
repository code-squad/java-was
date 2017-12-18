package request;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.SplitUtils;
import webserver.PathFileReader;

public class ResponseHeader {
	private static final Logger log = LoggerFactory.getLogger(ResponseHeader.class);
	private String statusCode;
	private byte[] body;
	private String responseUrl;

	public ResponseHeader(String responseValue, PathFileReader pathFileReader) throws IOException {
		statusCode = setStatusCode(responseValue);
		responseUrl = setResponseUrl(responseValue);
		body = pathFileReader.getReadAllBytes(responseUrl);
	}

	public void response(DataOutputStream dos) {
		responseHeader(dos, body.length);
		responseBody(dos, body);
	}

	private String setStatusCode(String responseValue) {
		if (responseValue.startsWith("redirect: ")) {
			return "302 Found";
		}
		return "200 OK";
	}

	private String setResponseUrl(String responseValue) {
		if ("302 Found".equals(statusCode)) {
			String url = SplitUtils.getSplitedValue(responseValue, ": ", 1);
			log.debug("redirect : {}", url);
			return url;
		}
		return responseValue;
	}

	private void responseHeader(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			log.debug("HTTP/1.1 " + statusCode);
			log.debug("Content-Type: text/html;charset=utf-8");
			log.debug("Content-Length: " + lengthOfBodyContent);

			dos.writeBytes("HTTP/1.1 " + statusCode + " \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			if ("302 Found".equals(statusCode)) {
				log.debug("Location: " + responseUrl);
				dos.writeBytes("Location: " + responseUrl);
			}
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}