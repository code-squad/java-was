package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.ResponseHeader;
import request.ResponseHeaderValues;
import util.SplitUtils;

public class ResponseHeaderHandler {
	private static final Logger log = LoggerFactory.getLogger(ResponseHeaderHandler.class);
	private StatusCode statusCode;
	private ResponseHeaderValues responseHeaders = new ResponseHeaderValues();
	private String responseUrl;
	private byte[] body;

	public ResponseHeaderHandler(String responseValue, PathFileReader pathFileReader) throws IOException {
		statusCode = setStatusCode(responseValue);
		if (responseValue.startsWith("dataValue: ")) {
			body = SplitUtils.getSplitedValue(responseValue, ": ", 1).getBytes();
		} else {
			body = pathFileReader.getReadAllBytes(responseUrl);
		}
		setAccept(responseValue);
		setDefaultHeader();
	}

	public void response(DataOutputStream dos) {
		responseHeader(dos);
		responseBody(dos, body);
	}

	public void setResponseHeaderList(ResponseHeaderValues responseHeaderValues) {
		responseHeaders.addResponseHeaderValues(responseHeaderValues);
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
		setHeader(new ResponseHeader("Location", url));
		log.debug("redirect : {}", url);
		responseUrl = url;
	}

	public void setHeader(ResponseHeader responseHeader) {
		responseHeaders.addResponseHeaderValues(responseHeader);
	}

	public void setDefaultHeader() {
		setHeader(new ResponseHeader("Content-Length", Integer.toString(body.length)));
	}

	public void setAccept(String responseValue) {
		setHeader(new ResponseHeader("Accept", getAcceptContentType(responseValue)));
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

	private void debugResponseHeaders() {
		for (ResponseHeader responseHeader : responseHeaders.getResponseHeaders()) {
			log.debug(responseHeader.toString());
		}
	}

	private void writeBytesResponseHeader(DataOutputStream dos) {
		try {
			for (ResponseHeader responseHeader : responseHeaders.getResponseHeaders()) {
				dos.writeBytes(responseHeader.toString() + "\r\n");
			}
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public void loginResponse() {
		setHeader(new ResponseHeader("Cookie", "logined=true; Path=/"));
	}

	private void responseHeader(DataOutputStream dos) {
		try {
			log.debug("HTTP/1.1 " + statusCode.getStatusCodeValue()); // statusLine
			debugResponseHeaders();

			dos.writeBytes("HTTP/1.1 " + statusCode.getStatusCodeValue() + " \r\n");
			writeBytesResponseHeader(dos);

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