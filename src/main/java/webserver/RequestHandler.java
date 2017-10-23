package webserver;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import mvc.Controller;
import mvc.RequestMapping;
import util.HttpRequestUtils;

import util.IOUtils;
import util.StringUtils;

public class RequestHandler extends Thread {
	String postValue;
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	DataBase db = new DataBase();
	
	private Socket connection;
	
	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
				connection.getPort());

		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			
			HttpRequest request = new HttpRequest(br,db);
			
			RequestMapping mapping = new RequestMapping();
			Controller controller = mapping.getController(request.getUrl());
			controller.inputParameter(request, br);
			
			HttpResponse response = new HttpResponse(request, out);
			controller.execute(request,response);
			db = controller.syncDataBase();

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}





}
