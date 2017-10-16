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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
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
        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            DataOutputStream dos = new DataOutputStream(out);
            String line = br.readLine();
            String requestType = HttpRequestUtils.getRequestType(line);
            String requestUrl = HttpRequestUtils.getRequestUrl(line);

            int length = 0;
            
            while(!line.equals("")) {
            		line = br.readLine();
            		if(line.contains("Content-Length")) {
            			length = Integer.parseInt(line.split(" ")[1]);
            		}
            }
            
            try {
	        		if(requestType.equals("GET")) {
	        			byte[] body = Files.readAllBytes(new File("./webapp" + requestUrl).toPath());
	        			response200Header(dos, body.length);
	        			responseBody(dos, body);
	        		}else {
	        			Map<String, String> map = HttpRequestUtils.parseQueryString(IOUtils.readData(br, (length)));
	        			User user = new User(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
	        			log.debug(user.toString());
	        			
	        			response302Header(dos);
	        		}
            }catch(IOException e) {
            		response404Header(dos);
            }
  
        } catch (IOException e) {
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
    
    private void response404Header(DataOutputStream dos) {
    		try {
    			 dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
    		}catch(IOException e) {
    			log.error(e.getMessage());
    		}
    }
    
    private void response302Header(DataOutputStream dos) {
    		try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: /index.html");
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
