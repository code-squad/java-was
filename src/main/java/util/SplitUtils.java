package util;

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
}