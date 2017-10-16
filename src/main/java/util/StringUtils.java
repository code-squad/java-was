package util;

public class StringUtils {
	
	public static String directoryFromRequestHeader(String header) {
		String [] inputArray = header.split(" ");
		return inputArray[1];
	}
	
	public static String parseQueryString(String query) {
		String [] inputArray = query.split("\\?");
		return inputArray[1];
	}

}
