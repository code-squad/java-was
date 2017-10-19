package webserver;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import request.GetMapper;
import request.PostMapper;
import request.RequestMapper;
import util.HttpRequestParser;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	private Socket connection;
	private Map<String, RequestMapper> requsetStrategy;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
		this.requsetStrategy = new HashMap<String, RequestMapper>();
		initRequestMethod();
	}
	
	public void initRequestMethod() {
		this.requsetStrategy.put("GET",new GetMapper());
		this.requsetStrategy.put("POST",new PostMapper());
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
		String requestType = parser.getRequestType();
		log.debug(requestType);
		requsetStrategy.get(requestType).run(parser, dos);
	}
	
}
