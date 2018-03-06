package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;

public class InputHandler {
	private static final Logger log = LoggerFactory.getLogger(InputHandler.class);

	public static byte[] doRequest(InputStream in) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		String line = br.readLine();
		log.debug("header : " + line);
		String splitLine = HttpRequestUtils.splitString(line);
		String[] urlArray = HttpRequestUtils.splitUrl(splitLine);
		String url = urlArray[0];
		if (urlArray.length >= 2) {
			createUser(urlArray[1]);
		}
		log.debug("split string result : " + url);
		while (!"".equals(line)) {
			line = br.readLine();
			log.debug("header : " + line);
			if (line == null) {
				break;
			}
		}
		byte[] body = pathByteArray(url);
		if (url.equals("/index.html")) {
			log.debug("if statement - /index.html");
			return pathByteArray(url);
		}

		if (url.equals("/user/form.html")) {
			log.debug("if statement - /user/form.html");
			return pathByteArray(url);
		}

		return body;
	}

	static void createUser(String queryString) throws UnsupportedEncodingException {
		Map<String, String> userMap = HttpRequestUtils.parseQueryString(queryString);
		DataBase.addUser(
				new User(userMap.get("userId"), userMap.get("password"), userMap.get("name"), userMap.get("email")));
		log.debug("[ createUser method ]  - user name : "
				+ URLDecoder.decode(DataBase.findUserById("javajigi").getName(), "UTF-8"));

	}

	static byte[] pathByteArray(String url) throws IOException {
		return Files.readAllBytes(new File("./webapp" + url).toPath());
	}
}
