package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controller.PathController;
import model.HeaderRequest;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	PathController pathController = new PathController();

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
		pathController.setRootDir("./webapp");
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader bufferedReader = convertToBufferedReader(in);
			String line = bufferedReader.readLine();
			log.debug("Request line : " + line);
			HeaderRequest request = new HeaderRequest(line);
			while (!"".equals(line) && line != null) {
				line = bufferedReader.readLine();
				request.addLine(line);
				log.debug("Header : " + line);
			}
			log.debug("Path : " + request.getPathValue());
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			DataOutputStream dos = new DataOutputStream(out);
			
			//이 부분에 html파일이 아닌경우에 다르게 처리하거나 특정 명령어 일때 어떻게 처리할지 확인
			//지금 대부분이 html파일이므로 html 먼저 정상 처리 하도록 하는게 좋을듯..
			
			String finalUrl = pathController.changeFinalUrl(request.getRequestPath());			
			
			PathFileReader pathFileReader = new PathFileReader("./webapp");
			byte[] body = pathFileReader.getReadAllBytes(finalUrl);
			
			response200Header(dos, body.length);
			responseBody(dos, body);
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private BufferedReader convertToBufferedReader(InputStream inputStream) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
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
