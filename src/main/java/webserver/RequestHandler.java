package webserver;

import db.DataBase;
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
	private String cookie;

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

			cookie = headerParams.get("Cookie");

			DataOutputStream dos = new DataOutputStream(out);
			if (method.equals("GET")) {
				String query = (url.length > 1) ? url[1] : "";
				dispatchHttpGet(path, HttpRequestUtils.parseQueryString(query));

				byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());

				response200Header(dos, body.length);
				responseBody(dos, body);
			}

			if (method.equals("POST")) {
				String body = IOUtils.readData(br, Integer.parseInt(headerParams.get("Content-Length")));
				dispatchHttpPost(path, HttpRequestUtils.parseQueryString(body), dos);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void dispatchHttpGet(String path, Map<String, String> queryParams) {
		switch (path) {
			case "/user/create":
				createUserFromParams(queryParams);
				break;
		}
	}

	private void dispatchHttpPost(String path, Map<String, String> bodyParams, DataOutputStream dos) {
		switch (path) {
			case "/user/create":
				createUserFromParams(bodyParams, dos);
				break;
			case "/user/login":
				loginUserFromParams(bodyParams, dos);
		}
	}

	private void createUserFromParams(Map<String, String> params) {
		User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		DataBase.addUser(user);
		log.debug("user: {}", user);
	}

	private void createUserFromParams(Map<String, String> params, DataOutputStream dos) {
		createUserFromParams(params);
		response302Header(dos, "/index.html");
	}

	private void loginUserFromParams(Map<String, String> params, DataOutputStream dos) {
		User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		User existedUser = DataBase.findUserById(params.get("userId"));
		log.debug("user: {}, existedUser", user, existedUser);

		if (!user.equals(existedUser)) {
			cookie = "logined=false";
			response302Header(dos, "/user/login_failed.html");
			return;
		}

		cookie = "logined=true";
		response302Header(dos, "/index.html");
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			if (cookie != null && !cookie.isEmpty()) {
				dos.writeBytes("Set-Cookie: " + cookie + "; Path=/\r\n");
			}
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String location) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: " + location + "\r\n");
			if (cookie != null && !cookie.isEmpty()) {
				dos.writeBytes("Set-Cookie: " + cookie + "; Path=/\r\n");
			}
			dos.writeBytes("\r\n");
			dos.flush();
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
}
