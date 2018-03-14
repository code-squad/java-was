package webserver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
	private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);
	
	public HttpResponse(OutputStream out) {
		
	}
	
	public void forward(String url) {
		
	}
	
	public void sendRedirect(String url) {
		
	}
	
	
	
	
	
//	
//	
//	
//	
//	byte[] pathByteArray(String url) throws IOException {
//		return Files.readAllBytes(new File("./webapp" + url).toPath());
//	}
//
//	private void response200(byte[] body, OutputStream out) {
//		DataOutputStream dos = new DataOutputStream(out);
//		response200Header(dos, body.length);
//		responseBody(dos, body);
//	}
//
//	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
//		try {
//			dos.writeBytes("HTTP/1.1 200 OK \r\n");
//			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
//			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//			dos.writeBytes("\r\n");
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//	}
//
//	private void response200CSS(byte[] body, OutputStream out) {
//		DataOutputStream dos = new DataOutputStream(out);
//		response200CSSHeader(dos, body.length);
//		responseBody(dos, body);
//	}
//
//	private void response200CSSHeader(DataOutputStream dos, int lengthOfBodyContent) {
//		try {
//			dos.writeBytes("HTTP/1.1 200 OK \r\n");
//			dos.writeBytes("Content-Type: text/css \r\n");
//			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
//			dos.writeBytes("\r\n");
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//	}
//
//	private void responseBody(DataOutputStream dos, byte[] body) {
//		try {
//			dos.write(body, 0, body.length);
//			dos.flush();
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//	}
//
//	private void response302Header(String url, OutputStream out) {
//		DataOutputStream dos = new DataOutputStream(out);
//		try {
//			dos.writeBytes("HTTP/1.1 302 Found \r\n");
//			dos.writeBytes("Location: " + url + "\r\n");
//			if (login) {
//				dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
//			}
//			if (!login) {
//				dos.writeBytes("Set-Cookie: logined=false; Path=/ \r\n");
//			}
//			dos.writeBytes("\r\n");
//		} catch (IOException e) {
//			log.error(e.getMessage());
//		}
//	}
//	
}
