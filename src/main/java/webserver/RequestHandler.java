package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.PathHandler;
import request.RequestMapper;
import util.HttpRequestParser;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private Socket connection;
	private RequestStrategies requsetStrategy;

	public RequestHandler(Socket connectionSocket, RequestStrategies requsetStrategies) {
		this.connection = connectionSocket;
		this.requsetStrategy = requsetStrategies;
	}
	
	public void initRequestMethod() {
		
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		DataOutputStream dos = null;
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			dos = new DataOutputStream(out);
			HttpRequestParser parser = new HttpRequestParser(in);
			log.debug("요청이 들어왔습니다");
			requestHandlling(parser, dos);
			log.debug("404 Error");
		} catch (IOException e) {
			log.error(e.getMessage());
			ResponseHandler.response404Header(dos);
		}
	}

	private void requestHandlling(HttpRequestParser parser, DataOutputStream dos) throws IOException {
		String requestType = parser.getRequestType();
		String url = parser.getUrl();
		PathHandler pathHandler = this.requsetStrategy.getPathHandler(requestType, url);
		pathHandler.run(parser, dos);
	}
	
}
