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
import java.util.Optional;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

	private static final String COOKIE = "Cookie";
	private static final String LOGIN_SUCCESS = "logined=true";
	private static final String LOGIN_FAIL = "logined=false";

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
			log.debug("method: {}, url: {}", method, url);

			Map<String, String> headerParams = parseHeaderParams(br);
			DataOutputStream dos = new DataOutputStream(out);

			String body = IOUtils.readData(br, Integer.parseInt(Optional.ofNullable(headerParams.get("Content-Length")).orElse("0")));
			dispatchHttpRequest(method, url, headerParams, HttpRequestUtils.parseQueryString(body), dos);
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	public Map<String, String> parseHeaderParams(BufferedReader br) throws IOException {
		Map<String, String> headerParams = new HashMap<>();
		String line = null;

		while (line == null || !line.isEmpty()) {
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

		return headerParams;
	}

	private void dispatchHttpRequest(String method, String[] url, Map<String, String> headerParams, Map<String, String> bodyParams, DataOutputStream dos) throws IOException {
		String path = url[0];

		if (method.equals("GET")) {
			String query = (url.length > 1) ? url[1] : "";
			dispatchHttpGet(path, headerParams, HttpRequestUtils.parseQueryString(query), dos);
		}

		if (method.equals("POST")) {
			String location = dispatchHttpPost(path, headerParams, bodyParams);
			response302Header(dos, location, headerParams.get(COOKIE));
		}
	}

	private void dispatchHttpGet(String path, Map<String, String> headerParams, Map<String, String> queryParams, DataOutputStream dos) throws IOException {
		if (path.equals("/user/create")) {
			createUserFromParams(queryParams);
		}

		if (path.equals("/user/list")) {
			if (headerParams.get(COOKIE).contains(LOGIN_SUCCESS)) {
				byte[] body = createUserListHtml().getBytes();
				response200Header(dos, body.length, headerParams.get(COOKIE));
				responseBody(dos, body);

				return;
			}

			path = "/user/login.html";
		}

		byte[] body = Files.readAllBytes(new File("./webapp" + path).toPath());
		response200Header(dos, body.length, headerParams.get(COOKIE));
		responseBody(dos, body);
	}

	private String dispatchHttpPost(String path, Map<String, String> headerParams, Map<String, String> bodyParams) {
		if (path.equals("/user/create")) {
			return createUserFromParams(bodyParams);
		}

		if (path.equals("/user/login")) {
			if (headerParams.get(COOKIE).contains(LOGIN_SUCCESS)) {
				return "/index.html";
			}

			if (loginUserFromParams(bodyParams)) {
				headerParams.put(COOKIE, LOGIN_SUCCESS);
				return "/index.html";
			}

			headerParams.put(COOKIE, LOGIN_FAIL);
			return "/user/login_failed.html";
		}

		return "";
	}

	private String createUserFromParams(Map<String, String> params) {
		User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		DataBase.addUser(user);
		log.debug("user: {}", user);

		return "/index.html";
	}

	private boolean loginUserFromParams(Map<String, String> params) {
		User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
		User existedUser = DataBase.findUserById(params.get("userId"));
		log.debug("user: {}, existedUser", user, existedUser);

		if (!user.equals(existedUser)) {
			return false;
		}

		return true;
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String cookie) {
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

	private void response302Header(DataOutputStream dos, String location, String cookie) {
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

	private String createUserListHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang=\"kr\">\n");
		sb.append("<head>\n");
		sb.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">\n");
		sb.append("<meta charset=\"utf-8\">\n");
		sb.append("<title>SLiPP Java Web Programming</title>\n");
		sb.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1, maximum-scale=1\">\n");
		sb.append("</head>\n");
		sb.append("<body>\n");
		sb.append("<ul>\n");

		for (User user : DataBase.findAll()) {
			sb.append(String.format("<li>%s</li>\n", user.toString()));
		}

		sb.append("</ul>\n");
		sb.append("</body>\n");
		sb.append("</html>\n");

		return sb.toString();
	}
}
