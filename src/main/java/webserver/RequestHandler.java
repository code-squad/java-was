package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private Socket connection;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			if (line == null) {
				return;
			}

			String[] tokens = line.split(" ");
			String method = tokens[0];
			String[] url = tokens[1].split("\\?");
			String path = url[0];
			log.debug("method: {}, path: {}", method, path);

			Map<String, String> headerParams = new HashMap<>();

			while (!line.isEmpty()) {
				line = br.readLine();
				if (line.isEmpty()) {
					break;
				}
				log.debug("line: {}", line);

				HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
				if (pair != null) {
					headerParams.put(pair.getKey(), pair.getValue());
				}
			}

			DataOutputStream dos = new DataOutputStream(out);
			if (method.equals("GET")) {
				String query = (url.length > 1) ? url[1] : "";
				dispatchHttpGet(path, query);

				byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

				response200Header(dos, body.length);
				responseBody(dos, body);
			}

			if (method.equals("POST")) {
				String body = IOUtils.readData(br, Integer.parseInt(headerParams.get("Content-Length")));
				dispatchHttpPost(path, body);

				response302Header(dos, "/index.html");
			}

			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void dispatchHttpGet(String path, String query) {
		switch (path) {
			case "/user/create":
				createUserFromParams(HttpRequestUtils.parseQueryString(query));
				break;
		}
	}

	private void dispatchHttpPost(String path, String body) {
		switch (path) {
			case "/user/create":
				createUserFromParams(HttpRequestUtils.parseQueryString(body));
				break;
		}
	}

	private void createUserFromParams(Map<String, String> params) {
		User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		log.debug("user: {}", user);
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

	private void response302Header(DataOutputStream dos, String location) {
		log.debug("here");
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + location + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
