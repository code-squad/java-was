package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.GeneralHeaderValue;
import util.SplitUtils;

public class ResponseHeaderHandler {
	private static final Logger log = LoggerFactory.getLogger(ResponseHeaderHandler.class);
	private StatusCode statusCode;
	private GeneralHeaderValue responseHeaders = new GeneralHeaderValue();
	private String responseUrl;
	private byte[] body;

	public ResponseHeaderHandler(String responseValue, PathFileReader pathFileReader,
			GeneralHeaderValue responseHeaderValues) throws IOException {
		statusCode = setStatusCode(responseValue);
		if (responseValue.startsWith("dataValue: ")) {
			body = SplitUtils.getSplitedValue(responseValue, ": ", 1).getBytes();
		} else if (responseValue.startsWith("redirect: ")) {
			body = "".getBytes();
		} else {
			body = pathFileReader.getReadAllBytes(responseUrl);
		}
		setAccept(responseValue);
		setDefaultHeader();
		if (responseHeaderValues != null) {
			responseHeaders.addGeneralHeaderValue(responseHeaderValues);
		}
	}

	// 실행하는 핵심 메소드
	public void response(DataOutputStream dos) {
		responseHeader(dos);
		responseBody(dos, body);
	}

	private void responseHeader(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 " + statusCode.getStatusCodeValue() + " \r\n");
			writeBytesResponseHeader(dos);
			log.debug("HTTP/1.1 " + statusCode.getStatusCodeValue()); // statusLine
			debugResponseHeaders();
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

	private void debugResponseHeaders() {
		for (Map.Entry<String, String> responseHeader : responseHeaders.getGeneralHeaderValue().entrySet()) {
			log.debug(responseHeader.toString());
		}
	}

	private StatusCode setStatusCode(String responseValue) {
		if (responseValue.startsWith("redirect: ")) {
			setRedirectUrl(responseValue);
			return StatusCode.FOUND;
		}
		responseUrl = responseValue;
		return StatusCode.OK;
	}

	private void setRedirectUrl(String responseValue) {
		String url = SplitUtils.getSplitedValue(responseValue, ": ", 1);
		setHeader("Location", url);
		log.debug("redirect : {}", url);
		responseUrl = url;
	}

	public void setHeader(String field, String text) {
		responseHeaders.addGeneralHeaderValue(field, text);
	}

	public void setDefaultHeader() {
		setHeader("Content-Length", Integer.toString(body.length));
	}

	public void setAccept(String responseValue) {
		setHeader("Accept", getAcceptContentType(responseValue));
	}

	private String getAcceptContentType(String responseValue) {
		String extension = SplitUtils.getSplitedExtension(responseValue).toUpperCase();
		if ("HTML".equals(extension))
			return "text/html";
		if ("CSS".equals(extension))
			return "text/css";
		if ("JS".equals(extension))
			return "text/javascript";
		return "*/*";
	}

	private void writeBytesResponseHeader(DataOutputStream dos) {
		try {
			for (Map.Entry<String, String> responseHeader : responseHeaders.getGeneralHeaderValue().entrySet()) {
				dos.writeBytes(responseHeader.getKey() + ": " + responseHeader.getValue() + "\r\n");
				log.debug("실제 입력되는 값: " + responseHeader.getKey() + ": " + responseHeader.getValue());
			}
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void loginResponse() {
		setHeader("Cookie", "logined=true; Path=/");
	}

	public enum StatusCode {
		FOUND("302", "FOUND"), OK("200", "OK");
		private String statusCode;
		private String statusText;

		StatusCode(String statusCode, String statusText) {
			this.statusCode = statusCode;
			this.statusText = statusText;
		}

		public static StatusCode getStatusCodeByCode(String codeNumber) {
			return StatusCode.valueOf(codeNumber);
		}

		public static StatusCode getStatusCodeByText(String codeText) {
			StatusCode[] statusCodes = StatusCode.values();
			String upperText = codeText.toUpperCase();
			for (StatusCode statusCode : statusCodes) {
				if (upperText.equals(statusCode.getStatusText()))
					return statusCode;
			}
			return null;
		}

		public String getStatusCodeValue() {
			return statusCode + " " + statusText;
		}

		private String getStatusText() {
			return statusText;
		}
	}
}