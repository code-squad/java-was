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

import request.RequestHeader;
import util.IOUtils;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;
	private RequestHeaderHandler requestHeaderHandler = new RequestHeaderHandler();

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader bufferedReader = convertToBufferedReader(in);
			String line = bufferedReader.readLine();
			log.debug("Request line : {}", line);
			RequestHeader request = new RequestHeader(line);
			while (!"".equals(line) && line != null) {
				line = bufferedReader.readLine();
				request.addLine(line);
				log.debug("Header : {}", line);
			}
			log.debug("Path : {}", request.getPathValue());
			int contentLength = request.getContentLength();
			if (contentLength > 0) {
				request.setRequestBody(String.valueOf(IOUtils.readData(bufferedReader, contentLength)));
				log.debug("Body : {}", request.getRequestBody());
			}
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			DataOutputStream dos = new DataOutputStream(out);
			
			PathFileReader pathFileReader = new PathFileReader("./webapp");
			String responseValue = requestHeaderHandler.getResponseValue(request);
			ResponseHeaderHandler responseHeaderHandler = new ResponseHeaderHandler(responseValue, pathFileReader);
			responseHeaderHandler.setResponseHeaderList(requestHeaderHandler.getResponseHeaderList());
			responseHeaderHandler.response(dos);
			
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private BufferedReader convertToBufferedReader(InputStream inputStream) throws UnsupportedEncodingException {
		return new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
	}
}
