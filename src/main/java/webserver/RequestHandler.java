package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.HttpRequestParser;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private GetHandler getHandler;
	private PostHandler postHandler;
	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
		getHandler = new GetHandler();
		postHandler = new PostHandler();
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			DataOutputStream dos = new DataOutputStream(out);
			HttpRequestParser parser = new HttpRequestParser(in);
			try {
				log.debug("요청이 들어왔습니다");
				requestHandlling(parser, dos);
			} catch (IOException e) {
				log.debug("404 Error");
				ResponseHandler.response404Header(dos);
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void requestHandlling(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		runRequestLogic(parser, dos);
	}

	private void runRequestLogic(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		String requestType = parser.getRequestType();
		if(requestType.equals("GET")) {
			this.getHandler.getHandling(parser, dos);
		}else {
			this.postHandler.postHandling(parser, dos);
		}
		
	}
	
}
