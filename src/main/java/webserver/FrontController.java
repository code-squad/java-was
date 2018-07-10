package webserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.User;
import util.HttpRequestUtils;
import util.IOUtils;
import util.RequestHeaderUtil;

public class FrontController {

	private static final Logger log = LoggerFactory.getLogger(FrontController.class);

	private static final String POST_TYPE = "POST";
	
	
	public static String doProcess(BufferedReader bufferReader) throws IOException {

		String firstLine = bufferReader.readLine();
		String requestAddress = RequestHeaderUtil.getAddress(firstLine);
		log.debug("firstLine :{}", firstLine);
		log.debug("requestAddress :{}", requestAddress);

		if (firstLine.contains(POST_TYPE)) {

			int contentLength = 0;
			String line = firstLine;
			while (!line.equals("")) {
				line = bufferReader.readLine();
				log.debug("oneLine :{}", line);

				if (contentLength == 0) {
					contentLength = RequestHeaderUtil.getContentLength(line);
				}
				log.debug("contentLength 변화 : {}", contentLength);
			}
			String data = IOUtils.readData(bufferReader, contentLength);
			line = bufferReader.readLine();

			log.debug("empty line : {}", line);
			log.debug("bodyData : {}",data.toString());

			Map<String, String> userData = HttpRequestUtils.parseQueryString(data);
			User user = new User(userData.get("userId"), userData.get("password"), userData.get("name"),
					userData.get("email"));
			log.debug("user : {}", user.toString());
			requestAddress = "/index.html";
		}

		log.debug("final requestAddress : {}", requestAddress);
		return requestAddress;

	}

}
