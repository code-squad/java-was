package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import webserver.FrontController;

public class RequestHeaderUtil {

	private static final int ONE = 0;
	private static final int TWO = 1;
	private static final Logger log = LoggerFactory.getLogger(RequestHeaderUtil.class);


	public static String getAddress(String line) throws IOException {
		String secondPart = requestFirstline(line).get(TWO).trim();
		return secondPartsDatas(secondPart).get(ONE).trim();
	}

	public static String parameterToQueryString(String line) throws IOException {
		String secondPart = requestFirstline(line).get(TWO).trim();
		if(secondPartsDatas(secondPart).size()!=2) {
			return "";
		}
		return secondPartsDatas(secondPart).get(TWO).trim();
	}

	public static List<String> requestFirstline(String line) throws IOException {
		return Arrays.asList(line.split(" "));
	}

	public static List<String> secondPartsDatas(String secondPart) {
		return Arrays.asList(secondPart.split("\\?"));
	}

	public static int getContentLength(String line) throws IOException {
		
			if(line.contains("Content-Length")) {
			log.debug(line.split(":")+"content-Length-part");
			return Integer.parseInt(line.substring(line.indexOf(":")+1).trim());
			}
		
		return 0;
	}

}
