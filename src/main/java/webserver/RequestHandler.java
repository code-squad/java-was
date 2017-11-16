package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.HttpRequestUtils.Pair;
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
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String line = br.readLine();
			String url = HttpRequestUtils.parseUrl(line);
			Map<String, String> headers = new HashMap<>();
			
			while(!"".equals(line) &&line != null) {
				log.info(line);
				line = br.readLine();
				Pair pair = HttpRequestUtils.parseHeader(line);
				if(pair != null) {
					headers.put(pair.getKey(), pair.getValue());
				}
			}
			DataOutputStream dos = new DataOutputStream(out);

			if("/user/login".equals(url)) {
				String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
				Map<String, String> parameters = HttpRequestUtils.parseQueryString(query);
				User user = DataBase.findUserById(parameters.get("userId"));
				if(user != null && user.matchPassword(parameters.get("password"))) {
					response302CookieHeader(dos, "/index.html");
				}
				else {
					response302Header(dos, "/user/login_failed.html");
				}
			}
			
			if("/user/create".equals(url)) {
				String query = IOUtils.readData(br, Integer.parseInt(headers.get("Content-Length")));
				Map<String, String> parameters = HttpRequestUtils.parseQueryString(query);
				DataBase.addUser(new User(parameters.get("userId"),parameters.get("password"),parameters.get("name"),parameters.get("email")));
				response302Header(dos, "/index.html");
			}
			if("/user/list".equals(url)) {
				Map<String, String> cookies = HttpRequestUtils.parseCookies( headers.get("Cookie"));
				String logined = cookies.get("logined");
				if ( logined == null || logined.equals("false") ) {
					url = "/user/login.html";
					response302Header(dos, url);	
				}	else {
					url = "/user/list.html";
					byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
					body = IOUtils.addUserList(body);
					response200(dos, body, headers);
				}
			}
			else {
				byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
				response200(dos, body, headers);
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
	
	private void response200(DataOutputStream dos, byte[] body, Map<String, String> headers) {
		response200Header(dos, body.length, headers);
		responseBody(dos, body);
	}

	private void response302CookieHeader(DataOutputStream dos, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Set-Cookie: logined=true; Path=/ \r\n");
			dos.writeBytes("Location: ");
			dos.writeBytes(url);
			dos.flush();
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response302Header(DataOutputStream dos, String url) {
		try {
			dos.writeBytes("HTTP/1.1 302 Found \r\n");
			dos.writeBytes("Location: ");
			dos.writeBytes(url);
			dos.flush();
		} catch(IOException e) {
			log.error(e.getMessage());
		}
	}

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent, Map<String, String> headers) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: " + headers.get("Accept") + "\r\n");
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
}
