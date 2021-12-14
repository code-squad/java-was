package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpMethod;
import util.HttpRequestUtils;
import util.IOUtils;

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
			BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
			String line = br.readLine();
			log.debug("request line: {} ", line);

			if (line == null) {
				return;
			}

			String[] tokens = line.split(" ");
			HttpMethod httpMethod = HttpMethod.valueOf(tokens[0]);
			String url = tokens[1];

			if (httpMethod.equals(HttpMethod.GET)) {
				httpMethodGetHandler(out, br, url);
			}

			if (httpMethod.equals(HttpMethod.POST)) {
				httpMethodPostHandler(out, br, url);
			}

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void httpMethodGetHandler(OutputStream out, BufferedReader br, String url) throws IOException {
		if (url.contains("/user/list")) {
			userListResponseHandler(out, br, url);
			return;
		}
		byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
		log.trace("body : {}", new String(body, StandardCharsets.UTF_8));
		DataOutputStream dos = new DataOutputStream(out);
		if (url.endsWith(".css")) {
			response200Header(dos, body.length, "text/css");
		}
		if (! url.endsWith(".css")) {
			response200Header(dos, body.length);
		}
		responseBody(dos, body);
	}

	private void httpMethodPostHandler(OutputStream out, BufferedReader br, String url) throws IOException {
		Map<String, String> headers = readRequestHeader(br);
		int contentLength = Integer.parseInt(headers.getOrDefault("Content-Length", "0"));
		String body = IOUtils.readData(br, contentLength);
		log.debug("body: {}", body);

		Map<String, String> parameterMap = HttpRequestUtils.parseQueryString(body);
		log.debug("parameterMap: {}", parameterMap);

		if (url.contains("create")) {
			userSignUpWithPostMethod(parameterMap, out);
		}

		if (url.contains("login")) {
			userLoginWithPostMethod(parameterMap, out);
		}
	}

	private void userSignUpWithPostMethod(Map<String, String> parameterMap, OutputStream out) throws IOException {
		log.debug("parameterMap: {}", parameterMap);
		User user = User.ofMap(parameterMap);
		DataBase.addUser(user);
		log.debug("user: {}", user);
		DataOutputStream dos = new DataOutputStream(out);
		response302Header(dos, "/index.html");
	}

	private void userLoginWithPostMethod(Map<String, String> parameterMap, OutputStream out) throws IOException {
		User loginUser = DataBase.findUserById(parameterMap.get("userId"));
		String inputPassword = parameterMap.get("password");
		boolean setCookie = false;
		String redirectUrl = "/user/login_failed.html";

		if (loginUser != null && loginUser.getPassword().equals(inputPassword)) {
			redirectUrl = "/index.html";
			setCookie = true;
		}

		DataOutputStream dos = new DataOutputStream(out);
		response302Header(dos, setCookie, redirectUrl);
	}

	private void userListResponseHandler(OutputStream out, BufferedReader br, String url) throws IOException {
		Map<String, String> requestHeaders = readRequestHeader(br);
		Map<String, String> cookies = HttpRequestUtils.parseCookies(requestHeaders.get("Cookie"));
		boolean isLoggedIn = Boolean.parseBoolean(cookies.get("loggedIn"));
		DataOutputStream dos = new DataOutputStream(out);

		if (! isLoggedIn) {
			response302Header(dos, isLoggedIn, "/index.html");
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("<tbody>");
		Collection<User> users = DataBase.findAll();
		List<User> userList = new ArrayList<>(users);

		for (int index = 0; index < userList.size(); index++) {
			User user = userList.get(index);
			sb.append("<tr>").append("<th scope=\"row\">")
				.append(index).append("</th> <td>").append(user.getUserId())
				.append("</td> <td>").append(user.getName())
				.append("</td> <td>").append(user.getEmail())
				.append("</td><td><a href=\"#\" class=\"btn btn-success\" role=\"button\">수정</a></td>").append("</tr>");
		}
		sb.append("</tbody>");
		byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
		body = new String(body, StandardCharsets.UTF_8).replaceAll("\\{\\{users}}", sb.toString()).getBytes();
		response200Header(dos, body.length);
		responseBody(dos, body);
	}

	private Map<String, String> readRequestHeader(BufferedReader br) throws IOException {
		Map<String, String> requestHeaders = new HashMap<>();

		while (true) {
			String line = br.readLine();
			if ("".equals(line)) {
				break;
			}
			String[] header = line.split(": ");
			requestHeaders.put(header[0], header[1]);
		}
		return requestHeaders;
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

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: " + contentType + "\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
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

	private void response302Header(DataOutputStream dos, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
			dos.writeBytes("Location: " + url + " \r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, boolean setCookie, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Redirect \r\n");
			dos.writeBytes("Cookie: loggedIn:" + setCookie + " Path=/\r\n");
			dos.writeBytes("Location: " + url + " \r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}
