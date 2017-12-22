package util;

import java.util.Map;

public class SplitUtils {
	public static String getSplitedValue(String inputValue, String pivot, int num) {
		String[] tokens = inputValue.split(pivot);
		return tokens[num];
	}

	public static String getSplitedBackValue(String inputValue, String pivot, int num) {
		String[] tokens = inputValue.split(pivot);
		String resultValue = "";
		for (int i = num + 1; i < tokens.length; i++) {
			resultValue += tokens[i];
			if (i > 0 && i < tokens.length - 1) {
				resultValue += pivot;
			}
		}
		return resultValue;
	}

	public static String getSplitedExtension(String inputValue) {
		return inputValue.substring(inputValue.lastIndexOf('.') + 1, inputValue.length());
	}
	
	public static String valueToStringOrEmpty(Map<String, ?> map, String key) {
		Object value = map.get(key);
		return value == null ? "" : value.toString();
	}
}