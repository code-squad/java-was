package util;

public class StringUtils {
	
	public static String directoryFromRequestHeader(String header) {
		String [] inputArray = header.split(" ");
		return inputArray[1];
	}

}
