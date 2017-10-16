package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;

import java.nio.file.*;

import util.HttpRequestUtils;
import util.IOUtils;
import util.StringUtils;

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
			DataOutputStream dos = new DataOutputStream(out);
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			String requestFirstLine = br.readLine();
			String requestFile = StringUtils.directoryFromRequestHeader(requestFirstLine);
			
			if(HttpRequestUtils.parseRequestType(requestFirstLine) == HttpRequestUtils.RequestTypes.POST) {
				
			}
			
			else if(requestFile.indexOf('?') >= 0) {
				log.debug("user info received.");
				String query = StringUtils.parseQueryString(requestFile);
				Map<String, String> UserInfo = HttpRequestUtils.parseQueryString(query);
				DataBase.addUser(new User(UserInfo.get("userId"), UserInfo.get("password"), UserInfo.get("name"), UserInfo.get("email")));
				log.debug(DataBase.findUserById(UserInfo.get("userId")).toString());
			}
			else {
			log.debug(requestFile);
			byte[] body = Files.readAllBytes(new File("./webapp" + requestFile).toPath());
			if (requestFile.contains("css")) {
				response200HeaderStaticCss(dos, body.length);
			} else {
				response200Header(dos, body.length);
			}
			responseBody(dos, body);
		}} catch (IOException e) {
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

	private void response200HeaderStaticCss(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/css;charset=utf-8\r\n");
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
