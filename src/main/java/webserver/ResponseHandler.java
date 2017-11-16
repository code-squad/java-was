package webserver;

import java.io.DataOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseHandler {
	private static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);
	
	public static void responseLoginSucess302Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /" + redirectUrl + "\r\n");
			dos.writeBytes("Set-Cookie: logined=true; Path=/" + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	public static void sendRedirect(String path) {
		
	}
	
	public static void ok(String file) {
		
	}

	public static void responseLoginFailed302Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /" + redirectUrl + "\r\n");
			dos.writeBytes("Set-Cookie: logined=false; Path=/" + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	public static void response200HeaderHtmlRequrest(DataOutputStream dos, int lengthOfBodyContent, String requestUrl) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}
	
	public static void response200HeaderCssRequrest(DataOutputStream dos, int lengthOfBodyContent, String url) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
	}

	public static void response200Header(DataOutputStream dos, int lengthOfBodyContent, String url) {
		if(url.contains("html")) {
			response200HeaderHtmlRequrest(dos, lengthOfBodyContent, url);
		}else if(url.contains("css")) {
			response200HeaderCssRequrest(dos, lengthOfBodyContent, url);
		}else {
			response200HeaderCssRequrest(dos, lengthOfBodyContent, url);
		}
	}

	public static void response404Header(DataOutputStream dos) {
		try {
			dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public static void response302Header(DataOutputStream dos, String redirectUrl) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: /" + redirectUrl + " \r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public static void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
