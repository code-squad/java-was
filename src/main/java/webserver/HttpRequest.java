package webserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import util.StringUtils;

public class HttpRequest {
	String url;
	Map<String, String> headers = new HashMap<String, String>();
	DataBase db = new DataBase();
	String cookieValue;
	String postValue = "";
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	public HttpRequest(BufferedReader br, DataBase db) throws IOException {
		this.db = db;
		this.url = getDefaultPath(br.readLine().split(" "));
		this.headers = getHeader(br);
		this.cookieValue = new String();
		this.postValue = new String();
		
//		getParameter(br);
	}

	public String getDefaultPath(String[] tokens) {
		return tokens[1];
	}

	public Map<String, String> getHeader(BufferedReader br) throws IOException {
		String[] tokens;
		String line = br.readLine();

		while (!"".equals(line)) {
			line = br.readLine();
			tokens = line.split(": ");
			if (tokens.length == 2) {
				headers.put(tokens[0], tokens[1]);
			}
		}
		return headers;
	}

	public void getParameter(BufferedReader br) throws IOException {
		IOUtils io = new IOUtils();
		postValue = io.readData(br, Integer.parseInt(headers.get("Content-Length")));;
	}
	public DataBase createUser() {
		User user = generateUser(postValue);
		db.addUser(user);
		return db;
	}
	public void getCookie() {
		cookieValue = headers.get("Cookie");
		cookieValue = cookieValue.split("; ")[1];
	}

	public User generateUser(String url) {
		HttpRequestUtils hru = new HttpRequestUtils();
		
		String userId = hru.parseQueryString(url).get("userId");
		String password =hru.parseQueryString(url).get("password");
		String name = hru.parseQueryString(url).get("name");
		String email = hru.parseQueryString(url).get("email");
		return new User(userId,password,name,email);
	}
	
	public DataBase getDb() {
		return db;
	}

	public String getUrl() {
		return url;
	}

	public String getPostValue() {
		return postValue;
	}


}
